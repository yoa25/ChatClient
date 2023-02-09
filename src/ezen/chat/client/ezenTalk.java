package ezen.chat.client;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Scanner;

public class ezenTalk {
	
	public static void setCenterScreen(Frame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int screenWidth = toolkit.getScreenSize().width;
		int screenHeight = toolkit.getScreenSize().height;
		int centerX = (screenWidth - frame.getWidth()) / 2;
		int centerY = (screenHeight - frame.getHeight()) / 2;
		frame.setLocation(centerX, centerY);
	}
	

	public static void main(String[] args) {
		
		Frame frame = new Frame(":::ezen talk:::"); //빈 프레임 생성
		ChatPanel chatPanel = new ChatPanel(); //패널 가져오기
		chatPanel.initLayout();
		chatPanel.eventRegist();
		frame.add(chatPanel, BorderLayout.CENTER);
		frame.setSize(400,550);
		setCenterScreen(frame);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}

}
