package DanmakuCrawler.DouyuCrawler.Crawler;

import java.util.Date;

public class Danmaku {
	private int uid;//用户id
    private String snick;//昵称
    private String content;//内容
    private Date date;//发布时间
    private int rid;//房间号

    public Danmaku(int uid, String snick, String content, int rid) {
        this.uid = uid;
        this.snick = snick;
        this.content = content;
        this.date = new Date();
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "Danmaku{" +
                "uid=" + uid +
                ", snick='" + snick + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", rid=" + rid +
                '}';
    }
}
