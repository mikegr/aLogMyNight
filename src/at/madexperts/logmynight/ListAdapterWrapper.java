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
package at.madexperts.logmynight;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

abstract class ListAdapterWrapper implements ListAdapter {
	private ListAdapter delegate;
	public ListAdapterWrapper(ListAdapter listAdapter) {
		this.delegate = listAdapter;
	}
	public boolean areAllItemsEnabled() {
		return delegate.areAllItemsEnabled();
	}
	public int getCount() {
		return delegate.getCount();
	}
	public Object getItem(int position) {
		return delegate.getItem(position);
	}
	public long getItemId(int position) {
		return delegate.getItemId(position);
	}
	public int getItemViewType(int position) {
		return delegate.getItemViewType(position);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		return delegate.getView(position, convertView, parent);
	}
	public int getViewTypeCount() {
		return delegate.getViewTypeCount();
	}
	public boolean hasStableIds() {
		return delegate.hasStableIds();
	}
	public boolean isEmpty() {
		return delegate.isEmpty();
	}
	public boolean isEnabled(int position) {
		return delegate.isEnabled(position);
	}
	public void registerDataSetObserver(DataSetObserver observer) {
		delegate.registerDataSetObserver(observer);
	}
	public void unregisterDataSetObserver(DataSetObserver observer) {
		delegate.unregisterDataSetObserver(observer);
	}
	
	public ListAdapter getDelegate() {
		return delegate;
	}
}