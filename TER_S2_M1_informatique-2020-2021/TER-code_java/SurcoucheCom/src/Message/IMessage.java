package Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Exception.MessageException;

public interface IMessage<T> {
	
	DataOutputStream getOutput();
	
	DataInputStream getInput();
	
	void setOutput(DataOutputStream newOutput);
	
	void setInput(DataInputStream newInput);
	
	void write() throws IOException, MessageException;
	
	T read() throws IOException, MessageException;
}
