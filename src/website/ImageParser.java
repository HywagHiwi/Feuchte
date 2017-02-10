package website;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

public class ImageParser {

	public LocalDateTime getMetadate(URL givenURL) throws IOException, SAXException, TikaException{
		
		      //detecting the file type
      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      URL url = givenURL;
      
//      FileInputStream inputstream = new FileInputStream(new File(url));
      InputStream inputstream = url.openStream();
      ParseContext pcontext = new ParseContext();
      
      //Jpeg Parse
      JpegParser  JpegParser = new JpegParser();
      JpegParser.parse(inputstream, handler, metadata,pcontext);
//      System.out.println("Contents of the document:" + handler.toString());
//      System.out.println("Metadata of the document:");
      String[] metadataNames = metadata.names();
      
      for(String name : metadataNames) {
    	 if(name.contains("File Modified Date")) {
    		 String[] split = metadata.get(name).split(" ");
    		 switch(split[1]){
    		 	case "Jan":
		 			split[1] = "01";
		 			break;
    		 	case "Feb":
		 			split[1] = "02";
		 			break;
    		 }
    		 String goodDate = new String(split[2] + "." + split[1] + "." + split[5] + " " + split[3]);
    		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
    		 LocalDateTime returnDate;
    		 returnDate = LocalDateTime.parse(goodDate, formatter);
    		 return returnDate;
    	 }
      }
		
		return null;		
	}
	
//	public static void main(String[] args){
//		
//		ImageParser imi = new ImageParser();
//		try {
//			System.out.println(imi.getMetadate("http://134.169.6.25/hywa/deutsch/klimastation/mast.jpg"));
//		} catch (IOException | SAXException | TikaException e) {
//		}
//		try {
//			System.out.println(imi.getMetadate("http://134.169.6.25/hywa/deutsch/klimastation/gelaende.jpg"));
//		} catch (IOException | SAXException | TikaException e) {
//		}
//		
//	}
}