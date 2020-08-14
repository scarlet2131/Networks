#include<stdio.h>
#include<fcntl.h>
#include<string.h>
#include "header.h"
void main()
{
 int pid1,i,no;
 char buffer[50];
 frame f;
 system(">pipe1");
 pid1=open("pipe1",O_WRONLY);
 printf("Enter NUMBER OF DATA : ");
 scanf("%d",&no);
 write(pid1,&no,sizeof(no));
 for(i=0;i<no;i++)
 {
  from_network_layer(buffer);
  strcpy(f.data,buffer);
  to_physical_layer(pid1,&f);
 }
 close(pid1);
}