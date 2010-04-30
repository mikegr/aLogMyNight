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

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import at.madexperts.logmynight.MainActivity.GradientAdapter;

/**
 * Atempt to implement a section adapter by myself. Buggy
 * @author Michael Greifeneder
 */
class MySectionAdapter extends BaseAdapter {
	private static final String TAG = MySectionAdapter.class.getName();
	
	private List<GradientAdapter> adapters;
	private Activity activity;
	
	public MySectionAdapter(Activity activity, List<GradientAdapter> adapters) {
		this.activity = activity;
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
				View view = activity.getLayoutInflater().inflate(R.layout.sectionheader, parent, false);
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
	public int getViewTypeCount() {
		int counter = 1;
		for(Adapter adapter:adapters) {
			counter = counter + adapter.getViewTypeCount() +1;
		}
		return counter;
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
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
}