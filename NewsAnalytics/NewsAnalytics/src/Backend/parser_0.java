package Backend;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import UI.Constants;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class parser_0 implements Runnable {

    public QueryDetails currentQuery;
    public String queryDirectoryPath;
    public String queryDirectoryLevel_0_Path;
    public int numberOfResults;
    public boolean isPaused;
    public GoogleConfig gc;

    public parser_0(QueryDetails query,String queryDirectoryPath){
        this.currentQuery = query;
        this.queryDirectoryPath = queryDirectoryPath;
        this.queryDirectoryLevel_0_Path = queryDirectoryPath + File.separator + "Level_0";
        File queryDirectoryLevel_0 = new File(queryDirectoryLevel_0_Path);
        if(!queryDirectoryLevel_0.exists()){
            queryDirectoryLevel_0.mkdir();
        }
        isPaused = false;
        File gcFile =new File(Constants.getData()+File.separator+"googleConfig.ser");
        gc = (GoogleConfig) main.deserialize(gcFile.getAbsolutePath());
    }

    public void run(){
        currentQuery.printQueryDetails();
        File queryDetailsFile = new File(queryDirectoryPath + File.separator + currentQuery.query.toLowerCase() + ".nq");
        String logDetails;
        while(currentQuery.currentLevel == 0){
            numberOfResults = currentQuery.totalLinksParsed;
            checkForPause(1000);
            LinkedList<NewsArticle> newsArticles = new LinkedList<NewsArticle>();
			try {
				newsArticles = filterCurrentPageLinks(googleNewsURL());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logDetails = "Parsed Google Page " + Integer.toString(currentQuery.googlePagesParsed + 1) + " -- Articles Found = " + newsArticles.size() + "\n";
            main.writeToTXT(queryDirectoryLevel_0_Path + File.separator + "log.txt",logDetails,true);
            System.out.print(logDetails);
            if(newsArticles.size() == 0){
            	System.out.println("----------");
            	System.out.println("Search Complete for Level 0");
                currentQuery.currentLevel = 1;
                break;
            }
            Iterator<NewsArticle> articleIterator = newsArticles.iterator();
            for(int i=0;i<newsArticles.size();i++){
                NewsArticle article = articleIterator.next();
                if(!validDate(article)){
                	System.out.println("DATE OUT OF RANGE: "+ article.getDateStamp());
                	continue;
                }
                checkForPause(1000);
                if((currentQuery.allLinksParsed.size()!=0)&&(currentQuery.allLinksParsed.contains(article.getUrl()) || currentQuery.allLinksParsed.contains(article.getUrl().replace("http","https")))) continue;
                File articleDirectory = new File(queryDirectoryLevel_0_Path + File.separator + Integer.toString(numberOfResults + 1));
                main.deleteDir(articleDirectory.getPath());
                articleDirectory.mkdir();
                main.serialize(article,articleDirectory.getPath() + File.separator + "article.ser");
                main.serialize(article.getLinks(),articleDirectory.getPath() + File.separator + "links.ser");
                String articleContent = article.getContent(false);
                if(articleContent == null) articleContent = article.getContent(true);
                main.writeToTXT(articleDirectory.getPath() + File.separator + "Contents.txt",articleContent,false);
                main.writeToTXT(articleDirectory.getPath() + File.separator + "Details.txt",article.getArticleDetails(),false);
                logDetails = "Article " + Integer.toString(numberOfResults + 1) + " -- " + article.getUrl() + " -- Indexed on " + main.getCurrentDateTimeStamp() + "\n";
                if(articleContent == null){
                    logDetails = "ERROR parsing Article " + Integer.toString(numberOfResults + 1) + " -- " + article.getUrl() + " -- Indexed on " + main.getCurrentDateTimeStamp() + "\n";
                }
                main.writeToTXT(queryDirectoryLevel_0_Path + File.separator + "log.txt",logDetails,true);
                System.out.print(logDetails);
                article.printArticleDetails();
                numberOfResults++;
                currentQuery.totalLinksParsed++;
                currentQuery.allLinksParsed.add(article.getUrl());
                queryDetailsFile.delete();
                main.serialize(currentQuery,queryDetailsFile.getPath());
            }
            logDetails = "Finished downloading Articles from Google Page " + Integer.toString(currentQuery.googlePagesParsed + 1) + "\n";
            main.writeToTXT(queryDirectoryLevel_0_Path + File.separator + "log.txt",logDetails,true);
            System.out.print(logDetails);
            currentQuery.googlePagesParsed++;
            queryDetailsFile.delete();
            main.serialize(currentQuery,queryDetailsFile.getPath());
        }
    }

    public String googleNewsURL() throws ParseException{
    	DateFormat dfIn = new SimpleDateFormat("dd/MM/yyyy");
    	DateFormat dfOut = new SimpleDateFormat(gc.getDateFormat());
    	Date sdate = dfIn.parse(this.currentQuery.getStartDate());
        String startDate = dfOut.format(sdate);
        Date edate = dfIn.parse(this.currentQuery.getEndDate());
        String endDate = dfOut.format(edate);
        return "https://www.google.com/search?tbm=nws&tbs=" + "cdr:1,cd_min:" + startDate + ",cd_max:" + endDate + "&q=" + this.currentQuery.query + "&start=" + this.currentQuery.googlePagesParsed + "0";
    }

    public LinkedList<NewsArticle> filterCurrentPageLinks(String googleNewsURL){
        System.out.println("Parsing Google Page - " + googleNewsURL);
        LinkedList<NewsArticle> newsArticles = new LinkedList<>();
        Random randomUserAgentIndex = new Random();
        String userAgent = main.userAgentsList.get(randomUserAgentIndex.nextInt(main.userAgentsList.size()));
        try{
            Document webpage = Jsoup.parse(Jsoup.connect(googleNewsURL).timeout(0).userAgent(userAgent).execute().parse().outerHtml());
            Elements hyperlinks = webpage.select("a");
            for(Element hyperlink : hyperlinks){
                if(hyperlink.attr("class").equals(gc.getUrlTag1()) || hyperlink.attr("class").equals(gc.getUrlTag2())){
                    String href = hyperlink.attr("href");
                    String title = hyperlink.text();
                    Element parentDivTag = hyperlink.attr("class").equals(gc.getUrlTag2()) ? hyperlink.parent() : hyperlink.parent().parent();
                    Elements infoSpanTags = parentDivTag.getElementsByTag("span");
                    Element publisherSpanTag = null;
                    Element dateSpanTag = null;
                    for(Element spanTag : infoSpanTags){
                        if(spanTag.attr("class").equals(gc.getPublisherTag1()) || spanTag.attr("class").equals(gc.getPublisherTag2())){
                            publisherSpanTag = spanTag;
                        }
                        if(spanTag.attr("class").equals(gc.getDateTag1()) || spanTag.attr("class").equals(gc.getDateTag2())){
                            dateSpanTag = spanTag;
                        }
                    }
                    String publisher = publisherSpanTag != null ? publisherSpanTag.text() : "Not Available";
                    String dateStamp = dateSpanTag != null ? dateSpanTag.text() : "Not Available";
                    newsArticles.add(new NewsArticle(0,href,title,dateStamp,publisher));
                }
            }
        }catch(IOException iex){
            System.out.println("Unable to parse Google News Page with URL - " + googleNewsURL);
        }
        return newsArticles;
    }

    public void checkForPause(int millis){
        while(this.isPaused){
            try{
                Thread.sleep(millis);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public LinkedList<NewsArticle> compileResults(LinkedList<String> keywords, boolean onlyHasAll){

        LinkedList<NewsArticle> results = new LinkedList<>();
        boolean searchKeywords = keywords.size() == 0 ? false : true;
        File resultsDir = new File(queryDirectoryLevel_0_Path);
        if(!resultsDir.isDirectory()) return results;
        for(int i=1;i<resultsDir.list().length-1;i++){
            NewsArticle article = (NewsArticle) main.deserialize(resultsDir.getPath() + File.separator + Integer.toString(i) + File.separator + "article.ser");
            if(article == null){
                System.out.println("INVALID Article at - " + resultsDir.getPath() + File.separator + Integer.toString(i) + File.separator + "article.ser");
                continue;
            }
            if(!article.usable){
                System.out.println("NON-USABLE Article at - " + resultsDir.getPath() + File.separator + Integer.toString(i) + File.separator + "article.ser");
                continue;
            }
            if(searchKeywords){
            	String content = main.readFromTXT(resultsDir.getAbsolutePath()+File.separator+Integer.toString(i)+File.separator+"Contents.txt");
            	if(content.contains(resultsDir.getAbsolutePath()+File.separator+Integer.toString(i)+File.separator+"Contents.txt")){
            		System.out.println(content);
            		continue;
            	}else{
            		if(onlyHasAll){
            			if(main.hasAllWords(content, keywords)){
            				results.add(article);
            			}
            		}else{
            			if(main.checkForWords(content, keywords).size()!=0){
            				results.add(article);	
            			}
            		}
            	}
            }else{
            	results.add(article);
            }
            
        }
        return results;
    }

    public String getContent(NewsArticle article){
    	File resultsDir = new File(currentQuery.getLocation()+File.separator+"Level_0");
        for(int i=1;i<=resultsDir.list().length;i++){
            File articleDir = new File(currentQuery.getLocation()+File.separator+"Level_0"+File.separator+(i+1));
            NewsArticle currentArticle = (NewsArticle) main.deserialize(articleDir+File.separator+"article.ser");                
            if(currentArticle.equals(article)){
            	try{
            		String content = main.readFromTXT(articleDir+File.separator+"Contents.txt");
                    return content;
                }catch(Exception ex){
                	System.out.println("Error while retrieving content from Contents.txt in folder at " +articleDir);
                	ex.printStackTrace();
                }
            }
        }
        return "No content found";
    }

    public void pause(){
    	this.isPaused=true;
    }
    
    public void resume(){
    	this.isPaused=false;
    }

    public boolean validDate(NewsArticle a){
    	String startDate =currentQuery.getStartDate();
    	String endDate = currentQuery.getEndDate();
    	try{
	    	int startYear = Integer.parseInt(startDate.substring(6));
		    int endYear = Integer.parseInt(endDate.substring(6));
		    int startMonth = Integer.parseInt(startDate.subSequence(3,5).toString());
	        int endMonth = Integer.parseInt(endDate.subSequence(3,5).toString());
	        int startDay = Integer.parseInt(startDate.subSequence(0,2).toString());
	        int endDay = Integer.parseInt(endDate.subSequence(0,2).toString());
	        int date=Integer.parseInt(a.getDateStamp().substring(0,2));
		    int month=Integer.parseInt(a.getDateStamp().substring(3,5));
		    int year = Integer.parseInt(a.getDateStamp().substring(6));
		    if((year>endYear)||(year<startYear)){
		    	return false;
		    }
		    if(year == endYear){
		    	if(month>endMonth){
		    		return false;
		    	}
		    	else if(month==endMonth){
		    		if(date > endDay){
		    			return false;
		    		}
		    	}
		    }
		    if(year == startYear){
		    	if(month < startMonth){
		    		return false;
		    	}else if(month == startMonth){
		    		if(date < startDay){
		    			return false;
		    		}
		    	}
		    	
		    }
    	}catch(NumberFormatException e){
    		System.out.println("Invalid date: "+ a.getDateStamp());
    		e.printStackTrace();
    		return false;
    	}
	    
	    return true;
    }
}
