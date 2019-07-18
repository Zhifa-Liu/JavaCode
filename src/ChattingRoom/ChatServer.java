package ChattingRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
	public static Map<String,Socket> nickNameSocketMap=new HashMap<>();
	
	public static void main(String[] args) {
		
		try (ServerSocket ss=new ServerSocket(30000)){
			System.out.println("Chatting room server start up!");
			while(true) {
				Socket socket=ss.accept();
				System.out.println("A user come in. Details: "+socket);
				ChatServerThread cst=new ChatServerThread(socket);
				cst.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}