package ic3cub3.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Settings {
	public static final String SETTINGSFILE = "settings.dat";
	public static final Set<Long> EMPTY_SET = Collections.emptySet();
	
	public static String COMMAND = null;
	
	//read the settings file
	static{
		File config = new File(SETTINGSFILE);
		try {
			Scanner scan = new Scanner(config);
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				Scanner linescan = new Scanner(line);
				String option = linescan.next();
				switch(option){
					case "logic2cnf_path":
						linescan.next("="); //assume assignment token is next
						COMMAND = linescan.nextLine().trim(); //rest of the option is text (including spaces inbetween)
					break;
					default:
						System.err.println("Warning: unknown option encountered... "+option);
						break;
				}
				
				linescan.close();
			}
			scan.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}
}
