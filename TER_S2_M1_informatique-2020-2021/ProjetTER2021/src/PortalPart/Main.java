/**
 * 
 */
package PortalPart;


/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */


public class Main {

	public static void main(String[] args) throws Exception {

		cPortal oPortal = new cPortal();
		
		
		while(true)
		{
			while (!oPortal.get_oTouchSensor().isTouched()) {}
			if(oPortal.activationBT())
			{
				if(oPortal.autoriserAcces())
				{
					oPortal.openPoral();
				}
			}
			while (!oPortal.get_oTouchSensor().isTouched()) {}
			oPortal.closePortal();
		}
	}
}
