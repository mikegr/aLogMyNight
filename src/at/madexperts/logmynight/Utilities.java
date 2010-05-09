package at.madexperts.logmynight;

import java.io.File;

import android.os.Environment;

public class Utilities {

	public static final String NAME = "at.madexperts.logmynight";
	public static File getImageDirectory() {
		File file = new File(Environment.getExternalStorageDirectory(), NAME);
		file.mkdirs();
		return file;
	}
	
	public static int getIcon(int category) {
		switch(category) {
			case 0: return R.drawable.icon_anti_small;
			case 1: return R.drawable.icon_bier_small;
			case 2: return R.drawable.icon_wein_small;
			case 3: return R.drawable.icon_cocktail_small;
			case 4: return R.drawable.icon_shot_small;
		}
		return android.R.drawable.gallery_thumb;
	}
	
	public static int getBigIcon(int category) {
		switch(category) {
			case 0: return R.drawable.icon_anti_big;
			case 1: return R.drawable.icon_bier_big;
			case 2: return R.drawable.icon_wein_big;
			case 3: return R.drawable.icon_cocktail_big;
			case 4: return R.drawable.icon_shot_big;
		}
		return android.R.drawable.gallery_thumb;		
	}
	

}
