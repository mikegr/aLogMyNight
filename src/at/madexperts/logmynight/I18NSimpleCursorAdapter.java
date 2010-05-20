package at.madexperts.logmynight;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class I18NSimpleCursorAdapter extends SimpleCursorAdapter {
	
	private static final String TAG = I18NSimpleCursorAdapter.class.getName();

	private int[] i18nColums;
	private int[] to = new int[0];
	private Resources resources;
	
	/**
	 * 
	 * @param context
	 * @param layout
	 * @param c
	 * @param from
	 * @param to
	 * @param i18nColumns array of indices of from/to columns that are post-processed. 
	 * @param resources 
	 */
	public I18NSimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int[] i18nColumns) {
		super(context, layout, c, from, to);
		this.to = to;
		if (i18nColumns != null) {
			this.i18nColums = i18nColumns;	
		}
		this.resources = context.getResources();
	}
	
	@Override
	public void changeCursorAndColumns(Cursor c, String[] from, int[] to) {
		super.changeCursorAndColumns(c, from, to);
		this.to = to; 
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		for(int i = 0; i < i18nColums.length; i++) {
			int columnIdx = i18nColums[i];
			TextView text = (TextView) view.findViewById(to[columnIdx]);
			String key = text.getText().toString();
			Log.d(TAG, "Looking for resource: " + key);
			
			int resId = resources.getIdentifier(key, "string", "at.madexperts.logmynight");
			CharSequence value = resources.getText(resId, key);
			Log.d(TAG, "Found resource value: " + value);
			text.setText(value);
		}
		return view; 
	}
	

	
}
