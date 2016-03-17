package DanmakuCrawler.DouyuCrawler.Crawler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

public class CrawlerClient extends Thread {
	Server danmakuServer;
	Server loginServer;
    private String roomUrl;
    private String pageHtml;
    private int room_id = -1;
    private int group_id = -1;
    private boolean isOnline;
    private Socket loginSocket;
    private Socket danmakuSocket;
    
    public CrawlerClient(String roomUrl) {
    	this.roomUrl = roomUrl;
    	danmakuServer = new Server("danmu.douyutv.com", 8602);
    }
    
    @Override
    public void run() {
    	super.run();
    	try{
    		pageHtml = HttpUtil.get(roomUrl);
    		room_id = ResponsePraser.getRoomId(pageHtml);
    		loginServer = ResponsePraser.getLoginServer(pageHtml);
    		isOnline = ResponsePraser.isOnline(pageHtml);
    		loginSocket = new Socket(loginServer.host, loginServer.port);
    		System.out.println(loginServer.host+":"+loginServer.port);
    		Reader reader = new InputStreamReader(loginSocket.getInputStream());
    		MessageHandler.send(loginSocket, ResponsePraser.loginContent(room_id));
    		char chars[] = new char[10240];		
    		int len;   	   
    	    len=reader.read(chars);
    	    String s1 = new String(chars, 0, len);  
    	    System.out.println("from server: " + s1); 
    	    
    	    len=reader.read(chars);
    	    String s2 = new String(chars, 0, len);  
    	    System.out.println("from server: " + s2); 
    	    
    	    String username = ResponsePraser.getUsername(s1);
    	    int gid = ResponsePraser.getGid(s2);
    	    System.out.println(username + ": " +gid);
    		
    	    //MessageHandler.send(loginSocket, ResponsePraser.keepLive((int)System.currentTimeMillis()/1000));
    	    //loginSocket.setKeepAlive(true);
    	    Thread keepalive1 = new KeepaliveThread(loginSocket);
    	    keepalive1.start();
    	    
    	    
    	    Thread keepalive2 = new KeepaliveThread(danmakuSocket);
    	    keepalive2.start();
    	    danmakuSocket = new Socket(danmakuServer.host, danmakuServer.port);
    	    MessageHandler.send(danmakuSocket, ResponsePraser.danmakuLogin(username, room_id));
    	    reader = new InputStreamReader(danmakuSocket.getInputStream());
    	    len=reader.read(chars);
    	    String s3 = new String(chars, 0, len);  
    	    System.out.println("from server: " + s3);
    	    
    	    MessageHandler.send(danmakuSocket, ResponsePraser.joinGroup(room_id, gid));
    	    //danmakuSocket.setKeepAlive(true);
    	    
    	    
    	    while(true) {
    	    	len=reader.read(chars);
    	    	if(len >0) {
    	    		String s4 = new String(chars, 0, len);  
    	    		System.out.println(s4);
    	    		Danmaku a = ResponsePraser.getDanmaku(s4);
    	    		if(a != null) {
    	    			System.out.println(a.toString());
    	    		}
    	    	}
    	    }
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally{
    		
    	}
    }
    
    public static void main(String[] args) {
		CrawlerClient a = new CrawlerClient("/bage");
		a.start();
	}
}
