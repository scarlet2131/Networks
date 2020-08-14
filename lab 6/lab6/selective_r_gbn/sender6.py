import socket
import time
import sys
import random
import threading

s = socket.socket()
port = int(2222)

lh = '127.0.0.1'
s.connect((lh, port))
print("Connected to host at port " + str(port))
s.settimeout(5.0)
#----------------------------------------------------------------
def parity(a):
    x=0
    for i in a:
        if i =='1':
            x+=1
    if x%2 == 0:
        return '0'
    else:
        return '1'

def from_network_layer():
    #generate random message
    x = bin(random.randint(129, 254))
    return x[6:]

def inc(x):
    if x=='0':
        return '1'
    else:
        return '0'

def to_physical_layer(s, frame):
    print(" OKaY SenT the message : " + frame)
    s.send(frame.encode())

#---------------------------------------------------------------

MAX_TR = 10
tr = 0
seq_num = 0
MAX_SEQ = 8
EVENT_BIT = 0
WINDOW_SIZE = 4
tr=0
mes = ''
buffer = []
for i in range(WINDOW_SIZE):
    frame = from_network_layer()
    frame+=parity(frame)
    #frame = str(seq_num)+frame
    mes+= frame
    buffer.append(frame)
    seq_num = (seq_num+1)%MAX_SEQ

print(buffer)
s.send(mes.encode())

FLAG=0
n=0
while True:
    tr+=1
    if tr ==20:
        break

    ack = s.recv(1024).decode()
    if ack[0] == '1':
         print("Please retransmit for frame seq num : " + ack[1])
         for i in buffer:
             if i[0]==ack[1]:
                 mes = i
    else:
        mes = from_network_layer()
        mes += parity(mes)
        #mes = str(seq_num) + mes
    to_physical_layer(s, mes)
    buffer = buffer[1:]
    buffer.append(mes)
    seq_num = (seq_num+1)%MAX_SEQ
    time.sleep(1)



s.close()
