package UI;

import java.io.Serializable;

public class ProxyConfig implements Serializable {
	  
	private static final long serialVersionUID = 4452386446414814404L;
	
	private String proxyNameHTTP = null;
	private String proxyPortHTTP = null;
	private String proxyNameHTTPS = null;
	private String proxyPortHTTPS = null;
	   
	public ProxyConfig(){
		   
	}

	public ProxyConfig(String proxyNameHTTP, String proxyPortHTTP, String proxyNameHTTPS, String proxyPortHTTPS) {
		super();
		this.proxyNameHTTP = proxyNameHTTP;
		this.proxyPortHTTP = proxyPortHTTP;
		this.proxyNameHTTPS = proxyNameHTTPS;
		this.proxyPortHTTPS = proxyPortHTTPS;
	}

	public String getProxyNameHTTP() {
		return proxyNameHTTP;
	}

	public void setProxyNameHTTP(String proxyNameHTTP) {
		this.proxyNameHTTP = proxyNameHTTP;
	}

	public String getProxyPortHTTP() {
		return proxyPortHTTP;
	}

	public void setProxyPortHTTP(String proxyPortHTTP) {
		this.proxyPortHTTP = proxyPortHTTP;
	}

	public String getProxyNameHTTPS() {
		return proxyNameHTTPS;
	}

	public void setProxyNameHTTPS(String proxyNameHTTPS) {
		this.proxyNameHTTPS = proxyNameHTTPS;
	}

	public String getProxyPortHTTPS() {
		return proxyPortHTTPS;
	}

	public void setProxyPortHTTPS(String proxyPortHTTPS) {
		this.proxyPortHTTPS = proxyPortHTTPS;
	}
	   
}
