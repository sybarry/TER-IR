package Message;
import java.io.IOException;

import Exception.MessageException;

public class MessageBoolean extends AMessage<Boolean> {

	private Boolean message;
	
	public MessageBoolean(boolean message) {
		this.message = message;
	}
	
	public MessageBoolean() {
		this.message = null;
	}
	
	@Override
	public void write() throws IOException, MessageException {
		if(dOut != null) {
			dOut.writeBoolean(message);
			dOut.flush();
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	@Override
	public Boolean read() throws IOException, MessageException {
		if(dIn != null) {
			return dIn.readBoolean();
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
}
