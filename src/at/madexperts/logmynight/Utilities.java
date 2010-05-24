/*
 	LogMyNight - Android app for logging night activities. 
 	Copyright (c) 2010 Michael Greifeneder <mikegr@gmx.net>, Oliver Selinger <oliver.selinger@gmail.com>
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
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
