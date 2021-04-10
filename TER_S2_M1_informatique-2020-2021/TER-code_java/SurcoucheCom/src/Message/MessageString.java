package Message;

import java.io.IOException;

import Exception.MessageException;

public class MessageString extends AMessage<String> {

	private String message;
	
	public MessageString(String message) {
		this.message = message;
	}
	
	public MessageString() {
		this.message = null;
	}
	
	@Override
	public void write() throws IOException, MessageException {
		if(dOut != null) {
			dOut.writeUTF(message);
			dOut.flush();
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	@Override
	public String read() throws IOException, MessageException {
		if(dIn != null) {
			return dIn.readUTF();
		}else {
			throw new MessageException("Le flux d'entrée à été mal initialisé");
		}
	}
}
