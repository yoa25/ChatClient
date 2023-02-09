package ezen.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * TCP/IP 기반의 자바 클라이언트
 * @author 김민영
 * @Date   2023. 2. 6.
 */
public class ChatClient {
	
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 7777;
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private ChatPanel chatPanel;
	public ChatClient (ChatPanel chatPanel) {
		this.chatPanel = chatPanel;
	}


	// 서버 연결
	public void connectServer() throws IOException {
		socket = new Socket(SERVER_IP, SERVER_PORT);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
	}
	
	// 서버 연결 종료
	public void disConnectServer() throws IOException {
		socket.close();
	}
	
	// 메시지 전송
	public void sendMessage(String message) throws IOException {
		out.writeUTF(message);
		out.flush();
	}
	
	// 메시지 수신
	public void receiveMessage() {
		//서버로부터 전송되는 메시지를 실시간 수신하기 위해 스레드 생성 및 시작
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						String serverMessage = in.readUTF();
						//"CONNECT!방그리"
						//"CHAT_MESSAGE!방그리!내용!"
						String[] tokens = serverMessage.split("＃");
						String messageType = tokens[0];
						String nickName = tokens[1];
						switch (messageType) {
							case MessageType.CONNECT:
								chatPanel.appendMessage("["+ nickName +"] 님이 대화에 참여하셨습니다.");
								break;
							case "USER_LIST":
								String userList = tokens[2];
								chatPanel.setList(userList);
								break;
							case MessageType.CHAT_MESSAGE:
								String chatMessage = tokens[2];
								chatPanel.appendMessage(nickName + " : " + chatMessage);
								System.out.println("받았어요");
								break;
							case MessageType.DIS_CONNECT:
								chatPanel.appendMessage("["+ nickName +"] 님이 퇴장하셨습니다.");
								userList = tokens[2];
								chatPanel.setList(userList);
								break;
						}
					}
				} catch (IOException e) {} 
				finally {
					//System.out.println("[서버]와 연결 종료함...");
				}
			}
		};
		thread.start();
	}

	
}








