import socket
s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.bind((socket.gethostname(),12345))
s.listen(5)

#while True:
clientSocket,address=s.accept()
print('In proxy server')
#clientSocket.send("Welcome to the proxy server")
#clientSocket.close()

s1=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s1.connect((socket.gethostname(),1234))
print("in proxy client")
msg=s1.recv(1024)
#print(msg)
clientSocket.send(msg)
clientSocket.close()

