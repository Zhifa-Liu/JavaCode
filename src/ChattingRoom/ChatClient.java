package ChattingRoom;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	public static void main(String[] args) {
		Socket s=null;
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		DataOutputStream dos=null;
		try {
			try{
				s=new Socket("127.0.0.1",30000); 
				dos=new DataOutputStream(s.getOutputStream());
				
				ChatClientThread cct=new ChatClientThread(s);
				cct.start();
				
				String input=null;
				while(true) {
					input=sc.nextLine();
					
					if(input.equals("Exit")) {
						break;
					}
					
					try {
						dos.writeUTF(input);
						dos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				cct.stopCaht();
			}catch(IOException e) {
				e.getMessage();
			}
		}finally {
			try {
				s.close();
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
