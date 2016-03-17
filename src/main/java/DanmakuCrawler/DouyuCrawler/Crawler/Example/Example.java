package DanmakuCrawler.DouyuCrawler.Crawler.Example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class Example {
	public static void main(String[] args) {
		try{
			// 1、构造一个Parser，并设置相关的属性  
            Parser parser = new Parser("http://www.douyutv.com/directory/all");  
            parser.setEncoding("utf-8");  

         // 2.1、自定义一个Filter，用于过滤<Frame >标签，然后取得标签中的src属性值  
            AndFilter filter = new AndFilter(new TagNameFilter("ul"), 
                                 new HasAttributeFilter("id","live-list-contentbox")); 
            
            NodeList list = parser.extractAllNodesThatMatch(filter);
            
            for (int i = 0; i < 1; i++) {       
                Node node = list.elementAt(i);           
                System.out.println(node.toHtml());
                System.out.println();
                System.out.println();
            }
		}catch(Exception e){
			
		}
	}
	
	private static Matcher getMatcher(String content, String regex) {
	     Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
	     return pattern.matcher(content);
	 }
}
