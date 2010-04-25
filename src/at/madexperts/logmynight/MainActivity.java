package at.madexperts.logmynight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
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
        		"ORDER BY COUNT(l._id) DESC LIMIT 10"
        		, null);
        
        //SELECT d._id, d.name as name, COUNT(l._id) as counter FROM drinks d LEFT OUTER JOIN drinklog l ON d._id = l.drink_id GROUP BY d._id, d.name HAVING MAX(l.log_time) = (SELECT MAX(log_time) FROM drinklog);
        Cursor lastCursor = db.rawQuery(
        		"SELECT d._id as _id, d.name as name, COUNT(l._id) as counter " +
        		"FROM drinks d LEFT OUTER JOIN drinklog l ON d._id = l.drink_id " +
        		"GROUP BY d._id, d.name " +
        		"HAVING MAX(l.log_time) = (SELECT MAX(log_time) FROM drinklog)", null);
        
        
        List<GradientAdapter> adapters = new ArrayList<GradientAdapter>();
        adapters.add(new GradientAdapter(this, "Last", lastCursor));
        adapters.add(new GradientAdapter(this, "Favourites", favouriteCursor));
        adapters.add(new GradientAdapter(this, "All drinks", allCursor));
        setListAdapter(new SectionAdapter(adapters));
        
        //setListAdapter(new GradientAdapter(this));
    }
    
    @Override
    protected void onDestroy() {
    	db.close();
    	super.onDestroy();
    }
    
    class SectionAdapter extends BaseAdapter {
    	
    	private List<GradientAdapter> adapters;
    	
    	
    	public SectionAdapter(List<GradientAdapter> adapters) {
    		this.adapters = adapters;
    	}
    	
    	public int getCount() {
    		int count = 0;
    		for (Adapter a:adapters) {
    			count++; //header
				count += a.getCount();
			}
    		Log.d(TAG, " count all sections + headers:" + count);
    		return count;
    	}
    	
    	public Object getItem(int position) {
    		int counter = 0;
    		for(Adapter adapter:adapters) {
    			if (position == counter) { //header
    				return null;  
    			}
    			int size = adapter.getCount();
    			if (counter + size >= position) {
    				return adapter.getItem(position - counter - 1);
    			}
    			counter = counter + size + 1;
    		}
    		return null;
    	}
    	
    	public long getItemId(int position) {
    		return position; //TODO: convert to item id
    	}
    	public View getView(int position, View convertView, ViewGroup parent) {
    		Log.d(TAG, "Requested view position:" + position);
    		int counter = 0;
    		for(GradientAdapter adapter:adapters) {
    			if (position == counter) { //header
    				View view = getLayoutInflater().inflate(R.layout.sectionheader, parent, false);
    				TextView text = (TextView) view.findViewById(R.id.sectionHeader);
    				text.setText(adapter.getHeader());
    				return view;
    			}
    			int size = adapter.getCount();
    			if (counter + size >= position) {
    				return adapter.getView(position - counter - 1, convertView, parent);
    			}
    			counter = counter + size + 1;
    		}
    		return null;
    	}
    
    	@Override
    	public boolean isEnabled(int position) {
    		int counter = 0;
    		for(Adapter adapter:adapters) {
    			if (position == counter) { //header
    				return false;  
    			}
    			counter = counter + adapter.getCount() + 1;
    		}
    		return true; 
    	}
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
