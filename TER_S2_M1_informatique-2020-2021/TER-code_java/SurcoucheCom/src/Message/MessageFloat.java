package Message;

import java.io.IOException;

import Exception.MessageException;

public class MessageFloat extends AMessage<Float> {

	private Float message;
	
	public MessageFloat(float message) {
		this.message = message;
	}
	
	public MessageFloat() {
		this.message = null;
	}
	
	@Override
	public void write() throws IOException, MessageException {
		if(dOut != null) {
			dOut.writeFloat(message);
			dOut.flush();
		}else {
			throw new MessageException("Le flux de sortie � �t� mal initialis�");
		}
	}
	
	@Override
	public Float read() throws IOException, MessageException {
		if(dIn != null) {
			return dIn.readFloat();
		}else {
			throw new MessageException("Le flux d'entr�e � �t� mal initialis�");
		}
	}
}
