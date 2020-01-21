import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.motor.NXTRegulatedMotor;


public class EV3Car 
{
	static NXTRegulatedMotor leftMotor = Motor.B;
    static NXTRegulatedMotor rightMotor = Motor.C;
    
    static void forward()
    {
    	RegulatedMotor T[] = {rightMotor};
    	leftMotor.synchronizeWith(T);
    	
    	
    	Button.waitForAnyPress();
    	
    	leftMotor.startSynchronization();
    	leftMotor.forward();
        rightMotor.forward();
        leftMotor.endSynchronization();
        
        Button.waitForAnyPress();
    	
        leftMotor.startSynchronization();
    	leftMotor.stop();
        rightMotor.stop();
    	leftMotor.endSynchronization();
    }
    
    static void backward()
    {
    	RegulatedMotor T[] = {rightMotor};
    	leftMotor.synchronizeWith(T);
    	
    	Button.waitForAnyPress();
    	
    	leftMotor.startSynchronization();
    	leftMotor.backward();
        rightMotor.backward();
        leftMotor.endSynchronization();
        
        Button.waitForAnyPress();
    	
        leftMotor.startSynchronization();
    	leftMotor.stop();
        rightMotor.stop();
    	leftMotor.endSynchronization();
    }
    
    public static void main(String[] args) 
    {
    	RegulatedMotor T[] = {rightMotor};
    	int button;
    	boolean start = true;
    	while(start)
    	{
    		Button.waitForAnyPress();
    		button = Button.getButtons();
    		switch(button)
    		{
    			case Button.ID_UP :
					
    				leftMotor.setSpeed(400);
    		    	rightMotor.setSpeed(400);
    		    	
			    	leftMotor.synchronizeWith(T);
			    	
			    	leftMotor.startSynchronization();
			    	leftMotor.forward();
			        rightMotor.forward();
			        leftMotor.endSynchronization();	  
			        
    				Button.waitForAnyEvent();
    				
    				leftMotor.startSynchronization();
			    	leftMotor.stop();
			        rightMotor.stop();
			    	leftMotor.endSynchronization();
    				break;
    				
    			case Button.ID_DOWN : 
    				
    				leftMotor.setSpeed(400);
    		    	rightMotor.setSpeed(400);
    		    	
			    	leftMotor.synchronizeWith(T);
			    	
			    	leftMotor.startSynchronization();
			    	leftMotor.backward();
			        rightMotor.backward();
			        leftMotor.endSynchronization();	  
			        
    				Button.waitForAnyEvent();
    				
    				leftMotor.startSynchronization();
			    	leftMotor.stop();
			        rightMotor.stop();
			    	leftMotor.endSynchronization();
    				break;
    				
    			case Button.ID_RIGHT : 
    				rightMotor.setSpeed(200);
    				rightMotor.forward();
    				Button.waitForAnyEvent();
    				rightMotor.stop();
    				break;
    				
    			case Button.ID_LEFT :
    		    	leftMotor.setSpeed(200);
    				leftMotor.forward();
    				Button.waitForAnyEvent();
    				leftMotor.stop();
    				break;
    				
    			default :
    				start = false;
    				break;
    		}
    	}
    }
}
