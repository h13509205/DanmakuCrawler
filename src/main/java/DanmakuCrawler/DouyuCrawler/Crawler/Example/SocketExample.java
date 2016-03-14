package DanmakuCrawler.DouyuCrawler.Crawler.Example;

import java.io.IOException;
import java.net.InetAddress;

import DanmakuCrawler.DouyuCrawler.Crawler.SocketClient;
import DanmakuCrawler.DouyuCrawler.Crawler.SocketServer;

public class SocketExample {
	public static void main(String[] args) throws IOException {
        SocketServer server = new SocketServer(5556, new EchoHandler());
        System.out.println("Server starts.");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        SocketClient client = new SocketClient(InetAddress.getLocalHost(), 5556);
        client.println("Hello!");
        System.out.println(client.readLine());
    }
}
