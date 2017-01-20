package website;

import java.io.IOException;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Reader {
	
	private Document doc;
	
	public Reader(){
		
	}
	
	public int find(String measureValue){
		
		try {
			doc = Jsoup.connect("http://lwi-klimastation.tu-bs.de/").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		Elements taupunkt = doc.select("div");
	
		 for (Element src : taupunkt) {
	        for(Node n : src.childNodes()){
	            for(Node m : n.childNodes()){
	            	for(Node o : m.childNodes()){
	            		if(o.toString().contains(measureValue)){
	            			if(o.toString().contains("<p class=\"wert\">")){ //*****************************************************************************
	            				String[] a = o.toString().split("\"wert\">");
	            				String[] b = a[1].split(",");
	            				//System.out.println(b[0]);
	            				return Integer.parseInt(b[0]);
	            			}
	            		}
	            	}
	            }
	         }
	     }
		 return 0;
	}
	
	public static void main(String[] args){
		Reader r = new Reader();
		r.find("rel. Luftfeuchte");
	}

}
