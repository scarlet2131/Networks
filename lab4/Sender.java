
import java.util.*;
import java.io.*;
import java.net.*;
public class Sender {

	static ServerSocket s;
	static Socket sock;
	static InputStreamReader in;
	static BufferedReader buff;
	static PrintStream p;

	static Random rn;
	static char getParity(char b[],int p)
	{
		int parity=0;
		for(int i=0;i<b.length;i++)
		{
			if(b[i]!='2')
			{
				int k=i+1;
				String s=Integer.toBinaryString(k);
				int x =((Integer.parseInt(s))/((int)Math.pow(10, p)))%10;
				if(x==1)
				{
					if(b[i]=='1')
					{
						parity=(parity+1)%2;
					}
				}
			}
		}

		String ans=Integer.toString(parity);
		return ans.charAt(0);
	}

	static String generate_code(String a)
	{
		char b[];

		int i=0,parity_count=0,j=0,k=0;
		while(i<a.length())				//parity Bits COUNTING.(m-r+1<=2^r);
		{
			if(Math.pow(2, parity_count)==i+parity_count+1)
			{
				parity_count++;
			}
			else
				i++;
		}
		b=new char[a.length()+parity_count];
		for(i=1;i<=b.length;i++)
		{
			if(Math.pow(2, j)==i)
			{
				b[i-1]='2';				//Leaving Position for Parity Bits. (2 2 1 2 0 1 1). Replace 2 with parity.
				j++;
			}
			else
			{
				b[k+j]=a.charAt(k);
				k++;
			}
		}

		for(i=0;i<parity_count;i++)
		{
			b[((int)Math.pow(2,i))-1]=getParity(b,i);
		}

		String ans="";
		for(i=0;i<b.length;i++)
		{

			ans+=b[i];
		}

		return ans;
	}

	static String ERROR(String data)
	{

		char x[]=data.toCharArray();
		int pos = rn.nextInt(x.length-1);
		if(x[pos] == '1')
			x[pos] = '0';
		else
			x[pos] = '1';
		String ans="";
		for(int i=0;i<x.length;i++)
			ans = ans + x[i];
		return ans;
	}

	public static void main(String args[])throws Exception
	{
		Scanner sc=new Scanner(System.in);
		rn=new Random();
		s=new ServerSocket(5000);

		sock=s.accept();

		in=new InputStreamReader(sock.getInputStream());
		buff=new BufferedReader(in);
		p=new PrintStream(sock.getOutputStream());
		int c;


				System.out.print("Enter the data BITS : ");
				String s=sc.nextLine();
				System.out.println();
				String gen=generate_code(s);
				System.out.println("\nGenerated hamming code : "+gen);

					String Gen = ERROR(gen);
					p.println(Gen);



	}
}
