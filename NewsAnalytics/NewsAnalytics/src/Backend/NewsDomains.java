package Backend;
/**
 *

  newsDomains class handles the list of all the websites which have to be included while filtering for news articles
 *
 **/

/**
 *
  TODO - Make modifications to addDomainsFromWeb method to add other popular news websites
 *
 **/

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import UI.Constants;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class NewsDomains{

    public static LinkedList<newsDomain> newsDomainsList = new LinkedList<>();

    public static void addDomain(newsDomain nwd){
        nwd.enableFilter();
        if(!newsDomainsList.contains(nwd)){
            newsDomainsList.add(nwd);
        }
    }

    public static void addDomainsFromWeb() throws Exception{
        proxy.enableProxy("proxy22.iitd.ernet.in","3128","proxy22.iitd.ernet.in","3128");
        int counter = 0;
        for(int i=0;i<4;i++){
            Document webpage = Jsoup.parse(Jsoup.connect("http://www.alexa.com/topsites/category;" + i + "/News").userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0").ignoreHttpErrors(true).timeout(0).get().outerHtml());
            Elements links = webpage.select("a");
            for(Element link : links){
                String href = link.attr("href");
                String text = link.text();
                if(href.subSequence(0,href.length() < 10 ? href.length() : 10).toString().equals("/siteinfo/") && href.length() > 10){
                    System.out.println(++counter + " -- " + text);
                    addDomain(new newsDomain(stripDomain(text)));
                }
            }
        }
    }

    public static String stripDomain(String text){
        if(text.subSequence(0,4).toString().equals("http") || text.subSequence(0,4).toString().equals("Http")){
            text = text.subSequence(text.indexOf("/")+2,text.length()).toString();
        }
        if(text.subSequence(0,3).toString().equals("www") || text.subSequence(0,3).toString().equals("Www")){
            text = text.subSequence(4,text.length()).toString();
        }
        if(text.charAt(text.length()-1) == '/'){
            text = text.subSequence(0,text.length()-1).toString();
        }
        if(text.subSequence(text.length()-4,text.length()).toString().equals(".com")){
            text = text.subSequence(0,text.length()-4).toString();
        }
        return text;
    }

    public static void listAllEnabledDomains(){
        Iterator<newsDomain> domainIterator = newsDomainsList.iterator();
        for(int i=1;i<newsDomainsList.size()+1;i++){
            newsDomain current = domainIterator.next();
            if(current.filterEnabled){
                System.out.println(i + " -- " + current.getURL());
            }
            else{
                System.out.println(i + " -- " + current.getURL() + " -- Disabled");
            }
        }
    }

    public static void serializeList(){
        FileOutputStream FOut = null;
        ObjectOutputStream OOut = null;
        try{
            FOut = new FileOutputStream(Constants.getData()+ File.separator + "newsDomainsList.ser");
            OOut = new ObjectOutputStream(FOut);
            OOut.writeObject(newsDomainsList);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(FOut != null){
                try{
                    FOut.close();
                }catch(IOException ioex){
                    ioex.printStackTrace();
                }
            }
        }
    }

    public static LinkedList<newsDomain> deserializeList(){
        LinkedList<newsDomain> domainsList = new LinkedList<>();
        try {
            FileInputStream FIn = new FileInputStream(Constants.getData()+ File.separator + "newsDomainsList.ser");
            ObjectInputStream OIn = new ObjectInputStream(FIn);
            domainsList = (LinkedList<newsDomain>)OIn.readObject();
            OIn.close();
            FIn.close();
            return domainsList;
        }catch(IOException ix) {
            ix.printStackTrace();
            return null;
        }catch(ClassNotFoundException cx) {
            System.out.println("LinkedList<newsDomain> -- Class not found");
            cx.printStackTrace();
            return null;
        }
    }

}

class newsDomain implements Serializable{

    private final String url;
    public Boolean filterEnabled = true;

    public newsDomain(String link){
        url = link;
    }

    public String getURL(){
        return url;
    }

    public void disableFilter(){
        filterEnabled = false;
    }

    public void enableFilter(){
        filterEnabled = true;
    }

    public int hash(){
        return url.hashCode();
    }

}
