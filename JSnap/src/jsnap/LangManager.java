package jsnap;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class LangManager {
	
	private LangManager() {}
	private static HashMap<String, String> strings;
	
	public static void initialize(Component component) {
		File root = new File(".");
		ArrayList<String> languages = new ArrayList<>();
		for(File file : root.listFiles()) {
			if(!file.getName().startsWith("lang_"))
				continue;
			languages.add(file.getName());
		}
		
		if(languages.isEmpty())
			return;
		
		String selected;
		languages.add("default");
		if((selected = (String) JOptionPane.showInputDialog(component, "Select language:",
				"JSnap", JOptionPane.PLAIN_MESSAGE, null, languages.toArray(new String[0]), null)) == null) {
			System.exit(0);
		}else if(selected.equals("default")) {
			return;
		}
		
		try (FileInputStream stream = new FileInputStream(new File("./"+selected))){
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			strings = new HashMap<>();
			String line;
			while((line = reader.readLine()) != null) {
				String[] data = line.split("=");
				if(data.length <= 1)
					continue;
				strings.put(data[0], data[1]);
			}
		} catch (IOException e) {
			if(e instanceof FileNotFoundException) {
				JOptionPane.showMessageDialog(component, "Can't open language file", "JSnap", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(component, e.getMessage(), "JSnap", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static String getString(String key, String _default) {
		if(strings != null && strings.containsKey(key))
			return strings.get(key);
		return _default;
	}
}
