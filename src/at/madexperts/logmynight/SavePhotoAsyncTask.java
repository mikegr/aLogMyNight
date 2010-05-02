/*
 	LogMyNight - Android app for logging night activities. 
 	Copyright (c) 2010 Michael Greifeneder <mikegr@gmx.net>
 
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
import java.io.FileOutputStream;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class SavePhotoAsyncTask extends AsyncTask<byte[], String, String> {
	
	private String name;
	public SavePhotoAsyncTask(String name) {
		this.name = name;
	}
	@Override
	protected String doInBackground(byte[]... jpeg) {
		File dir = new File(Environment.getExternalStorageDirectory(), "at.madexperts.logmynight");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File photo = new File(dir, name);
		if (photo.exists()) {
			photo.delete();
		}
		try {
			FileOutputStream fos=new FileOutputStream(photo.getPath());
			fos.write(jpeg[0]);
			fos.close();
		}
		catch (java.io.IOException e) {
			Log.e("PictureDemo", "Exception in photoCallback", e);
		}
		return(null);
	}
}
