package Exception;

/**
 * @author TER2021 : Gicquel, Gu�rin, Rozen
 *
 */

public class OutOfMotorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OutOfMotorException() {
		super("This motor does not exist.");
    }


}
