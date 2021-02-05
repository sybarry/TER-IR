/**
 * 
 */
package VehiclePart;


/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		cVehicle oVehicle = new cVehicle();
		boolean isConnect = false;
		
		while(true)
		{
			while (!oVehicle.get_oTouchSensor().isTouched()) {}
			if(isConnect)
			{
				oVehicle.vehiculeForward();
			}
			else
			{
				if(cVehicle.connectSERVEUR())
				{
					isConnect = true;
				}
				
			}
			
		}

	}

}
