package at.madexperts.logmynight;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LogMyNight extends Activity
{
	
	String[] lastItems = new String[] {"Beer"};
	String[] favItems = new String[] {"Beer", "Wine"};
	String[] allItems = new String[] {"Beer", "Wine", "Cocktail", "Prosecco", "Water"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sections);
        
        ListView allList = (ListView) findViewById(R.id.allList);
        allList.setAdapter(new GradientAdapter(this, allItems));
        ListView favourites = (ListView) findViewById(R.id.favouriteList);
        favourites.setAdapter(new GradientAdapter(this,favItems));
        ListView lastItem = (ListView) findViewById(R.id.lastList);
        lastItem.setAdapter(new GradientAdapter(this, lastItems));
        
        //setListAdapter(new GradientAdapter(this));
    }
    
    class GradientAdapter extends ArrayAdapter {
    	public GradientAdapter(Context ctx, String[] items) {
			super(ctx, R.layout.row, R.id.rowText, items);
		}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View view = super.getView(position, convertView, parent);
    		//setBackgroundResource(R.drawable.entry);
    		view.setBackgroundResource(R.drawable.entry);
    		
    		return view;
    	}
    }
    
    

    
    
}
