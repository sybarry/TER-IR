/**
 * 
 */
package PortalPart;

import java.util.Collection;

import javax.jws.WebParam.Mode;

import lejos.hardware.BrickFinder;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.RemoteBTDevice;
import lejos.hardware.ev3.EV3;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;

/**
 * @author TER2021 : Gicquel, Guérin, Rozen
 *
 */


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
				if(cPortal.connectCLIENT())
				{
					oPortal.openPoral();
					isOpen = true;
				}
				
			}
			
		}		
	}

}
