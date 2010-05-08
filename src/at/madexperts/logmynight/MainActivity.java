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

import java.util.List;

import org.apache.http.client.CircularRedirectException;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends ListActivity
{
	
	private final static String TAG = MainActivity.class.getName();
	
	
	SQLiteDatabase db;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this).getWritableDatabase();
        setContentView(R.layout.main);
        
        DatabaseHelper.debug(db, "drinks");
        DatabaseHelper.debug(db, "drinklog");
        
        final AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.mainLocationAutoCompleteTextView);
        //auto.setAdapter(adapter)
        auto.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
			
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null) {
					Log.d(TAG, "keyCode: " + event.getKeyCode());
				}
				Log.d(TAG, "actionId: " + actionId);
				Log.d(TAG, "ENTER: " + KeyEvent.KEYCODE_ENTER);
				
				LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
				
				List<String> provs = lm.getAllProviders();
				for (String prov : provs) {
					Log.d(TAG, "Provider: " + prov);
				}
				Criteria c = new Criteria();
				c.setAccuracy(Criteria.ACCURACY_COARSE);
				c.setAltitudeRequired(false);
				c.setBearingRequired(false);
				c.setCostAllowed(false);
				c.setPowerRequirement(Criteria.NO_REQUIREMENT);
				c.setSpeedRequired(false);
				String provider = lm.getBestProvider(c, true);
				Log.d(TAG, "Used provider: " + provider);
				Location location = lm.getLastKnownLocation(provider);
				double lat = 0;
				double lon = 0;
				if (location != null) {
					lat = location.getLatitude();
					lon = location.getLongitude();
				}
				String name = auto.getText().toString();
				Integer id = getLocationId(name);
				Log.d(TAG, "Id of entered Location: " + id);
				if ((id == null) || (id != null && hasLocationData(id))) {
					Log.d(TAG, "Open dialog");
					View view = getLayoutInflater().inflate(R.layout.yesnodialog, null);
					//TextView text = (TextView) view.findViewById(R.id.yesNoDialogText);
					
					Dialog dlg = new Dialog(MainActivity.this);
					dlg.setTitle("Sind Sie am aktuellen Ort?");
					dlg.setContentView(view);
					Log.d(TAG, "Show dialog");
					dlg.show();
				}
				else {
					SharedPreferences prefs = getSharedPreferences("default", MODE_PRIVATE);
					prefs.edit().putInt("location", id);
					Log.d(TAG, "Lat: " + lat + ";Long:" + lon);
				}
				return false;
			}
		});
        
        updateList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.mainmenu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.historyMenuItem: 
    			openHistory();
    			return true;
    		case R.id.galleryMenuItem:
    			openGallery();
    			return true;
    	}
    	// TODO Auto-generated method stub
    	return super.onOptionsItemSelected(item);
    }
    
    
    private Integer getLocationId(String name){
    	Cursor c = db.rawQuery("SELECT _id FROM location WHERE lower(name) = lower(?)", new String[] {name});
    	if (c.moveToFirst()) {
    		if (c.isNull(0)) {
    			return null;
    	    }
    		return c.getInt(0);
    	}
    	return null; 
    }
    
    private boolean hasLocationData(int id) {
    	Cursor c = db.rawQuery("SELECT latitude, longitude FROM location WHERE _id ?", new String[] {Integer.toString(id)});
    	if (c.moveToFirst()) {
        	if (c.isNull(0) || c.isNull(1)) {
        		return false;
        	}
        	return true;
    	}
    	return false;
    }
    
    private void openGallery() {
		Intent intent = new Intent(this, GalleryActivity.class);
		startActivity(intent);
	}

	private void openHistory() {
    	Intent intent = new Intent(this, HistoryActivity.class);
    	startActivity(intent);
    }

    public void updateList() {
        //SELECT d._id, d.name as name, COUNT(l._id) as counter FROM drinks d LEFT OUTER JOIN drinklog l ON d._id = l.drink_id GROUP BY d._id, d.name;
        Cursor allCursor = db.rawQuery(
        		"SELECT d._id as _id, d.name as name, COUNT(l._id) as counter " +
        		"FROM drinks d " +
        		"LEFT OUTER JOIN drinklog l ON d._id = l.drink_id " +
        		"GROUP BY d._id, d.name", null);
        
        
        //SELECT d._id, d.name as name, COUNT(l._id) as counter FROM drinks d LEFT OUTER JOIN drinklog l ON d._id = l.drink_id GROUP BY d._id, d.name ORDER BY COUNT(l._id) DESC LIMIT 10;
        Cursor favouriteCursor = db.rawQuery(
        		"SELECT d._id as _id, d.name as name, COUNT(l._id) as counter " +
        		"FROM drinks d " +
        		"LEFT OUTER JOIN drinklog l ON d._id = l.drink_id " +
        		"GROUP BY d._id, d.name " +
        		"ORDER BY COUNT(l._id) DESC LIMIT 3"
        		, null);
        
        //SELECT d._id, d.name as name, COUNT(l._id) as counter FROM drinks d LEFT OUTER JOIN drinklog l ON d._id = l.drink_id GROUP BY d._id, d.name HAVING MAX(l.log_time) = (SELECT MAX(log_time) FROM drinklog);
        Cursor lastCursor = db.rawQuery(
        		"SELECT d._id as _id, d.name as name, COUNT(l._id) as counter " +
        		"FROM drinks d LEFT OUTER JOIN drinklog l ON d._id = l.drink_id " +
        		"GROUP BY d._id, d.name " +
        		"HAVING MAX(l.log_time) = (SELECT MAX(log_time) FROM drinklog)", null);
        
        SectionedAdapter adapter=new SectionedAdapter() {
        	protected View getHeaderView(String caption, int index, View convertView, ViewGroup parent) {
        		Log.d(TAG, "Caption: " + caption + " for " + index);
        		View result = convertView;
        		if (result ==null) {
        			result = getLayoutInflater().inflate(R.layout.sectionheader, parent, false);
        			TextView textView = (TextView) result.findViewById(R.id.sectionHeader);
        			textView.setText(caption);
        		}
        		return(result);
        	}
        	@Override
        	public long getItemId(int position) {
        		long value = super.getItemId(position);
        		Log.d(TAG, "value=" + value);
        		return value; 
        	}
        };
        
        adapter.addSection("Last", new GradientAdapter(this, "Last", lastCursor));
        adapter.addSection("Favourites", new GradientAdapter(this, "Favourites", favouriteCursor));
        adapter.addSection("All drinks", new GradientAdapter(this, "All drinks", allCursor));
        
        
        setListAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	updateList();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	Log.d(TAG, "Clicked: position:" + position + ", id:" + id);
    	
    	Intent i = new Intent(this, LogActivity.class);
		i.putExtra("drink", (int) id);
		startActivityForResult(i, 0);
		
    }
    
    
    @Override
    protected void onDestroy() {
    	db.close();
    	super.onDestroy();
    }
    
    
    class GradientAdapter extends SimpleCursorAdapter {
    	private String header;
    	public GradientAdapter(Context ctx, String header, Cursor cursor) {
			super(ctx, R.layout.row, cursor, new String[] {"name", "counter"}, new int[] {R.id.rowText, R.id.rowCounter});
			this.header = header;
		}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		Log.d(TAG, "Requested in Gradient " + header + ":"+ position);
    		View view = super.getView(position, convertView, parent);
    		//setBackgroundResource(R.drawable.entry);
    		view.setBackgroundResource(R.drawable.entry);
    		return view;
    	}
    	
    	public String getHeader() {
			return header;
		}
    	
    }
}
