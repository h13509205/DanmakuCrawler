package DanmakuCrawler.DouyuCrawler.Crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

public class ResponsePraser {
	/*
	 * 
	 *  type 表示消息的类型登陆消息为loginreq
	 * 	username&password 不需要，请求登陆以后系统会自动的返回对应的游客账号。
	 * 	ct 不清楚什么意思，默认为0并无影响
	 * 	password 不需要
	 * 	roomid 房间的id
	 * 	devid 为设备标识，无所谓，所以我们使用随机的UUID生成
	 * 	rt 时间戳（以秒记）
	 * 	vk 为时间戳+"7oE9nPEG9xXV69phU31FYCLUagKeYtsF"+devid的字符串拼接结果的MD5值（这个是参考了一篇文章，关于这一处我也不大明白怎么探究出来的）
	 * 	ver 默认。
	 */
	 public static String loginContent(int roomId) {
		 //String devid = "DF9E4515E0EE766B39F8D8A2E928BB7C";
		 String devid = UUID.randomUUID().toString().replaceAll("-", "").toString().toUpperCase();
		 String rt = String.valueOf(System.currentTimeMillis()/1000);
		 String vk = MD5Util.MD5(rt+"7oE9nPEG9xXV69phU31FYCLUagKeYtsF"+devid);
	     return String.format("type@=loginreq/username@=/ct@=0/password@=/roomid@=%d/devid@=%s/rt@=%s/vk@=%s/ver@=20150929/", roomId, devid, rt, vk);
	 }

	 public static String danmakuLogin(String username, int roomId) {
	     return String.format("type@=loginreq/username@=%s/password@=1234567890123456/roomid@=%d/", username, roomId);
	 }

	 public static String joinGroup(int rid, int gid) {
	     return String.format("type@=joingroup/rid@=%d/gid@=%d/", rid, gid);
	 }

	 public static String keepLive(long tick) {
	     return String.format("type@=keeplive/tick@=%d/", tick);
	 }
	 
	 private static final String REGEX_ROOM_ID = "\"room_id\":(\\d*),";
	 private static final String REGEX_ROOM_STATUS = "\"show_status\":(\\d*),";
	 private static final String REGEX_SERVER = "%7B%22ip%22%3A%22(.*?)%22%2C%22port%22%3A%22(.*?)%22%7D%2C";
	 private static final String REGEX_GROUP_ID = "type@=setmsggroup.*/rid@=(\\d*?)/gid@=(\\d*?)/";
	 private static final String REGEX_DANMAKU_SERVER = "/ip@=(.*?)/port@=(\\d*?)/";
	 private static final String REGEX_CHAT_DANMAKU = "type@=chatmessage/.*/sender@=(\\d.*?)/content@=(.*?)/snick@=(.*?)/.*/rid@=(\\d*?)/";
	 private static final String REGEX_USERNAME = "/username@=(.*?)/nickname";
	 private static final String REGEX_ERROR = "type@=error/";
	 
	 private static Matcher getMatcher(String content, String regex) {
	     Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
	     return pattern.matcher(content);
	 }
	 
	 public static boolean isError(String content) {
		 Matcher matcher = getMatcher(content, REGEX_ERROR);
		 return matcher.find();
	 }
	 
	 /**
	  * 从tcp返回内容得到username
	  */
	 public static String getUsername(String content) {
	     String username = "";
	     if (content == null) return username;
	     Matcher matcher = getMatcher(content, REGEX_USERNAME);
	     if (matcher.find()) {
	         username = matcher.group(1);
	     }
	     return username;
	 }
	 
	 /**
	  * 从tcp返回内容得到gid
	  */
	 public static int getGid(String content) {
	     int gid = -1;
	     if (content == null) return gid;
	     Matcher matcher = getMatcher(content, REGEX_GROUP_ID);
	     if (matcher.find()) {
	         gid = Integer.parseInt(matcher.group(2));
	     }
	     return gid;
	 }
	 
	 /**
	  * 从房间页面解析出roomId
	  */
	 public static int getRoomId(String content) {
	     int rid = -1;
	     if (content == null) return rid;
	     Matcher matcher = getMatcher(content, REGEX_ROOM_ID);
	     if (matcher.find()) {
	         rid = Integer.parseInt(matcher.group(1));
	     }
	     return rid;
	 }
	 
	 /**
	  * 解析当前直播状态
	  * @return 若room_status == 1 则正在直播
	  */
	 public static boolean isOnline(String content) {
	     if (content == null) return false;
	     Matcher matcher = getMatcher(content, REGEX_ROOM_STATUS);
	     return matcher.find() && "1".equals(matcher.group(1));
	 }
	 
	 /**
	  * 在主播的房间的html文件中解析得到
	  * 解析出第一个服务器地址
	  */
	 public static Server getLoginServer(String content) {
	     if (content == null) return null;
	     Matcher matcher = getMatcher(content, REGEX_SERVER);
	     Server loginServer = null;
         if (matcher.find()) {
             loginServer = new Server(matcher.group(1), Integer.parseInt(matcher.group(2)));
         }
         return loginServer;
	 }
	 
//	 /**
//	  * 解析弹幕服务器地址
//	  */
//	 public static Server getDanmakuServer(String content) {
//	     if (content == null) return null;
//	     Matcher matcher = getMatcher(content, REGEX_DANMAKU_SERVER);
//	     Server danmakuServer = null;
//	     if (matcher.find()) {
//	         danmakuServer = new Server(matcher.group(1), Integer.parseInt(matcher.group(2)));
//	     }
//	     return danmakuServer;
//	 }
	 
	 /**
	  * 在弹幕服务器发过来的tcp报文中可以解析得到
	  * 解析弹幕信息
	  */
	 public static Danmaku getDanmaku(String response) {
	     if (response == null) return null;
	     Matcher matcher = getMatcher(response, REGEX_CHAT_DANMAKU);
	     Danmaku danmaku = null;
	     if (matcher.find()) {
	     	danmaku = new Danmaku(Integer.parseInt(matcher.group(1)),
	     	matcher.group(3),
	    	matcher.group(2),
	    	Integer.parseInt(matcher.group(4)));
	     }
	     return danmaku;
	 }
	 
	 public static void main(String[] args) {
		 String devid = "DF9E4515E0EE766B39F8D8A2E928BB7C";
		 String rt = "1453795822";
		 System.out.println(DigestUtils.md5Hex(rt+"7oE9nPEG9xXV69phU31FYCLUagKeYtsF"+devid));
		
	}
}
