package DanmakuCrawler.DouyuCrawler.Crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageHandler {
	/**
     * 发送消息
     */
    public static void send(Socket socket, String content) throws IOException {
        if (socket == null || !socket.isConnected()) return;

        Message message = new Message(content);
        OutputStream out = socket.getOutputStream();
        out.write(message.getBytes());
        //System.out.println(message.toString());
        out.flush();
        //out.close();
    }
}
