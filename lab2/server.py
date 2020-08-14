import socket
s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.bind((socket.gethostname(),1234))
s.listen(5)

#while True:
clientSocket,address=s.accept()
print("currently in server")
print 'GOT connection from' ,address
filename='sample.txt'
f=open(filename,'rb')
l=f.read(1024)
while(1) :
	clientSocket.send(1)
	print('sent',repr(1))
	l=f.read(1024)
f.close()	

print('Done sending')
clientSocket.send("Thank u for connecting")
clientSocket.close()