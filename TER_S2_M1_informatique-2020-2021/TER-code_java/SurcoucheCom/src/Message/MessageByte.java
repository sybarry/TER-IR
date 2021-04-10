package Message;
import java.io.IOException;

import Exception.MessageException;

public class MessageByte extends AMessage<Byte> {

	private Byte message;
	
	public MessageByte(byte message) {
		this.message = message;
	}
	
	public MessageByte() {
		this.message = null;
	}
	
	@Override
	public void write() throws IOException, MessageException {
		if(dOut != null) {
			dOut.writeByte(message);
			dOut.flush();
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	@Override
	public Byte read() throws IOException, MessageException {
		if(dIn != null) {
			return dIn.readByte();
		}else {
			throw new MessageException("Le flux d'entrée à été mal initialisé");
		}
	}
}
