package UI;

import java.io.File;
import java.net.URISyntaxException;

public class Constants {
	public static String getData(){
		String s= null;
		try {
			s = Constants.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			s = s.substring(0, s.lastIndexOf(File.separator.toString()));
			s+=File.separator+"Data";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

}
