package ChattingRoom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class ChatServerThread extends Thread {
	private Socket s;
	DataInputStream dis=null;
	DataOutputStream dos=null;
	Scanner sc=new Scanner(System.in);
	String currentUserNickname;
	
	public ChatServerThread(Socket s) throws IOException {
		this.s=s;
		dis=new DataInputStream(s.getInputStream());
		dos=new DataOutputStream(s.getOutputStream());
	}
	
	public void run() {
		try {
			dos.writeUTF("Welcome to our chatting room.\nPlease enter your nickname:");
			dos.flush();
			
			while(true) {
				currentUserNickname=dis.readUTF();
				if(isExist(currentUserNickname)) {
					dos.writeUTF("The nickname is using by somebody in the chatting room.Please enter another one");
					dos.flush();
				}else {
					ChatServer.nickNameSocketMap.put(currentUserNickname,s);
					System.out.println(currentUserNickname+" is online now");
					dos.writeUTF("You join chatting room successfully");
					break;
				}
			}
			
			dos.writeUTF("Enter 'to all message' to send message to everyone\n"+
					   	 "Enter 'to nickname' to send message to user who own this nickname\n"+
					     "Enter 'User List' to dispaly users now online\n"+
					     "Enter 'Exit' to exit chatting room");
			dos.flush();
			
			String enter=dis.readUTF();
			String[] strs=enter.split(" ");
			if(enter.startsWith("to ")) {
				if(strs[1].equals("all")) {
					System.out.println("Send message to all");
					String message=strs[2];
					sendAll(message);
				}else {
					System.out.println("Send message to someone");
					String name=strs[1];
					String message=strs[2];
					sendToOne(name,message);
				}
				
			}else if(enter.startsWith("User List")) {
				display();
			}else {
				dos.writeUTF("Invaid input. Enter again");
				dos.flush();
			}
			
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	public boolean isExist(String nick) {
		if(ChatServer.nickNameSocketMap.containsKey(nick)) {
			return true;
		}else {
			return false;
		}
	}
	
	public void sendAll(String message) {
		try {
			if(ChatServer.nickNameSocketMap!=null) {
				Iterator<String> it=ChatServer.nickNameSocketMap.keySet().iterator();
				String nicks=it.next();
				while(nicks!=null&&!nicks.equals(currentUserNickname)) {
					Socket socketL=ChatServer.nickNameSocketMap.get(it.next());
					DataOutputStream dos=new DataOutputStream(socketL.getOutputStream());
					dos.writeUTF(currentUserNickname+" says to all:"+message+"\nSend time:"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendToOne(String name,String message) {
		Socket recieverSocket=ChatServer.nickNameSocketMap.get(name);
		if(recieverSocket!=null){
			try {
				DataOutputStream dos=new DataOutputStream(recieverSocket.getOutputStream());
				dos.writeUTF(currentUserNickname+" says to you:"+message+"\nSend time:"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				dos.writeUTF(name+"is not here or is outline");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void display() {
		Iterator<String> it=ChatServer.nickNameSocketMap.keySet().iterator();
		String list="";
		while(it.hasNext()) {
			list=list+"["+it.next()+"]\n";
		}
		try {
			dos.writeUTF(list);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
