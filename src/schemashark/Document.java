package schemashark;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Document {
	private boolean isDirty = false;
	private String path = null;
	private String shortName = "Untitled";

	public void setDirty() {
		isDirty = true;
	}
	
	public boolean isDirty() {
		return isDirty;
	}
	
	public void setPath(String p) {
		path = p;
		
		Path pathObj = Paths.get(path);
		shortName = pathObj.getFileName().toString();
	}
	
	public String getShortName() {
		return shortName;
	}
}
