import socket
s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.bind((socket.gethostname(),12345))
s.listen(5)

#while True:
clientSocket,address=s.accept()
print("Currently in proxy server")


s1=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s1.connect((socket.gethostname(),1234))
print("Currently in proxy client")
msg=s1.recv(1024)

clientSocket.send(msg)
clientSocket.close()