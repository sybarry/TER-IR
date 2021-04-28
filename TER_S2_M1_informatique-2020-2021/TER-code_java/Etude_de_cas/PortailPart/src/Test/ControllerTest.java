
/**
  * @file ControllerTest.java
  *
  * @brief PortailPart
  * @package Test
  * @author Gicquel, Guérin, Rozen
  * @since 2/01/2021
  * @version 1.0
  * @date 23/04/2021
  *
*/
package Test;


import static org.junit.Assert.*;

import org.junit.Test;


import portailEV3.Controller;
import portailEV3.State;

public class ControllerTest {

	
	/*@Test (expected = NoBrickFound.class)
	public void testInitialisation() {
		
	//Controller initi = new Controller();
		
		ContactSensor doorSensorClosed = new ContactSensor(SensorPort.S3);
		State etat = State.FERME;
		String statutPorte = "FERME";
		
		if (doorSensorClosed.equals(true)) {
			assertEquals(etat, statutPorte);
		} 
		fail("Not yet implemented");
	} */
	
	private Controller brique;

	@Test 
	public void testTotalOpening() {
		setBrique(new Controller());
		State stateDoor = State.FERME;
		State stateTest = State.OUVERT;
		
		Controller.totalOpening();
		assertEquals(stateTest, stateDoor );
		
		State stateDoor1 = State.OUVERT;
		State stateTest1 = State.FERME;
		Controller.totalOpening();
		assertEquals(stateTest1, stateDoor1 );
	}

	public Controller getBrique() {
		return brique;
	}

	public void setBrique(Controller brique) {
		this.brique = brique;
	}

	/*@Test
	public void testPartialOpening() {
		fail("Not yet implemented");
	}

	@Test
	public void testLeftOpening() {
		fail("Not yet implemented");
	}

	@Test
	public void testPartialClosing() {
		fail("Not yet implemented");
	}

	@Test
	public void testTotalClosing() {
		fail("Not yet implemented");
	}*/

}
