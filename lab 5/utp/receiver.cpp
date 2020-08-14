#include<stdio.h>
#include<fcntl.h>
#include<string.h>
#include "header.h"
void main()
{
 int pid1,no,i;
 char buffer[50]; 
 frame f;
 pid1=open("pipe1",O_RDONLY); 
 read(pid1,&no,sizeof(no));
 printf("DATA RECEIVED : %d",no);
 printf("\nDATA");
 for(i=0;i<no;i++)
 {
  from_physical_layer(pid1,&f);
  strcpy(buffer,f.data);
  to_network_layer(buffer);
 }
 close(pid1);
 unlink("pipe1");
}