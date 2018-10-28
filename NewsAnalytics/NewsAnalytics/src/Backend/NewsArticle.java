package Backend;
/**
*
newsArticle class handles the links to various news articles and maintains some relevant data about the links
*
**/

/**
*
TODO - Make methods to handle calculation of relevance and parsing of date and title of article
*
**/

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

public class NewsArticle implements Serializable,Comparable<NewsArticle>{

   private final int depth;
   private String url;
   private String title;
   private String publisher;
   private int date;
   private int month;
   private int year;
   private String dateStamp;
   private float relevance = 0;
   public boolean usable = true;

   public NewsArticle(int depth,String url,String title,String dateStamp,String publisher){
       this.depth = depth;
       this.url = url;
       this.title = title;
       this.dateStamp = parseDateStamp(dateStamp);
       this.publisher = publisher;
   }

   public int getDepth(){
       return depth;
   }

   public String getUrl(){
       return url;
   }

   public String getTitle(){
       return title;
   }

   public String getDateStamp(){
       return dateStamp;
   }

   public String getPublisher(){
       return publisher;
   }

   public float getRelevance(){
       return relevance;
   }

   public void setUrl(String url){
       this.url = url;
   }

   public void setTitle(String title){
       this.title = title;
   }

   public void setDateStamp(String dateStamp){
       this.dateStamp = dateStamp;
   }

   public void setPublisher(String publisher){
       this.publisher = publisher;
   }

   public void printArticleDetails(){
       System.out.println("----------");
       System.out.println("Level\t=\t" + this.depth);
       System.out.println("Link\t=\t" + this.url);
       System.out.println("Title\t=\t" + this.title);
       System.out.println("Date\t=\t" + this.dateStamp);
       System.out.println("Publisher\t=\t" + this.publisher);
       System.out.println("----------");
   }

   public String getArticleDetails(){
       return "Level\t=\t" + this.depth + "\n" + "Link\t=\t" + this.url + "\n" + "Title\t=\t" + this.title + "\n" + "Date\t=\t" + this.dateStamp + "\n" + "Publisher\t=\t" + this.publisher;
   }

   public String parseDateStamp(String dateStamp){
       if(dateStamp.equals("Not Available")){
           this.usable = false;
           return "00/00/0000";
       }
       String dateValue = "--/--/----";
       if(dateStamp.toLowerCase().contains("hour") || dateStamp.toLowerCase().contains("minute") || dateStamp.toLowerCase().contains("second")){
           DateFormat dfOut = new SimpleDateFormat("dd/MM/yyyy");
           Date date = new Date();
           dateValue = dfOut.format(date);
           this.date = Integer.parseInt(dateValue.subSequence(0,2).toString());
           this.month = Integer.parseInt(dateValue.subSequence(3,5).toString());
           this.year = Integer.parseInt(dateValue.subSequence(6,10).toString());
       }
       else{
           DateFormat[] dfIn = new DateFormat[3];
           boolean[] dateParsed = new boolean[3];
           DateFormat dfOut = new SimpleDateFormat("dd/MM/yyyy");
           dfIn[0] = new SimpleDateFormat("dd-MMM-yyyy");
           dfIn[1] = new SimpleDateFormat("dd-MM-yyyy");
           dfIn[2] = new SimpleDateFormat("dd.MM.yyyy");
           for(int i=0;i<3;i++){
               try{
                   Date date = dfIn[i].parse(dateStamp);
                   dateValue = dfOut.format(date);
                   this.date = Integer.parseInt(dateValue.subSequence(0,2).toString());
                   this.month = Integer.parseInt(dateValue.subSequence(3,5).toString());
                   this.year = Integer.parseInt(dateValue.subSequence(6,10).toString());
                   dateParsed[i] = true;
                   break;
               }catch(ParseException px){
//                   System.out.println(dateStamp + " could not be parsed");
//                   px.printStackTrace();
            	   dateParsed[i] = false;
                   continue;
               }
           }
           if(!(dateParsed[0]||dateParsed[1]||dateParsed[1])){
        	   System.out.println(dateStamp + " could not be parsed");
           }
       }
       if(dateStamp.equals("--/--/----")){
           this.usable = false;
           return "00/00/0000";
       }
       return dateValue;
   }

   public int compareTo(NewsArticle n){
       if(this.year > n.year){
           return 1;
       }
       else if(this.year < n.year){
           return -1;
       }
       else{
           if(this.month > n.month){
               return 1;
           }
           else if(this.month < n.month){
               return -1;
           }
           else{
               if(this.date > n.date){
                   return 1;
               }
               else if(this.date < n.date){
                   return -1;
               }
               else{
                   return 0;
               }
           }
       }
   }

   public boolean equals(NewsArticle n){
       if(n == null) return false;
       if(this.url.equals(n.getUrl())){
           return true;
       }
       return false;
   }

   public String getContent(boolean replace){
       String content = null;
       try{
           if(replace) setUrl(url.replace("http","https"));
           content = ArticleExtractor.INSTANCE.getText(new URL(url));
       }catch (BoilerpipeProcessingException bpex){
           System.out.println("Error during content extraction of URL - " + url);
       }catch (MalformedURLException mx){
           System.out.println("Malformed URL - " + url);
       }
       return content;
   }

   public LinkedList<pair<String,String>> getLinks(){
       LinkedList<pair<String,String>> links = new LinkedList<>();
       Random randomUserAgentIndex = new Random();
       String userAgent = main.userAgentsList.get(randomUserAgentIndex.nextInt(main.userAgentsList.size()));
       try{
           Document webpage = Jsoup.parse(Jsoup.connect(url).timeout(0).userAgent(userAgent).execute().parse().outerHtml());
           Elements hyperlinks = webpage.select("a");
           for(Element hyperlink : hyperlinks){
               String href = hyperlink.attr("href");
               String title = hyperlink.text();
               if(!href.contains("http://") && !href.contains("https://") && !href.contains("www.")) href = url + href;
               links.add(new pair(href,title));
           }
       }catch(Exception ex){
           System.out.println("Error while getting next level links from URL - " + url);
           ex.printStackTrace();
       }
       return links;
   }

}