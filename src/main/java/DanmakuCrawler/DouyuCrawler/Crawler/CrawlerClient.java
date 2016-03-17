package DanmakuCrawler.DouyuCrawler.Crawler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;

public class CrawlerClient extends Thread {
	Server danmakuServer;
	Server loginServer;
    private String roomUrl;
    private String pageHtml;
    private String username;
    private int room_id = -1;
    private int group_id = -1;
    private boolean isOnline;
    private Socket loginSocket;
    private Socket danmakuSocket;
    
    public CrawlerClient(String roomUrl) throws Exception {
    	this.roomUrl = roomUrl;
    	danmakuServer = new Server("danmu.douyutv.com", 8602);
    	danmakuSocket = new Socket(danmakuServer.host, danmakuServer.port);
    }
    
    @Override
    public void run() {
    	super.run();
    	try{
    		loginDanmakuServer();
    		
    		KeepaliveThread keepalive = new KeepaliveThread(loginSocket);
    	    keepalive.start();
    	    
    	    getDammaku();
    	    keepalive.flag = false;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally{
    		
    	}
    }
    
    private void loginDanmakuServer() {
    	try{
    		pageHtml = HttpUtil.get(roomUrl);
    		room_id = ResponsePraser.getRoomId(pageHtml);
    		loginServer = ResponsePraser.getLoginServer(pageHtml);
    		isOnline = ResponsePraser.isOnline(pageHtml);
    		loginSocket = new Socket(loginServer.host, loginServer.port);
    		Reader reader = new InputStreamReader(loginSocket.getInputStream());
    		MessageHandler.send(loginSocket, ResponsePraser.loginContent(room_id));
    		char chars[] = new char[1024];		
    		int len;   	   
    	    len=reader.read(chars);
    	    String s1 = new String(chars, 0, len);  
    	    len=reader.read(chars);
    	    String s2 = new String(chars, 0, len);  
    	    
    	    username = ResponsePraser.getUsername(s1);
    	    group_id = ResponsePraser.getGid(s2);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void getDammaku() {
    	try{
    		MessageHandler.send(danmakuSocket, ResponsePraser.danmakuLogin(username, room_id));		   
    	    MessageHandler.send(danmakuSocket, ResponsePraser.joinGroup(room_id, group_id));
    		char[] chars = new char[1024];
    		Reader reader = new InputStreamReader(danmakuSocket.getInputStream());
    		while(true) {
    	    	int len=reader.read(chars);
    	    	if(len >0) {
    	    		String s4 = new String(chars, 0, len);  
    	    		if(ResponsePraser.isError(s4)){
    	    			break;
    	    		}
    	    		Danmaku a = ResponsePraser.getDanmaku(s4);
    	    		if(a != null) {
    	    			System.out.println(a.toString());
    	    		}
    	    	}
    	    }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	 
    }
    
    public static void main(String[] args) throws Exception {
		CrawlerClient a = new CrawlerClient("/yifuleiya");
		a.start();
	}
}
