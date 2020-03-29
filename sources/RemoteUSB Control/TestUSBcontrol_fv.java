package org.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;


public class TestUSBcontrol {
	private static EV3UltrasonicSensor uSensor;
	
	public static boolean estOuvert(){
		SampleProvider sampleProvider = uSensor.getDistanceMode();
		float[] sample = new float[sampleProvider.sampleSize()];
		sampleProvider.fetchSample(sample, 0);
		if(sample[0]>8){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		String clientRequest;
		String serverResponse;
		uSensor = new EV3UltrasonicSensor(SensorPort.S2);
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
					if(text.equals("1") && !estOuvert())
					{
						// commenter ces deux lignes
						LCD.clear();
						OpenDoor();
					}
					else if(text.equals("2") && estOuvert())
					{
						//commenter ces deux lignes
						LCD.clear();
						CloseDoor();
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
	public static void OpenDoor()
	{
		Motor.A.setSpeed(90);
		Motor.D.setSpeed(90);
		Motor.A.forward();
		Motor.D.backward();
		Motor.A.rotateTo(-90);
		Motor.D.stop();
		Motor.A.stop();
	}
	
	public static void CloseDoor()
	{
		Motor.A.setSpeed(90);
		Motor.D.setSpeed(90);
		Motor.A.forward();
		Motor.D.forward();
		Motor.A.rotateTo(0);
		Motor.D.stop();
		Motor.A.stop();
	}


	}


