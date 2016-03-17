package DanmakuCrawler.DouyuCrawler.Crawler;

import java.net.Socket;

public class KeepaliveThread extends Thread {
	private Socket socket;
	
	public KeepaliveThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try{
			while(true){
				Thread.sleep(45000);
				MessageHandler.send(socket, ResponsePraser.keepLive((int) System.currentTimeMillis()/1000));
				System.out.println("发送了心跳包");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
