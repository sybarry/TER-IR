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
		
		while(true)
		{
			while (!oVehicle.get_oTouchSensor().isTouched()) {}
			if(oVehicle.activationBT("EV3"))
			{
				if(oVehicle.demandeAcces())
				{
					oVehicle.vehiculeForward();
				};
			}
			else
			{

			}
		}
	}


}
