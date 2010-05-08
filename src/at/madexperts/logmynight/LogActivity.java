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

import java.text.DecimalFormat;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class LogActivity extends Activity implements OnClickListener{

	private final static String TAG = LogActivity.class.getName(); 
	SQLiteDatabase db;
	private int drink;
	
	private EditText price;
	private EditText times;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logdrink);
		drink = getIntent().getIntExtra("drink", -1);
		Log.d(TAG, "Drink: " + drink);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
		Cursor c = db.rawQuery("SELECT name, lastPrice FROM drinks WHERE _id = ?", new String[] {Integer.toString(drink)});
		c.moveToFirst();
		
		TextView nameView = (TextView) findViewById(R.id.log_drink);
		nameView.setText(c.getString(c.getColumnIndexOrThrow("name")));
		
		int priceValue = c.getInt(c.getColumnIndexOrThrow("lastPrice"));
		price = (EditText) findViewById(R.id.priceEditText);
		price.setText(Integer.toString(priceValue/100));
		
		times = (EditText) findViewById(R.id.countEditText);
		((Button) findViewById(R.id.upButton)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String t = times.getText().toString();
				Log.d(TAG, "times: " + t);
				int value = Integer.parseInt(t);
				times.setText(Integer.toString(value+1));
			}
		});
		
		((Button) findViewById(R.id.downButton)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String t = times.getText().toString();
				Log.d(TAG, "times: " + t);
				int value = Integer.parseInt(t);
				if (value > 0) {
					times.setText(Integer.toString(value-1));	
				}
			}
		});
		SeekBar bar = (SeekBar) findViewById(R.id.priceSeekBar);
		final int step = 5;
		bar.setMax(1500/step);
		bar.setProgress(priceValue/step);
		bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				float value = ((float)progress*step)/100;
				String result = new DecimalFormat("#0.00").format(value);
				Log.d(TAG, "progress: " + result);
				price.setText(result);
			}
		});
		
		Button button =  (Button) findViewById(R.id.logButton);
		button.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		 
		Float f = Float.valueOf(price.getText().toString());
		String newPrice = Integer.toString(Math.round(f * 100));
		Log.d(TAG, "newPrice: " + newPrice);
		
		int count = Integer.parseInt(times.getText().toString());
		Log.d(TAG, "count: " + count);
		
		long loc_id = getSharedPreferences("default", MODE_PRIVATE).getLong("location", -1);
		Log.d(TAG, "Using this location for drink logging: " + loc_id);
		
		db.execSQL("UPDATE drinks set lastPrice = ? WHERE _id = ?", new String[] {newPrice, Integer.toString(drink)});
		for (int i = 0; i < count; i++) {
			db.execSQL("INSERT INTO drinklog (drink_id, location_id, price, log_time) VALUES (?, ?, ?, datetime('now'))", 
					new String[] {Integer.toString(drink), Long.toString(loc_id), newPrice});	
		}
		finish();
		
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
}
