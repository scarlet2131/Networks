import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
	public static void main(String[] args)
	{
		String host="localhost";
		try{

			Scanner scan = new Scanner(System.in);
			
			Socket socket=new Socket(host, 8008);

			System.out.println("Welcome to client side!\n");

			BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));


			int n = scan.nextInt();
			for(int i=0;i<n;i++)
			{
				String s = scan.nextLine();
				out.println(s);
				out.flush();
				if(s.equals("over"))
				{
					break;
				}

			}
			out.println("Over Client");
			out.flush();

			//receive data from server

			String str = null;
			while ((str = in.readLine()) != null) {
				System.out.println(str);
			}
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

			


		}
	}
