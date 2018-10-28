package Backend;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;

import UI.Constants;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class main {

//    public static boolean pause = false;
    public static ProjectFile projFile;
    public static boolean newProject = false;
    public static String projectTitle;
    public static String projectDirectoryPath;
    public static String workingDirectoryPath;
    public static String importedDirectoryPath;
    public static QueryDetails currentQuery = null;
    public static LinkedList<String> userAgentsList;
    public static boolean success = true;

    public main(String projTitle,boolean newProj, String projectDirPath) {
        addUserAgents();
        newProject = newProj;
        this.projectTitle = projTitle;
        this.projectDirectoryPath = projectDirPath + File.separator + projectTitle;
        File projectDirectory = new File(projectDirectoryPath);
        if(!projectDirectory.exists()){
        	success = projectDirectory.mkdir() && success;
            newProject = true; 
        }
        projectTitle = projTitle;
        workingDirectoryPath = projectDirectoryPath + File.separator + "queries";
        importedDirectoryPath = projectDirectoryPath + File.separator + "imported";
        File workingDirectory = new File(workingDirectoryPath);
        String ser_projectFilePath = projectDirectoryPath + File.separator + projectTitle + ".nproj";
        File ser_projFile = new File(ser_projectFilePath);
        String currentDate = new SimpleDateFormat("dd/mm/yyyy").format(new Date());
        if(!newProject){
            projFile = null;
            if(ser_projFile.exists()){
                projFile = (ProjectFile)deserialize(ser_projectFilePath);
                if(workingDirectory.exists()){
                    projFile = checkAllPaths(projFile);
                }
                else{
                    projFile.queryPaths = new LinkedList<>();
                    projFile.importedQueryPaths = new LinkedList<>();
                    success = workingDirectory.mkdir() && success;
                }
                projFile.lastModified = currentDate;
            }
            if(!ser_projFile.exists() || projFile == null){
                newProject = true;
            }
        }
        if(newProject){
            projFile = new ProjectFile(projectTitle,currentDate, projectDirectoryPath);
            projFile.queryPaths = new LinkedList<>();
            projFile.importedQueryPaths = new LinkedList<>();
            projFile.lastModified = currentDate;
            projFile.lastActiveQueryPath = "";
            if(!workingDirectory.exists() && success){
                success = workingDirectory.mkdir() && success;
            }
        }
        
    }

    public static void addUserAgents(){
        userAgentsList = new LinkedList<>();
        userAgentsList.add("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0");
        userAgentsList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14");
        userAgentsList.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.100 Safari/537.36");
    }

    public static void serialize(Object obj,String filePath){
        FileOutputStream FOut = null;
        ObjectOutputStream OOut = null;
        try{
            FOut = new FileOutputStream(filePath,false);
            OOut = new ObjectOutputStream(FOut);
            OOut.writeObject(obj);
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

    public static Object deserialize(String filePath){
        Object out;
        try {
            FileInputStream FIn = new FileInputStream(filePath);
            ObjectInputStream OIn = new ObjectInputStream(FIn);
            out = OIn.readObject();
            OIn.close();
            FIn.close();
            return out;
        }catch(Exception ix) {
            System.out.println("Unable to deserialize file at - " + filePath );
            ix.printStackTrace();
            return null;
        }
    }

    public static String convertDate(String date){
        String[] splitDate = date.split("/");
        String output = splitDate[0] + "-" + splitDate[1] + "-" + splitDate[2];
        return output;
    }

    public static void serializeProjFile(){
        projFile.lastActiveQueryPath = currentQuery.isImported() ? importedDirectoryPath + File.separator + currentQuery.queryDirectoryName() : workingDirectoryPath + File.separator + currentQuery.queryDirectoryName();
        File ser_projFile = new File(projectDirectoryPath + File.separator + projectTitle + ".nproj");
        if(ser_projFile.exists()){
            ser_projFile.delete();
        }
        serialize(projFile,ser_projFile.getPath());
    }

    public static ProjectFile checkAllPaths(ProjectFile projFile){
        if(projFile != null){
            File workingDirectory = new File(projectDirectoryPath + File.separator + "queries");
            if(workingDirectory.exists()){
                LinkedList<String> queryPaths = projFile.queryPaths;
                for(int i=0;i<queryPaths.size();i++){
                    String queryPath = queryPaths.poll();
                    File checkFile = new File(queryPath);
                    if(checkFile.exists() && checkFile.isDirectory() && checkFile.getPath().contains(workingDirectory.getPath())){
                        if(checkForQuery(checkFile.getPath())){
                            queryPaths.add(queryPath);
                        }
                    }
                }
                projFile.queryPaths = queryPaths;
            }
            else{
                projFile.queryPaths = new LinkedList<>();
            }
            File importDirectory = new File(projectDirectoryPath + File.separator + "imported");
            if(importDirectory.exists()){
                LinkedList<String> importedQueryPaths = projFile.importedQueryPaths;
                for(int i=0;i<importedQueryPaths.size();i++){
                    String importedQueryPath = importedQueryPaths.poll();
                    File checkFile = new File(importedQueryPath);
                    if(checkFile.exists() && checkFile.isDirectory() && checkFile.getPath().contains(importDirectory.getPath())){
                        if(checkForQuery(checkFile.getPath())){
                            importedQueryPaths.add(importedQueryPath);
                        }
                    }
                }
                projFile.importedQueryPaths = importedQueryPaths;
            }
            else{
                projFile.importedQueryPaths = new LinkedList<>();
            }
        }
        return projFile;
    }

    public static LinkedList<ProjectFile> checkForProjects(LinkedList<ProjectFile> projects){
    	LinkedList<ProjectFile> newList = new LinkedList<ProjectFile>();
    	for(int i=projects.size()-1;i>=0;i--){
    		ProjectFile proj = projects.get(i);
    		File file = new File(proj.getLocation()+File.separator+proj.getProjectTitle());
    		File file2 = new File(Constants.getData()+File.separator+"imported"+File.separator+proj.getProjectTitle());
    		if(!file.exists()){
    			if(!file2.exists()){
    				System.out.println("Removing project from list: "+ file.getAbsolutePath());
    				
    			}else{
    				proj.setLocation(Constants.getData()+File.separator+"imported");
    				newList.add(proj);
    			}
    		}
    		else{
    			newList.add(proj);
    		}
    	}
    	return newList;
    }
    
    public static boolean checkForQuery(String queryDirectoryPath){
        File queryDirectory = new File(queryDirectoryPath);
        if(queryDirectory.exists() && queryDirectory.isDirectory()){
            String[] contents = queryDirectory.list();
            for(String str : contents){
                if(str.endsWith("nq")){
                    File queryDetailsFile = new File(queryDirectory.getPath() + File.separator + str);
                    if(queryDetailsFile.exists()){
                        QueryDetails query = (QueryDetails) deserialize(queryDetailsFile.getPath());
                        if(query != null && queryDirectory.getPath().endsWith(query.queryDirectoryName())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkForQuery(String query,String startDate,String endDate,boolean imported){
        String queryPath;
        if(!imported){
            queryPath = workingDirectoryPath + File.separator + query.toLowerCase() + "_" + convertDate(startDate) + "_" + convertDate(endDate);
        }
        else{
            queryPath = importedDirectoryPath + File.separator + query.toLowerCase() + "_" + convertDate(startDate) + "_" + convertDate(endDate);
        }
        return checkForQuery(queryPath);
    }

    public static boolean checkForProject(String projectDirectoryPath,String projectTitle){
        File projectDirectory = new File(projectDirectoryPath);
        if(projectDirectory.exists()){
            File ser_projFile = new File(projectDirectoryPath + File.separator + projectTitle);
            if(ser_projFile.exists()){
                ProjectFile projFile = (ProjectFile)deserialize(ser_projFile.getPath());
                if(projFile != null && projectDirectoryPath.endsWith(projFile.projectTitle)){
                    return true;
                }
            }
        }
        return false;
    }

//    public static QueryDetails addNewQuery(String projectTitle,String query,String startDate,String endDate){
//        QueryDetails newQuery = new QueryDetails(projectTitle,query,startDate,endDate);
//        String queryPath = workingDirectoryPath + File.separator + newQuery.queryDirectoryName();
//        File queryDirectory = new File(queryPath);
//        if(queryDirectory.exists()){
//            queryDirectory.delete();
//        }
//        queryDirectory.mkdir();
//        serialize(newQuery,queryPath + File.separator + query.toLowerCase() + ".nq");
//        return newQuery;
//    }

//    public static void pauseProgram(){
//        pause = true;
//    }
//
//    public static void playProgram(){
//        pause = false;
//    }

    public static void exportQuery(String queryPath,String zipPath,String queryTitle){
        try{
            ZipUtil.pack(new File(queryPath), new File(zipPath), new NameMapper() {
                public String map(String name) {
                    return queryTitle + File.separator + name;
                }
            });
        }catch(Exception ex){
            System.out.println("Error while exporting query");
            ex.printStackTrace();
        }
    }

    public static void exportProject(String projectDirectoryPath,String zipPath,String projectTitle){
        try{
            ZipUtil.pack(new File(projectDirectoryPath), new File(zipPath), new NameMapper() {
                public String map(String name) {
                    return projectTitle + File.separator + name;
                }
            });
        }catch(Exception ex){
            System.out.println("Error while exporting project");
            ex.printStackTrace();
        }
    }

    public static void importProject(String zipPath){
        File importDirectory = new File(Constants.getData()+File.separator+ "imported");
        if(!importDirectory.exists()){
            importDirectory.mkdir();
        }
        unZip(zipPath, importDirectory.getAbsolutePath());
        Path zipfile = Paths.get(zipPath);
        String s1 = zipfile.getFileName().toString();
        String projectName = s1.replaceAll("-zipped.zip", "");
        System.out.println(projectName);
        File importedProjectDirectory = new File(importDirectory.getPath()+File.separator+projectName);
        System.out.println(importedProjectDirectory.getAbsolutePath());
        File projectSer = new File(importedProjectDirectory.getPath()+File.separator+projectName+".ser");
        ProjectFile project = (ProjectFile) main.deserialize(projectSer.getAbsolutePath());
        File projectListFile=new File(Constants.getData()+File.separator+"projectList.ser");
		LinkedList<ProjectFile> projectList;
		if(!projectListFile.exists()){
			projectList=new LinkedList<ProjectFile>();
			projectList.add(project);
			main.serialize(projectList,projectListFile.getPath());
		}
		else{
			projectList = (LinkedList<ProjectFile>)main.deserialize(projectListFile.getPath());
			projectList.add(project);
			for(ProjectFile temp : projectList){
				System.out.println("   - " + temp.getProjectTitle());
			}
			main.serialize(projectList,projectListFile.getPath());
		}
    }

    public static void unZip(String zipPath,String outPath){
    	System.out.println(zipPath);
        ZipUtil.unpack(new File(zipPath),new File(outPath));
//        try{
//            Files.move(Paths.get(zipPath.substring(0,zipPath.lastIndexOf("/")) + File.separator + "unZipTempDirectory"), Paths.get(outPath),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
//        }catch(Exception ex){
//            System.out.println("Error while Unzipping");
//            ex.printStackTrace();
//        }
    }
     
    public static void writeToTXT(String filePath,String content,boolean append){
        File out = new File(filePath);
        if(out.exists() && !append){
            out.delete();
        }
        try{
            FileWriter outputFileWriter = new FileWriter(filePath,append);
            outputFileWriter.write(content);
            outputFileWriter.close();
        }catch(Exception e){
            System.out.println("Error while trying to write to file at - " + filePath);
        }
    }

    public static String readFromTXT(String filePath){
        File check = new File(filePath);
        if(!check.exists() || check.isDirectory()){
            return "File does not exist at specified path - " + filePath;
        }
        try{
            return new String(Files.readAllBytes(Paths.get(filePath)));
        }catch(Exception ex){
            ex.printStackTrace();
            return "Unable to read the file at - " + filePath;
        }
    }

    public static void deleteDir(String path){
        File dir = new File(path);
        if(dir.isDirectory()){
            try{
                FileUtils.deleteDirectory(dir);
            }catch(Exception e){
                System.out.println("Unable to delete Directory - " + path);
                e.printStackTrace();
            }
        }
        if(dir.exists()){
            dir.delete();
        }
    }

    public static String getCurrentDateTimeStamp(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String getContentFromWebPage(String url){
        String content = null;
        try{
            content = ArticleExtractor.INSTANCE.getText(new URL(url));
        }catch (BoilerpipeProcessingException bpex){
            System.out.println("Error during content extraction");
        }catch (MalformedURLException mx){
            System.out.println("Malformed URL - " + url);
        }
        return content;
    }

    public static LinkedList<String> checkForWords(String content,LinkedList<String> words){
        LinkedList<String> containedWords = new LinkedList<>();
        content = content.toLowerCase();
        for(String word : words){
        	word= word.toLowerCase();
            if(content.contains(word)){
                containedWords.add(word);
            }
        }
        return containedWords;
    }

    public static boolean hasAllWords(String content,LinkedList<String> words){
        return checkForWords(content,words).size() == words.size();
    }

}