package Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Exception.MessageException;

public abstract class AMessage<T> implements IMessage<T> {

	protected DataOutputStream dOut;
	protected DataInputStream dIn;
	
	public AMessage() {
		this.dOut = null;
		this.dIn = null;
	}
	
	public DataOutputStream getOutput() {
		return this.dOut;
	}
	
	public DataInputStream getInput() {
		return this.dIn;
	}
	
	public void setOutput(DataOutputStream newOutput) {
		this.dOut = newOutput;
	}
	
	public void setInput(DataInputStream newInput) {
		this.dIn = newInput;
	}
	
	public abstract void write() throws IOException, MessageException;
	
	public abstract T read() throws IOException, MessageException;
}
