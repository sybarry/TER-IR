import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class EV3Car
{
	private EV3LargeRegulatedMotor leftMotor;
    private EV3LargeRegulatedMotor rightMotor;

	private int speedLeftMotor;
	private int speedRightMotor;
	
	//----------------------------------------------constructeur
	
	public EV3Car(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor)
	{
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		speedLeftMotor = 0;
		speedRightMotor = 0;
	}
	
	//----------------------------------------------

	public void up(int value)
	{
		increasedSpeedMotors(value,value);
		updateSpeedMotors();
	}

	public void down(int value)
	{
		if(getSpeedLeftMotor()-value > 0 && getSpeedLeftMotor()-value > 0)
		{
			this.ev3.decreasedSpeedMotors(value, value);
			this.ev3.updateSpeedMotors();
		}
	}

	public void left(int ratio)
	{
		setSpeedRightMotor(this.ev3.getSpeedRightMotor()*ratio);
		updateSpeedMotors();
	}

	public void right(int ratio)
	{
		setSpeedLeftMotor(this.ev3.getSpeedLeftMotor()*ratio);
		updateSpeedMotors();
	}
	
	public void forward()
    {
    	leftMotor.startSynchronization();
    	leftMotor.forward();
        rightMotor.forward();
        leftMotor.endSynchronization();
    }
	
	public void backward()
    {
    	leftMotor.startSynchronization();
    	leftMotor.backward();
        rightMotor.backward();
        leftMotor.endSynchronization();
        
    }
	
	public void stop()
    {	
    	leftMotor.startSynchronization();
    	leftMotor.stop();
        rightMotor.stop();
        leftMotor.endSynchronization();
    }
	
	//----------------------------------------------set
	
	public void setSpeedLeftMotor(int value)
	{
		speedLeftMotor = value;
	}
	
	public void setSpeedRightMotor(int value)
	{
		speedRightMotor = value;
	}
	
	//----------------------------------------------get
	
	public int getSpeedLeftMotor()
	{
		return speedLeftMotor;
	}
	
	public int getSpeedRightMotor()
	{
		return speedRightMotor;
	}
	
	public EV3LargeRegulatedMotor getLeftMotor()
	{
		return leftMotor;
	}
	
	public EV3LargeRegulatedMotor getRightMotor()
	{
		return rightMotor;
	}
	
	private void increasedSpeedMotors(int valueLeftMotor, int valueRightMotor)
	{
		speedLeftMotor+=valueLeftMotor;
		speedRightMotor+=valueRightMotor;
	}
	
	private void decreasedSpeedMotors(int valueLeftMotor, int valueRightMotor)
	{
		speedLeftMotor-=valueLeftMotor;
		speedRightMotor-=valueRightMotor;
	}
	
	private void updateSpeedMotors()
	{
		leftMotor.startSynchronization();
    	leftMotor.setSpeed(speedLeftMotor);
        rightMotor.setSpeed(speedRightMotor);
    	leftMotor.endSynchronization();
	}
}