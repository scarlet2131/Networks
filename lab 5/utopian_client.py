import datetime
import json
import math
import os
import requests
import threading
from multiprocessing import Pool
from functools import partial
from dateutil.parser import parse
from pymongo import MongoClient
from steem import Steem

try:
    from urllib import urlencode
except ImportError:
    from urllib.parse import urlencode

CLIENT = MongoClient()
DB = CLIENT.utopian

UTOPIAN_API = "https://api.utopian.io/api/"

HEADERS = {
    "Origin": "https://utopian.info",
    "Accept": "application/json",
    "x-api-key": os.environ["API_KEY"],
    "x-api-key-id": os.environ["API_KEY_ID"]
}


def generate_url(action, parameters):
    return f"{UTOPIAN_API}{action}/?{urlencode(parameters)}"


def create_post(post, status, update=True):
    week = datetime.datetime.now() - datetime.timedelta(days=7)
    if update and parse(post["created"]) < week:
        return None

    new_post = {
        "moderator": None,
        "author": post["author"],
        "permlink": post["permlink"],
        "title": post["title"],
        "repository": post.get("json_metadata").get("repository"),
        "last_update": parse(post["last_update"]),
        "created": parse(post["created"]),
        "active": parse(post["active"]),
        "_id": post["_id"],
        "category": post.get("json_metadata").get("type"),
        "modified": False,
        "status": status,
        "updated": datetime.datetime.now()
    }
    if not status == "pending":
        # Add moderator to post
        moderator = post["json_metadata"]["moderator"]
        try:
            moderator["time"] = parse(moderator.get("time"))
        except TypeError:
            moderator["time"] = new_post["created"]
        new_post["moderator"] = moderator

        # Add post's score
        if "score" in post["json_metadata"].keys():
            new_post["score"] = post["json_metadata"]["score"]
            if new_post["score"] == None:
                new_post["score"] = 0
        else:
            new_post["score"] = 100

        # Add questionaire's questions
        if "questions" in post["json_metadata"].keys():
            new_post["questions"] = post["json_metadata"]["questions"]
        else:
            new_post["questions"] = "N/A"

    return new_post


def get_posts(status, update=True):
    posts = []
    limit = 1000
    skip = 0
    action = "posts"
    posts = DB.posts

    # Get total amount of posts submitted to Utopian.io
    if not status == "pending":
        r = requests.get(generate_url(action, {"status": status, "limit": 1}),
                         headers=HEADERS)
    else:
        r = requests.get(generate_url(action, {"filterBy": "review",
                         "limit": 1}), headers=HEADERS)
    if r.status_code == 200:
        total = r.json()["total"]
        total = math.ceil(total / 1000)
    else:
        time = datetime.datetime.now()
        print(f"{time} - Something went wrong, please try again later.")
        return

    # Get ALL posts submitted to Utopian.io
    if not update:
        for _ in range(total):
            if not status == "pending":
                parameters = {"status": status, "limit": limit, "skip": skip}
            else:
                parameters = {"filterBy": "review", "limit": limit, "skip": skip}
            url = generate_url(action, parameters)
            print(f"{datetime.datetime.now()} - Fetching from {url}")
            r = requests.get(url, headers=HEADERS)
            if r.status_code == 200:
                pool = Pool()
                x = partial(create_post, status=status, update=False)
                post_list = pool.map(x, r.json()["results"])
                pool.close()
                pool.join()
                for post in post_list:
                    if not post == None:
                        posts.replace_one({"_id": post["_id"]}, post, True)
            else:
                time = datetime.datetime.now()
                print(f"{time} - Something went wrong, please try again later.")
                return
            skip += 1000
    # Get posts submitted to Utopian.io within the last week
    else:
        week = datetime.datetime.now() - datetime.timedelta(days=7)
        for _ in range(total):
            parameters = {"status": status, "limit": limit, "skip": skip}
            url = generate_url(action, parameters)
            print(f"{datetime.datetime.now()} - Fetching from {url}")
            r = requests.get(url, headers=HEADERS)
            if r.status_code == 200:
                pool = Pool()
                x = partial(create_post, status=status, update=True)
                post_list = pool.map(x, r.json()["results"])
                pool.close()
                pool.join()
                for post in post_list:
                    if post is None:
                        return
                    database_post = posts.find_one({"_id": post["_id"]})
                    if database_post and "flagged" in database_post:
                        if (not database_post["flagged"] == post["flagged"]
                            or database_post["modified"]):
                            post["modified"] = True
                    posts.replace_one({"_id": post["_id"]}, post, True)
                    if post["created"] < week:
                        return
            else:
                time = datetime.datetime.now()
                print(f"{time} - Something went wrong, please try again later.")
                return

            skip += 1000


def get_moderators():
    action = "moderators"
    url = generate_url(action, {})
    r = requests.get(url, headers=HEADERS)
    if r.status_code == 200:
        return r.json()["results"]
    else:
        time = datetime.datetime.now()
        print(f"{time} - Something went wrong, please try again later.")
        return