package ChattingRoom;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClientThread extends Thread{
	private Socket socket;
	DataInputStream dis=null;
	private boolean out=false;
	
	public ChatClientThread(Socket s) throws IOException {
		this.socket=s;
		dis=new DataInputStream(socket.getInputStream());
	}
	
	public void run() {
		while(!out) {
			try {
				System.out.println(dis.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Out");
	}
	
	public void stopCaht() {
		out=true;
	}
	
}
