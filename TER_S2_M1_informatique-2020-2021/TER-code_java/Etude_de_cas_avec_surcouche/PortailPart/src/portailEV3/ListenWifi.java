/**
  * @file ListenWifi.java
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
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ListenWifi extends Thread {
	
	//Attributs
	static String idVehicle = "";
	private String ip;
	private Socket sock;
	private InputStream in;
	private DataInputStream dIn;
	
	
	/*---------------------------------------------------------------------
    |  @Method run()
    |
    |  @Purpose: This method is the thread run, it consists in maintaining a Wifi connection.
    |			
    |
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void run() {
		try {
			connect();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      	for(;;){
      		try {
      			idVehicle = dIn.readUTF();
      		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
      		}	
      	}
	}
	
	/*---------------------------------------------------------------------
    |  @Method returnIdVehicle()
    |
    |  @Purpose: This method returns the ID of the connected vehicle.
    |			
    |  @Parameters: None.
    |
    |  @Returns:  String : Id of the connected vehicle.
    -------------------------------------------------------------------*/
	public String returnIdVehicle() {
		return idVehicle;
	}
	
	/*---------------------------------------------------------------------
    |  @Method setIdVehicle(String id)
    |
    |  @Purpose: This method allows to change the outgoing flow.
    |			
    |  @Parameters:
    |      id --  new id vehicle
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void setIdVehicle(String id) {
		idVehicle = id;
	}
	
	/*---------------------------------------------------------------------
    |  @Method connect()
    |
    |  @Purpose: This method establishes a Wifi connection
    |			
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void connect() throws UnknownHostException, IOException {
		ip = "192.168.1.22"; //ip de la voiture qui veut rentrer
		sock = new Socket(ip, 1234);//1234 port du vehicule qui veut rentré
		System.out.println("Connected");
		
		in = sock.getInputStream();
		dIn = new DataInputStream(in);
	}
	
	/*---------------------------------------------------------------------
    |  @Method disconnect()
    |
    |  @Purpose: This method destablishes a Wifi connection
    |			
    |  @Parameters: None.
    |
    |  @Returns:  None.
    -------------------------------------------------------------------*/
	public void disconnect() throws IOException{
		sock.close();
	}
}
