#include<stdio.h>
#include<fcntl.h>
#include<string.h>
typedef struct 
{
 int seqno;
 int ackno;
 char data[50];
}frame;
void from_network_layer(char buffer[])
{
 printf("Enter Data : ");
 scanf("%s",buffer);
}
void to_physical_layer(int pid1,frame *f)
{
 write(pid1,f,sizeof(frame));
}
void from_physical_layer(int pid1,frame *f)
{
 read(pid1,f,sizeof(frame));
}
void to_network_layer(char buffer[])
{ 
 printf("\n%s",buffer);
}