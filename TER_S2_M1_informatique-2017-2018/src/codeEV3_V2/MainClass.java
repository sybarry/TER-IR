import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;

public class MainClass 
{
	public static void main(String[] args) 
    {
		EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(BrickFinder.getLocal().getPort("A"));
	    EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(BrickFinder.getLocal().getPort("B"));
	    
    	RegulatedMotor T[] = {rightMotor};
    	leftMotor.synchronizeWith(T);
    	
    	EV3Cars car = new EV3Cars(leftMotor, rightMotor);

    	int button;
		boolean runProgram = true;
    	boolean start = false;
    	while(runProgram)
    	{
    		Button.waitForAnyPress();
    		button = Button.getButtons();
			if(start)
			{
				switch(button)
				{
					case Button.ID_UP :
						
						car.up(50);
						break;
						
					case Button.ID_DOWN : 
						
						car.down(50);  
						break;
						
					case Button.ID_RIGHT : 
						
						car.right(2);
						break;
						
					case Button.ID_LEFT :
						
						car.left(2);
						break;
						
					case Button.ID_ENTER :
					
						start = false;
						car.stop();
						break;
					
					default :
						runProgram = false;
						break;
				}
			}
			else
			{
				switch(button)
				{
					case Button.ID_UP :
					
						start = true;
						car.forward();
						break;
						
					case Button.ID_DOWN : 
					
						start = true;
						car.backward();  
						break;
					
					default :
						runProgram = false;
						break;
				}
			}
    	}
    }
}
