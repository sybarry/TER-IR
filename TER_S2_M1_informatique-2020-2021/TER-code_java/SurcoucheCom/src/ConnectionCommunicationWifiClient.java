import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ConnectionCommunicationWifiClient implements IConnectionCommunication {

	private Socket client;
	private DataOutputStream dOut;
	private DataInputStream dIn;
	private int port;
	private String ip;
	
	public ConnectionCommunicationWifiClient(int port, String ip) {
		this.port = port;
		this.ip = ip;
		this.client = null;
		this.dOut = null;
		this.dIn = null;
	}
	
	@Override
	public void openConnection() throws IOException {
		client = new Socket(ip, port); 
		System.out.println("Connected");
		
		InputStream in = client.getInputStream();
		OutputStream out = client.getOutputStream();
		
		dIn = new DataInputStream(in);
		dOut = new DataOutputStream(out);
	}

	@Override
	public void closeConnection() throws IOException {
		client.close();
		/*dOut.close();
		dIn.close();*/
		System.out.println("Connection closed");
	}

	@Override
	public void sendMessage(Object message, int mode) throws IOException{
		switch(mode) {
		case 1:
			dOut.writeUTF((String) message); 
			dOut.flush();
			break;
		case 2:
			dOut.writeInt((int) message); 
			dOut.flush();
			break;
		case 3:
			dOut.writeBoolean((boolean) message); 
			dOut.flush();
			break;
		case 4:
			dOut.writeFloat((float) message); 
			dOut.flush();
			break;
		case 5:
			dOut.writeDouble((double) message); 
			dOut.flush();
			break;
		case 6:
			dOut.writeShort((short) message); 
			dOut.flush();
			break;
		case 7:
			dOut.writeByte((Byte) message); 
			dOut.flush();
			break;
		case 8:
			dOut.writeChar((char) message); 
			dOut.flush();
			break;
		case 9:
			dOut.writeLong((long) message); 
			dOut.flush();
			break;
		default:
			break;
			
		}
				
	}
	
	@Override
	public boolean sendMessageWithACK(Object message, int mode) throws IOException, InterruptedException{
		boolean result = false;
		switch(mode) {
		case 1:
			dOut.writeUTF((String) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 2:
			dOut.writeInt((int) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 3:
			dOut.writeBoolean((boolean) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 4:
			dOut.writeFloat((float) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 5:
			dOut.writeDouble((double) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 6:
			dOut.writeShort((short) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 7:
			dOut.writeByte((Byte) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 8:
			dOut.writeChar((char) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		case 9:
			dOut.writeLong((long) message); 
			dOut.flush();
			TimeUnit.SECONDS.sleep(1);
			if(dIn.readByte() == 1) {
				result = true;
			}
			return result;
		default:
			return result;
			
		}
				
	}

	@Override
	public Object receiveMessage(int mode) throws IOException{
		switch(mode) {
		case 1:
			return dIn.readUTF();	
		case 2:
			return dIn.readInt();	
		case 3:
			return dIn.readBoolean();	
		case 4:
			return dIn.readFloat();	
		case 5:
			return dIn.readDouble();	
		case 6:
			return dIn.readShort();	
		case 7:
			return dIn.readByte();	
		case 8:
			return dIn.readChar();	
		case 9:
			return dIn.readLong();	
		default:
			System.out.println("Error mode");
			return new IOException();
			
		}
	}
	
	@Override
	public Object receiveMessageWithACK(int mode) throws IOException{
		Object result = null;
		switch(mode) {
		case 1:
			result = dIn.readUTF();
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 2:
			result = dIn.readInt();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 3:
			result = dIn.readBoolean();
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 4:
			result = dIn.readFloat();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 5:
			result = dIn.readDouble();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 6:
			result = dIn.readShort();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 7:
			result = dIn.readByte();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 8:
			result = dIn.readChar();	
			dOut.writeByte(1);
			dOut.flush();
			break;
		case 9:
			result = dIn.readLong();
			dOut.writeByte(1);
			dOut.flush();
			break;
		default:
			break;
			
		}
		return result;
	}
}
