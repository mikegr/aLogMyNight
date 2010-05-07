package at.madexperts.logmynight;

import java.io.File;

import android.os.Environment;

public class Utilities {

	public static final String NAME = "at.madexperts.logmynight";
	public static File getImageDirectory() {
		File file = new File(Environment.getExternalStorageDirectory(), NAME);
		if(!file.exists())
			file.mkdirs();
		return file;
	}
	
}
