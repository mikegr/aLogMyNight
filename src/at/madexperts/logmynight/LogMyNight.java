package at.madexperts.logmynight;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LogMyNight extends ListActivity
{
	
	String[] items = new String[] {"beer", "wine", "cocktail"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListAdapter(new GradientAdapter(this));
    }
    
    class GradientAdapter extends ArrayAdapter {
    	public GradientAdapter(Context ctx) {
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
