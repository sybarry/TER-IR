package org.client;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientWIFI {

	public static void main(String[] args) {
		try(Socket socket = new Socket("192.168.43.208",5555))
		{
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output,true);
			Scanner sc = new Scanner(System.in);
			String text;
			
			do
			{
				text = sc.nextLine();
				writer.println(text);
			}while(!text.equals("leave"));
			
			socket.close();
		}
		catch(UnknownHostException e)
		{
			System.out.println("Server not found");
		}
		catch(IOException e)
		{
			System.out.println("I/O error");
		}
	
	
		

	}

}

