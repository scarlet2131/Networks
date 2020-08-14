import time
import sys
import socket

s = socket.socket()

port = int(2222)
s.bind(('127.0.0.1', port))
#print ("Socket successfully created")
s.listen(5)
print ("Listening on port: %s" %(port))


c, addr = s.accept()
print ('connection set up', addr )

#-----------------------------------------------------------
def parity(a):
    x=0
    for i in a:
        if i =='1':
            x+=1
    if x%2 == 0:
        return '0'
    else:
        return '1'

def inc(x):
    if x=='0':
        return '1'
    else:
        return '0'

def from_physical_layer():
    m = c.recv(1024).decode()
    if m=='':
        return ['','']
    p = m[-1]
    check = parity(m[:-1])

    if p == check:
        return ["frame_arrival", m[:-1]]
    else:
        return ["checksum_error"]

def to_network_layer(mes):
    print(">>Received message: " + mes)
#---------------------------------------------------------

tr = 0
seq_num = 0

mes = c.recv(1024).decode()

buffer=[]
temp=''
count=0
for i in mes:
    temp+=i
    count+=1
    if count==9:
        buffer.append(temp)
        temp=''
        count=0
time.sleep(1)

while True:
    tr+=1
    if tr ==20:
        break

    print("received message: " + buffer[0])
    if tr==8:
        print("ohhh sad!!...damaged frame\n")
        c.send(('1'+str((tr-1)%8)).encode())
    else:
        c.send("0".encode())
        print("YAYY.....acknowledgement sent ")

    new_mes = c.recv(1024).decode()
    buffer.append(new_mes)

    buffer=buffer[1:]



c.close()
