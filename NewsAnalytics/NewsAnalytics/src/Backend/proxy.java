package Backend;
/**
*
proxy Class handles the proxy settings of the system
*
**/

/**
*
TODO - Handle SecurityExceptions (check System.setProperty doc)
*
**/

public class proxy{

   private static String proxyNameHTTP = null;
   private static String proxyPortHTTP = null;
   private static String proxyNameHTTPS = null;
   private static String proxyPortHTTPS = null;

   public static void enableProxy(String nameHTTP,String portHTTP,String nameHTTPS,String portHTTPS){
       setProxyNameHTTP(nameHTTP);
       setProxyPortHTTP(portHTTP);
       setProxyNameHTTPS(nameHTTPS);
       setProxyPortHTTPS(portHTTPS);
   }

   public static void disableProxy(){
       setProxyNameHTTP("");
       setProxyPortHTTP("");
       setProxyNameHTTPS("");
       setProxyPortHTTPS("");
   }

   private static void setProxyNameHTTP(String name){
       proxyNameHTTP = name;
       System.setProperty("http.proxyHost", proxyNameHTTP);
   }

   private static void setProxyPortHTTP(String port){
       proxyPortHTTP = port;
       System.setProperty("http.proxyPort", proxyPortHTTP);
   }

   private static void setProxyNameHTTPS(String name){
       proxyNameHTTPS = name;
       System.setProperty("https.proxyHost", proxyNameHTTPS);
   }

   private static void setProxyPortHTTPS(String port){
       proxyPortHTTPS = port;
       System.setProperty("https.proxyPort", proxyPortHTTPS);
   }

   public static String getProxyNameHTTP(){
       return proxyNameHTTP;
   }

   public static String getProxyNameHTTPS(){
       return proxyNameHTTPS;
   }

   public static String getProxyPortHTTP(){
       return proxyPortHTTP;
   }

   public static String getProxyPortHTTPS(){
       return proxyPortHTTPS;
   }
}