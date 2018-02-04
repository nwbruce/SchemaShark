package schemashark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Document {
	private boolean isDirty = false;
	private File file = null;
	private String shortName = "Untitled";

	public void setDirty() {
		isDirty = true;
	}
	
	public boolean isDirty() {
		return isDirty;
	}
	
	public void setFile(File f) {
		file = f;
		shortName = file.getName();
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public boolean hasFileName() {
		return file != null;
	}
	
	public String read() throws FileNotFoundException, IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			return sb.toString();
		}
	}
	
	public void write(String text) throws IOException {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
			bw.write(text);
		}
		isDirty = false;
	}
	
	public String[] getEmptyXsd() {
		String[] result = new String[2];
		result[0] = "<?xml version=\"1.0\"?>\n"
				+ "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n";
		result[1] = "\n</xs:schema>\n";
		return result;
	}
}
