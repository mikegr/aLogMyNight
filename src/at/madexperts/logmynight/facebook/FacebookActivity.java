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
package at.madexperts.logmynight.facebook;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


public class FacebookActivity extends Activity {

	private static final String TAG = FacebookActivity.class.getName();
	
	public static final int MESSAGE_PUBLISHED = 1834950434;
    private static final String API_KEY = "06893827c978510877e95202f7412ebe"; // "<YOUR API KEY>";

	// Enter either your API secret or a callback URL (as described in documentation):
    private static final String API_SECRET = "13a0adee6b8f69b9723ddfa0d657f2fe"; // "<YOUR SECRET KEY>";
    
    public static final String APPLICATION_ID = "110539402322122";

    private String[] permissions = new String[] { "publish_stream" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Dispatcher dispatcher = new Dispatcher(this);
        dispatcher.addHandler("login", LoginHandler.class);
        dispatcher.addHandler("publish", PublishHandler.class);

        /*
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("message", message);
        editor.commit();
        */

		Session session = Session.restore(this);
		if (session != null) {
			dispatcher.runHandler("publish");
		} else {
			dispatcher.runHandler("login");
		}
		
	}

}
