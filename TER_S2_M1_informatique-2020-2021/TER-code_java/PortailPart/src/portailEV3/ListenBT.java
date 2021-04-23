/**
  * @file EcouteBT.java
  *
  * @brief PortailPart
  * @package portailEV3
  * @author Gicquel, Guérin, Rozen
  * @since 2/01/2021
  * @version 1.0
  * @date 23/04/2021
  *
*/
package portailEV3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

public class ListenBT extends Thread {

	//Attributs
	private static DataOutputStream dataOut;  
	private static DataInputStream dataIn;
	private static BTConnection BTLink;
	private static boolean app_alive;
	static boolean BTconnect = false;
	volatile int byteRecu=0;
	
	
	/*---------------------------------------------------------------------
    |  @Method run()
    |
    |  @Purpose: This method is the thread run, it consists in maintaining a BT connection.
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void run() {
		
		connect();
		app_alive = true;
		while(app_alive){
			try {
				byteRecu = (int) dataIn.readByte(); 
				
				Thread.sleep(100);
				System.out.println("Recu " + byteRecu);
				
				byteRecu = 0;
				
			}

			catch (IOException ioe) {
				System.out.println("IO Exception readInt");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*---------------------------------------------------------------------
    |  @Method getTransmit()
    |
    |  @Purpose: This method allows to return the last value of the received byte.
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  int : return the last value of the received byte
    -------------------------------------------------------------------*/
	public int getTransmit() {
		return this.byteRecu;
	}
	
	/*---------------------------------------------------------------------
    |  @Method connect()
    |
    |  @Purpose: This method establishes a BT connection
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void connect()
	{  
		System.out.println("En écoute");
		BTConnector ncc = (BTConnector) Bluetooth.getNXTCommConnector();
		BTLink = (BTConnection) ncc.waitForConnection(30, NXTConnection.RAW);
		setDataOut(BTLink.openDataOutputStream());
		dataIn = BTLink.openDataInputStream();
		BTconnect = true;
	}

	/*---------------------------------------------------------------------
    |  @Method getDataOut()
    |
    |  @Purpose: This method makes it possible to recover the outgoing flow.
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  DataOutputStream : dataOut.
    -------------------------------------------------------------------*/
	public static DataOutputStream getDataOut() {
		return dataOut;
	}

	/*---------------------------------------------------------------------
    |  @Method setDataOut(DataOutputStream dataOut)
    |
    |  @Purpose: This method allows to change the outgoing flow.
    |			
    |  @Parameters:
    |      dataOut --  new dataOut
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public static void setDataOut(DataOutputStream dataOut) {
		ListenBT.dataOut = dataOut;
	}
}
