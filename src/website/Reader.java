package website;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

public class Reader {
	
	private Document doc;
	
	public Reader(){
		
	}
	
	public boolean isActual(){
		
	LocalDateTime ldt = LocalDateTime.now();
		
		try {
			doc = Jsoup.connect("http://lwi-klimastation.tu-bs.de/").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
				
		LocalDateTime website_Date = null;
		
		Elements date = doc.select("title");
		for(Element src : date){
			for(Node n : src.childNodes()){
				String[] split = n.toString().split(" ");
				website_Date = workUpDate(new String(split[split.length-3] + " " + split[split.length-1] ));				
			}
		}
		
		if((ldt.minusHours((long) 1.0).isAfter(website_Date))){
			return false;
		} else {
			return true;
		}	
		
		
	}
	
	public int find(String measureValue){
		
		try {
			doc = Jsoup.connect("http://lwi-klimastation.tu-bs.de/").get();
		} catch (IOException e) {
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
	
	private LocalDateTime workUpDate(String date){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
		LocalDateTime returnDate;
		returnDate = LocalDateTime.parse(date,formatter);
		return returnDate;	
	}

	public HashSet<URL> isImageActual(){
		
		HashSet<URL> urls = new HashSet<URL>();
		HashSet<URL> notActualUrls = new HashSet<URL>();
		
		try {
			doc = Jsoup.connect("https://www.tu-braunschweig.de/lwi/hywa/klimastation/webcam").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Elements images = doc.select("div");
		for(Element src : images){
			for(Node n : src.childNodes()){
				if(n.toString().contains("mittlere_spalte")){ //*****************************************************************************
    				for(Node o : n.childNodes()){
    					for(Node r : o.childNodes()){
        					if(r.toString().contains("<img src=\"http://134")){
        						String[] split = r.toString().split("src=\"|\" hight");
        						System.out.println(split.length);
        						for(String s: split){
        							try {
										urls.add(new URL(split[1]));
									} catch (MalformedURLException e) {
										e.printStackTrace();
									}
        						}
        					}
    					}
    				}
    			}	
			}
		}
		
		
		
		ImageParser imageParser = new ImageParser();
		
		LocalDateTime ldt = LocalDateTime.now();
		
		for(URL url : urls){
			try {
				if((ldt.minusHours((long) 1.0).isAfter(imageParser.getMetadate(url)))){
					notActualUrls.add(url);
				}
			} catch (IOException | SAXException | TikaException e) {
				
			}
		}
		
		return notActualUrls;
		
	}
	
	public static void main(String[] args){
		Reader r = new Reader();
		r.isImageActual();
	}
}
