package Backend;
import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

public class QueryDetails implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 9122254221580827793L;
	
//	public String projectTitle;
    public String query;
    public String startDate;
    public String endDate;
    public int currentLevel = 0;
    public int googlePagesParsed = 0;
    public int totalLinksParsed = 0;
    public LinkedList<String> allLinksParsed;
    public boolean imported = false;
    public ProjectFile parentProject;
    public String location;

//    public QueryDetails(String projectTitle,String query,String startDate,String endDate){
//        this.projectTitle = projectTitle;
//        this.query = query;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.allLinksParsed = new LinkedList<>();
//    }
    public QueryDetails(ProjectFile project, String query, String startDate, String endDate){
    	this.parentProject = project;
    	this.query=query;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.location = project.getLocation()+ File.separator+project.getProjectTitle()+File.separator+"queries"+File.separator +query.toLowerCase() + "_" + main.convertDate(startDate) + "_" + main.convertDate(endDate);
    	File queryDirectory = new File(location);
    	if(!queryDirectory.exists()){
    		queryDirectory.mkdir();
    	}
    	this.allLinksParsed = new LinkedList<>();
    }

    public String queryDirectoryName(){
        return this.query.toLowerCase() + "_" + main.convertDate(startDate) + "_" + main.convertDate(endDate);
    }

    public boolean isImported(){
        return imported;
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

//    public String getProjectTitle() {
//		return projectTitle;
//	}
//
//	public void setProjectTitle(String projectTitle) {
//		this.projectTitle = projectTitle;
//	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getGooglePagesParsed() {
		return googlePagesParsed;
	}

	public void setGooglePagesParsed(int googlePagesParsed) {
		this.googlePagesParsed = googlePagesParsed;
	}

	public int getTotalLinksParsed() {
		return totalLinksParsed;
	}

	public void setTotalLinksParsed(int totalLinksParsed) {
		this.totalLinksParsed = totalLinksParsed;
	}

	public LinkedList<String> getAllLinksParsed() {
		return allLinksParsed;
	}

	public void setAllLinksParsed(LinkedList<String> allLinksParsed) {
		this.allLinksParsed = allLinksParsed;
	}

	public ProjectFile getParentProject() {
		return parentProject;
	}

	public void setParentProject(ProjectFile parentProject) {
		this.parentProject = parentProject;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void setImported(boolean imported) {
		this.imported = imported;
	}

	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void printQueryDetails(){
        System.out.println("----------");
        System.out.println("Project Title\t=\t" + parentProject.getProjectTitle());
        System.out.println("Query\t=\t" + query);
        System.out.println("Start Date\t=\t" + startDate);
        System.out.println("End Date\t=\t" + endDate);
        System.out.println("Imported\t=\t" + imported);
        System.out.println("Current Level\t=\t" + currentLevel);
        System.out.println("Google Pages Parsed\t=\t" + googlePagesParsed);
        System.out.println("Total Links Parsed\t=\t" + totalLinksParsed);
        System.out.println("----------");
    }

}