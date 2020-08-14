import socket
s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.bind((socket.gethostname(),1234))
s.listen(5)

#while True:
clientSocket,address=s.accept()
print('in main server')
clientSocket.send("Welcome to the main server")
clientSocket.close()
