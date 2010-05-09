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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = DatabaseHelper.class.getName();
	public DatabaseHelper(Context ctx) {
		super(ctx, "LogMyNight", null, 5);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v(TAG, "Creating database");
		db.execSQL("CREATE TABLE drinks (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, category INTEGER, lastPrice INTEGER, alcohol INTEGER, volume INTEGER)");
		db.execSQL("CREATE TABLE drinklog (_id INTEGER PRIMARY KEY AUTOINCREMENT, drink_id INTEGER, location_id INTEGER, price INTEGER, log_time TEXT, FOREIGN KEY(drink_id) REFERENCES drinks(_id), FOREIGN KEY(location_id) REFERENCES location(_id))");
		db.execSQL("CREATE TABLE pics (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, creation TEXT, filename TEXT)");
		db.execSQL("CREATE TABLE location (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, longitude FLOAT, latitude FLOAT)");
		addTestdata(db);
	}

	private void addTestdata(SQLiteDatabase db) {
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Halbe Bier', 1, 300, 5, 500)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Seiderl Bier', 1, 250, 5, 350)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Achterl Weiß', 2, 250, 10, 125)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Achterl Rot', 2, 300, 12, 125)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Prosecco', 2, 250, 10, 100)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Tequilla Sunrise', 3, 700, 10, 250)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Mojito', 3, 700, 10, 250)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Long Island Ice Tea', 3, 700, 15, 250)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('B52', 4, 700, 15, 250)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Mineralwasser', 0,  700, 15, 250)");
		db.execSQL("INSERT INTO drinks (name, category, lastPrice, alcohol, volume) VALUES ('Red Bull', 0,  700, 15, 250)");
		
		//cats: 0= anti, 1=beer, 2=wein, 3=cocktail, 4=shot
		//INSERT INTO drinklog (drink_id, log_time, price) VALUES(1, datetime('now'), 3);
		//INSERT INTO drinklog (drink_id, log_time, price) VALUES(1, datetime('now'), 1);
		//INSERT INTO drinklog (drink_id, log_time, price) VALUES(1, datetime('now'), 5);
		//INSERT INTO drinklog (drink_id, log_time, price) VALUES(2, datetime('now'), 2);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(TAG, "Upgrade from" + oldVersion + "to" + newVersion);
		db.execSQL("DROP TABLE drinklog");
		db.execSQL("DROP TABLE drinks");
		if (oldVersion > 2) {
			db.execSQL("DROP TABLE pics");	
		}
		if (oldVersion > 3) {
			db.execSQL("DROP TABLE location");
		}
		onCreate(db);
	}
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.v(TAG, "Open database");
		super.onOpen(db);
	}

	public static void debug(SQLiteDatabase db, String table) {
		Cursor cursor = db.rawQuery("SELECT * FROM " + table, new String[0]);
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			Log.v(TAG, "Row " + i);
			for(int c = 0; c < cursor.getColumnCount(); c++) {
				Log.v(TAG, cursor.getColumnName(c)  + ":" + cursor.getString(c));
			}
		}
	}
	
	 

}
