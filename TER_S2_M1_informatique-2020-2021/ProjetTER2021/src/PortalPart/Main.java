/**
 * 
 */
package PortalPart;

/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */
import lejos.hardware.Button;

public class Main {

	public static void main(String[] args) throws Exception {

		cPortal oPortal = new cPortal();
		boolean isOpen = false;
		
		while(true)
		{
			while (!oPortal.get_oTouchSensor().isTouched()) {}
			if(isOpen)
			{
				oPortal.closePortal();
				isOpen = false;
			}
			else
			{
				oPortal.openPoral();
				isOpen = true;
			}
			
		}
		
	}

}
