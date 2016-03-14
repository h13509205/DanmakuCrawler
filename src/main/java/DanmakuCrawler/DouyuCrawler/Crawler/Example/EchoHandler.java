package DanmakuCrawler.DouyuCrawler.Crawler.Example;

import DanmakuCrawler.DouyuCrawler.Crawler.Connection;
import DanmakuCrawler.DouyuCrawler.Crawler.MessageHandler;

public class EchoHandler implements MessageHandler {
	public void onReceive(Connection connection, String message) {
        connection.println(message);
    }
}
