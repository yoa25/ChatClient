package ezen.chat.client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.JOptionPane;

public class ChatPanel extends Panel{
	
	Panel connectPanel, inputPanel;
	Label nickNameLabel;
	TextField nickNameTF, inputTF;
	Button connectButton, sendButton;
	TextArea messageTA;
	JList<String> userList;
	//전화기 역할
	ChatClient chatClient;
	String nickName;
	
	public ChatPanel() {
		connectPanel = new Panel();
		inputPanel = new Panel();
		nickNameLabel = new Label("대화명 : ");
		nickNameTF = new TextField("사용하고자 하는 대화명 입력");
		inputTF = new TextField();
		connectButton = new Button("연결");
		sendButton = new Button("보내기");
		messageTA = new TextArea(10,20);
		
		
//		String[] list  = {"초코", "딸기", "바닐라"};
		userList = new JList<String>();
		userList.setPreferredSize(new Dimension(80, 200));
	}
	
	
	//컴포넌트 배치
	public void initLayout() {
		//레이아웃 담당 매니저 설정
		setLayout(new BorderLayout());
		
		connectPanel.setLayout(new BorderLayout());
		connectPanel.add(nickNameLabel, BorderLayout.WEST);
		connectPanel.add(nickNameTF, BorderLayout.CENTER);
		connectPanel.add(connectButton, BorderLayout.EAST);
		
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(inputTF, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);
		
		add(connectPanel, BorderLayout.NORTH);
		add(messageTA, BorderLayout.CENTER);
		add(userList, BorderLayout.EAST);
		add(inputPanel, BorderLayout.SOUTH);
	}
	
	//컴포넌트 이벤트 처리 
	public void eventRegist() {

		nickNameTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				nickNameTF.setText("");
			}
		});
		
		nickNameTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(connectButton.getLabel().equals("연결")) {
					connect();					
				}else {
					disConnect();
					
				}
			}
		});
		
		//텍스트 필드 이벤트 처리
		inputTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		
		//보내기 버튼 액션 이벤트 처리
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		
		
		userList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userIndex = Integer.toString(userList.getSelectedIndex());
					sendMessage();
			}
		});
		
	}
	
	// 서버 연결
	public void connect() {
		//유효성 검증
		nickName = nickNameTF.getText();
		chatClient = new ChatClient(ChatPanel.this);
		//서버 연결
		try {
			chatClient.connectServer();
			//채팅 메시지 수신
			chatClient.receiveMessage();
			//대화명 비활성화
			nickNameTF.setEnabled(false);
			nickNameTF.setEditable(false);
			
			//서버에 대화 참여 메세지 수신
//			chatClient.sendMessage("☆☆ ["+nickName+"] ☆☆님이 채팅에 참여하셨습니다.");
			// 사용자 정의 프로토콜 설계
			// "메시지 종류 ! 대화명" 
			// "CONNECT!방그리"
			chatClient.sendMessage(MessageType.CONNECT + MessageType.delimeter + nickName);
			connectButton.setLabel("나가기");
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "관리자에게 문의하여주세요","서버 연결에 실패", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	//서버 연결 끊기
	public void disConnect() {
		try {
			chatClient.sendMessage("DIS_CONNECT#" + nickName);
			chatClient.disConnectServer();
			nickNameTF.setEnabled(true);
			nickNameTF.setEditable(true);
			connectButton.setLabel("연결");
			appendMessage("서버와 연결을 종료하였습니다.");
		} catch (IOException e) {
		}
	}
	
	//메시지 전송 처리
	public void sendMessage() {
		String inputmessage = inputTF.getText();
	//데이터 유효성 검증
		if(Validator.isEmpty(inputmessage)) {
			return;
		}
		inputTF.setText("");
	//서버 전송
		try {
			chatClient.sendMessage(MessageType.CHAT_MESSAGE+MessageType.delimeter+nickName+MessageType.delimeter+ inputmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//서버로 수신한 메세지 출력 
	 public void appendMessage(String message) {
		 messageTA.append(message + "\n");
	 }
	 
	 //서버로부터 수신한 사용자 목록을 List에 추가
	 public void setList(String nickNameList) {
		 String[] list = nickNameList.split(",");
		 userList.setListData(list);
	 }
	
}
