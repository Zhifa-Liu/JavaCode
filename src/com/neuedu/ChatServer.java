package com.neuedu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
	
	public static final String EXIT = "exit";
	
	public static final int PORT = 8888;
	
	static Map<String, Socket> nickNameSocketMap = new HashMap<>();

	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(PORT)) {
			System.out.println("聊天室服务器端已启动，正在监听" + PORT + "端口");
			while (true) {
				try {
					Socket socket = ss.accept();
					System.out.println("有新用户连接到服务器端，信息为：" + socket);
					new Thread(new ChatServerRunnable(socket)).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
