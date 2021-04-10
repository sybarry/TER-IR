package Message;

import java.io.IOException;

import Exception.MessageException;

public class MessageInteger extends AMessage<Integer> {

	private Integer message;
	
	public MessageInteger(int message) {
		this.message = message;
	}
	
	public MessageInteger() {
		this.message = null;
	}
	
	@Override
	public void write() throws IOException, MessageException {
		if(dOut != null) {
			dOut.writeInt(message);
			dOut.flush();
		}else {
			throw new MessageException("Le flux de sortie à été mal initialisé");
		}
	}
	
	@Override
	public Integer read() throws IOException, MessageException {
		if(dIn != null) {
			return dIn.readInt();
		}else {
			throw new MessageException("Le flux d'entrée à été mal initialisé");
		}
	}
}
