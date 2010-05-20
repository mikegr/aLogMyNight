/**
 * 
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