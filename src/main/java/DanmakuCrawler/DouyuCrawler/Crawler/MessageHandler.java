package DanmakuCrawler.DouyuCrawler.Crawler;

public interface MessageHandler {
	public void onReceive(Connection connection, String message);
}
