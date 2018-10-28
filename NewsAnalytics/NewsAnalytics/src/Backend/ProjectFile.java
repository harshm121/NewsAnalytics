package Backend;
import java.io.Serializable;
import java.util.LinkedList;

public class ProjectFile implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2732554232771578079L;
	
	public String projectTitle;
    public String creationDate;
    public String lastModified;
    public LinkedList<String> queryPaths;
    public LinkedList<String> importedQueryPaths;
    public String lastActiveQueryPath;
    public LinkedList<QueryDetails> queries;
    public String location;

//    public ProjectFile(String projectTitle,String creationDate){
//        this.projectTitle = projectTitle;
//        this.creationDate = creationDate;
//        this.lastModified = creationDate;
//        this.lastActiveQueryPath = "";
//        this.queryPaths = new LinkedList<>();
//        this.importedQueryPaths = new LinkedList<>();
//    }
    
    public ProjectFile(String projectTitle,String creationDate,String location){
        this.projectTitle = projectTitle;
        this.creationDate = creationDate;
        this.lastModified = creationDate;
        this.lastActiveQueryPath = "";
        this.location = location;
        this.queryPaths = new LinkedList<>();
        this.importedQueryPaths = new LinkedList<>();
        this.queries = new LinkedList<QueryDetails>();
    }

    public static String[] parseQueryPath(String queryPath){
        String[] output = queryPath.split("_");
        return output;
    }

    public void printAllQueryPaths(){
        for(String path : queryPaths){
            System.out.println(path);
        }
    }
    public void addQuery(QueryDetails q){
    	queries.add(q);
    }

    public void printAllImportedQueryPaths(){
        for(String path : importedQueryPaths){
            System.out.println(path);
        }
    }

    
    public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public LinkedList<String> getQueryPaths() {
		return queryPaths;
	}

	public void setQueryPaths(LinkedList<String> queryPaths) {
		this.queryPaths = queryPaths;
	}

	public LinkedList<String> getImportedQueryPaths() {
		return importedQueryPaths;
	}

	public void setImportedQueryPaths(LinkedList<String> importedQueryPaths) {
		this.importedQueryPaths = importedQueryPaths;
	}

	public String getLastActiveQueryPath() {
		return lastActiveQueryPath;
	}

	public void setLastActiveQueryPath(String lastActiveQueryPath) {
		this.lastActiveQueryPath = lastActiveQueryPath;
	}

	public LinkedList<QueryDetails> getQueries() {
		return queries;
	}

	public void setQueries(LinkedList<QueryDetails> queries) {
		this.queries = queries;
	}
	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void printProjectFile(){
        System.out.println("----------");
        System.out.println("Title\t=\t" + projectTitle);
        System.out.println("Created on\t=\t" + creationDate);
        System.out.println("Last modified on\t=\t" + lastModified);
        System.out.println("Last Active QueryPath\t=\t" + lastActiveQueryPath);
        System.out.println("----------");
    }

}