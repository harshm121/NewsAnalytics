package Backend;

import java.io.Serializable;

public class GoogleConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2297325583541824798L;
	private String urlTag1;
	private String urlTag2;
	private String publisherTag1;
	private String publisherTag2;
	private String dateTag1;
	private String dateTag2;
	private String dateFormat;
	public GoogleConfig(){
		
	}
	public GoogleConfig(String urlTag1, String urlTag2, String publisherTag1, String publisherTag2, String dateTag1,
			String dateTag2, String dateFormat) {
		super();
		this.urlTag1 = urlTag1;
		this.urlTag2 = urlTag2;
		this.publisherTag1 = publisherTag1;
		this.publisherTag2 = publisherTag2;
		this.dateTag1 = dateTag1;
		this.dateTag2 = dateTag2;
		this.dateFormat = dateFormat;
	}
	public String getUrlTag1() {
		return urlTag1;
	}
	public void setUrlTag1(String urlTag1) {
		this.urlTag1 = urlTag1;
	}
	public String getUrlTag2() {
		return urlTag2;
	}
	public void setUrlTag2(String urlTag2) {
		this.urlTag2 = urlTag2;
	}
	public String getPublisherTag1() {
		return publisherTag1;
	}
	public void setPublisherTag1(String publisherTag1) {
		this.publisherTag1 = publisherTag1;
	}
	public String getPublisherTag2() {
		return publisherTag2;
	}
	public void setPublisherTag2(String publisherTag2) {
		this.publisherTag2 = publisherTag2;
	}
	public String getDateTag1() {
		return dateTag1;
	}
	public void setDateTag1(String dateTag1) {
		this.dateTag1 = dateTag1;
	}
	public String getDateTag2() {
		return dateTag2;
	}
	public void setDateTag2(String dateTag2) {
		this.dateTag2 = dateTag2;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
		
}
