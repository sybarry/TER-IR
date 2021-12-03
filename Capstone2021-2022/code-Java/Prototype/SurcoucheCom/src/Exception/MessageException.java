package Exception;

/*
 * @author ROZEN Anthony - GICQUEL Alexandre - GUERIN Antoine
 */

public class MessageException extends Exception {

	private static final long serialVersionUID = 1L;

	/*
	 * Returns the error to the system
	 * 
	 * @param errorMessage The error message has returned
	 */
	public MessageException(String errorMessage) {
		super(errorMessage);
	}
}
