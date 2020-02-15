import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;

public class TestUSBcontrol  {

	public static void main(String[] args) {

		String clientRequest;
		String serverResponse;

		try(ServerSocket socket = new ServerSocket(5555))
		{
			while(true)
			{
				LCD.drawString("Waiting", 0, 0);
				Socket connectionSocket = socket.accept();
				LCD.clear();
				LCD.drawString("Connected client", 0, 0);
				BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				OutputStream output = connectionSocket.getOutputStream();
				PrintWriter writer = new PrintWriter(output,true);
				String text;
				
				do {
					text = reader.readLine();
					if(text.equals("1"))
					{
						LCD.clear();
						LCD.drawString("Comm 1", 0, 0);
					}
					else if(text.equals("2"))
					{
						LCD.clear();
						LCD.drawString("Comm 2", 0, 0);
					}
					
				}while(!text.equals("leave"));

				socket.close();
			}
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}

		
	}
}
