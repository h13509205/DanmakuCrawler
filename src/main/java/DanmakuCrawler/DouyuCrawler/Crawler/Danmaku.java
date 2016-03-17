package DanmakuCrawler.DouyuCrawler.Crawler;

import java.util.Date;

public class Danmaku {
	private int uid;//用户id
    private String nickname;//昵称
    private String content;//内容
    private long curr;//发布时间
    private String cid;//弹幕唯一编号
    private int rid;//房间号

    public Danmaku(int rid, int uid, String nn, String txt, String cid) {
        this.uid = uid;
        this.nickname = nn;
        this.content = txt;
        this.curr = System.currentTimeMillis();
        this.rid = rid;
        this.cid = cid;
    }

    public void tostring() {
    	System.out.println("rid:"+rid);
    	System.out.println("uid:"+uid);
    	System.out.println("nickname:"+nickname);
    	System.out.println("content:"+content);
    	System.out.println("curr:"+curr);
    	System.out.println("cid:"+cid);
    	System.out.println();
    }
}
