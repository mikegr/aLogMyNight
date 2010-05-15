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

import java.text.NumberFormat;
import java.text.ParseException;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class LogActivity extends Activity implements OnClickListener{

	private final static String TAG = LogActivity.class.getName(); 
	SQLiteDatabase db;
	private int drink;
	
	private EditText price;
	private TextView times;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logdrink);
		drink = getIntent().getIntExtra("drink", -1);
		Log.d(TAG, "Drink: " + drink);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
		Cursor c = db.rawQuery("SELECT name, category, lastPrice FROM drinks WHERE _id = ?", new String[] {Integer.toString(drink)});
		c.moveToFirst();
		
		TextView nameView = (TextView) findViewById(R.id.log_drink);
		final Resources res = getResources();
		final String nameKey = c.getString(c.getColumnIndexOrThrow("name"));
		final int resId = res.getIdentifier(nameKey, "string", "at.madexperts.logmynight");
		
		nameView.setText(res.getText(resId, nameKey));
		
		ImageView imageView = (ImageView) findViewById(R.id.drinkImageView);
		int category = c.getInt(c.getColumnIndex("category"));
		imageView.setImageResource(Utilities.getBigIcon(category));
		
		int priceValue = c.getInt(c.getColumnIndexOrThrow("lastPrice"));
		price = (EditText) findViewById(R.id.priceEditText);
		updatePriceField(priceValue/100);
		
		
		times = (TextView) findViewById(R.id.countEditText);
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
		final int step = 10;
		bar.setMax(1000/step);
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
				updatePriceField(value);
			}
				
		});
		
		Button button =  (Button) findViewById(R.id.logButton);
		button.setOnClickListener(this);
	}
	
	private void updatePriceField(float value) {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(2);
		String result = format.format(value);
		Log.d(TAG, "progress: " + result);
		price.setText(result);		
	}
	public void onClick(View v) {
		
		try {
			Number number = NumberFormat.getNumberInstance().parse(price.getText().toString());
			Float f = number.floatValue();
			String newPrice = Integer.toString(Math.round(f * 100));
			Log.d(TAG, "newPrice: " + newPrice);
			
			int count = Integer.parseInt(times.getText().toString());
			Log.d(TAG, "count: " + count);
			
			db.execSQL("UPDATE drinks set lastPrice = ? WHERE _id = ?", new String[] {newPrice, Integer.toString(drink)});
			for (int i = 0; i < count; i++) {
				db.execSQL("INSERT INTO drinklog (drink_id, price, log_time) VALUES (?, ?, datetime('now'))", new String[] {Integer.toString(drink), newPrice});	
			}
			finish();
			
		} catch (ParseException e) {
			price.selectAll();
		}
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	
}
