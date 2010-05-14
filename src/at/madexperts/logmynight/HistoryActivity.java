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

import java.util.ArrayList;
import java.util.List;

import com.codecarpet.fbconnect.FBFeedActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import at.madexperts.logmynight.chart.BarChartView;
import at.madexperts.logmynight.chart.BarItem;

public class HistoryActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private static final String TAG = HistoryActivity.class.getName();
	private SQLiteDatabase db;

	private ListView listView;
	private ListView sumView;
	private Button button;

	private TextView checkLabel;

	private BarChartView barChartView;

	private static final int MESSAGE_PUBLISHED = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHelper(this).getReadableDatabase();

		/* First, get the Display from the WindowManager */
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();

		/* Now we can retrieve all display-related infos */
		int orientation = display.getOrientation();
		Log.d(TAG, "Orientation: " + orientation);

		if (orientation == 0) {
			setContentView(R.layout.history);
			listView = (ListView) findViewById(R.id.historyListView);
			View view = getLayoutInflater().inflate(R.layout.historyheader,
					null);
			((TextView) view.findViewById(R.id.historyRowName))
					.setText("Drink");
			((TextView) view.findViewById(R.id.historyRowSum)).setText("Summe");
			((TextView) view.findViewById(R.id.historyRowAmount))
					.setText("Menge");
			listView.addHeaderView(view);

			checkLabel = (TextView) findViewById(R.id.checkText);
			checkLabel.setText("letzten 24h");

			sumView = (ListView) findViewById(R.id.historySum);
			button = (Button) findViewById(R.id.historyTimeButton);
			button.setOnClickListener(this);
			showHistory(1);
		} else {
			setContentView(R.layout.barchart);

			days = (Integer) getLastNonConfigurationInstance();
			if (days == null)
				days = 1;

			createBarChartDataList(days);
			barChartView = (BarChartView) findViewById(R.id.barChartView);
			barChartView.setData(barChartData);
		}
	}

	Integer days;

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, "onRetainNonConfigurationInstance");
		return days;
	}

	public void onConfigurationChanged(Configuration configuration) {
		super.onConfigurationChanged(configuration);
		Log.d(TAG, "onConfigurationChanged");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.historymenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.shareMenuItem:
			share("Message from Android!");
			return true;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	private void share(String txt) {
		FacebookHelper h = new FacebookHelper(this);
		h.publish(txt);
	}

	private Dialog dlg;
	private ListView timeView;

	public void onClick(View v) {
		Log.d(TAG, "onClick from ID: " + v.getId());
		ArrayAdapter<String> a = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				new String[] { "letzten 24h", "letzte Woche", "letzter Monat",
						"Alle" });
		timeView = new ListView(this);
		timeView.setAdapter(a);
		timeView.setOnItemClickListener(this);
		dlg = new Dialog(this);
		dlg.setContentView(timeView);

		dlg.setTitle("Zeitraum ändern");
		dlg.show();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		dlg.dismiss();
		Log.d(TAG, "id:" + id);
		switch ((int) id) {
		case 0:
			showHistory(1);
			checkLabel.setText("letzten 24h");
			break;
		case 1:
			showHistory(7);
			checkLabel.setText("letzte Woche");
			break;
		case 2:
			showHistory(30);
			checkLabel.setText("letzten Monat");
			break;
		case 3:
			showHistoryAll();
			checkLabel.setText("gesamter Zeitraum");
			break;
		default:
		}

	}

	private void showHistoryAll() {
		// TODO: implement correctly
		showHistory(365);
	}

	private List<BarItem> barChartData;

	private void createBarChartDataList(int days) {
		/*******************************************************************************
		 * create list for chart view
		 */
		barChartData = new ArrayList<BarItem>();

		Cursor cursor2 = db
				.rawQuery(
						"SELECT d._id as _id, d.name as name, COUNT(l._id) as counter, SUM(l.price) as itemsum "
								+ "FROM drinks d JOIN drinklog l ON d._id = l.drink_id "
								+ "WHERE l.log_time >= datetime('now', '-"
								+ days + " day') " + "GROUP BY d._id, d.name",
						null);

		/* Get the indices of the Columns we will need */
		int nameColumn = cursor2.getColumnIndex("name");
		int amountColumn = cursor2.getColumnIndex("counter");

		/* Check if our result was valid. */
		if (cursor2 != null) {
			/* Check if at least one Result was returned. */
			if (cursor2.moveToFirst()) {
				int i = 0;
				/* Loop through all Results */
				do {
					i++;
					/*
					 * Retrieve the values of the Entry the Cursor is pointing
					 * to.
					 */
					String name = cursor2.getString(nameColumn);
					int amount = cursor2.getInt(amountColumn);

					/*
					 * We can also receive the Name of a Column by its Index.
					 * Makes no sense, as we already know the Name, but just to
					 * shwo we can Wink String ageColumName =
					 * c.getColumnName(ageColumn);
					 */

					/* Add current Entry to results. */
					barChartData.add(new BarItem(name, amount));
				} while (cursor2.moveToNext());
			}

			cursor2.close();
		}
	}

	private void showHistory(int days) {
		this.days = days;

		// SELECT d._id, d.name as name, COUNT(l._id) as counter, SUM(l.price)
		// as itemsum FROM drinks d JOIN drinklog l ON d._id = l.drink_id WHERE
		// l.log_time >= datetime('now', '-2 day') GROUP BY d._id, d.name;
		Cursor cursor = db
				.rawQuery(
						"SELECT d._id as _id, d.name as name, d.category as category, COUNT(l._id) as counter, SUM(l.price) as itemsum "
								+ "FROM drinks d JOIN drinklog l ON d._id = l.drink_id "
								+ "WHERE l.log_time >= datetime('now', '-"
								+ days + " day') " + "GROUP BY d._id, d.name",
						null); // new String[] {Integer.toString(days)});
//		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
//				R.layout.historyrow, cursor, new String[] { "name", "counter",
//						"itemsum" }, new int[] { R.id.historyRowName,
//						R.id.historyRowAmount, R.id.historyRowSum });

		Cursor sumCursor = db
				.rawQuery(
						"SELECT '0' as _id, 'Summe:' as name, COUNT(l._id) as counter, SUM(l.price) as itemsum "
								+ "FROM drinks d JOIN drinklog l ON d._id = l.drink_id "
								+ "WHERE l.log_time >= datetime('now', '-"
								+ days + " day') ", null); // new String[]
		// {Integer.toString(days)});

		SimpleCursorAdapter sumAdapter = new SimpleCursorAdapter(this,
				R.layout.historysum, sumCursor, new String[] { "name",
						"counter", "itemsum" }, new int[] {
						R.id.historyRowName, R.id.historyRowAmount,
						R.id.historyRowSum });
		sumView.setAdapter(sumAdapter);

		listView.setAdapter(new ImageAdapter(this, cursor));
	}

	class ImageAdapter extends SimpleCursorAdapter {
		private String header;

		public ImageAdapter(Context ctx, Cursor cursor) {
			super(ctx, R.layout.historyrow, cursor, new String[] { "category", "name",
					"counter", "itemsum" }, new int[] { R.id.historyRowImage,
					R.id.historyRowName, R.id.historyRowAmount,
					R.id.historyRowSum });
		}

		/*
		 * @Override
		 * 
		 * public void setViewImage(ImageView v, String value) { Log.d(TAG,
		 * "setImageView: " + value); super.setViewImage(v, getIcon(value)); }
		 */

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d(TAG, "Requested in Gradient " + header + ":" + position);
			View view = super.getView(position, convertView, parent);
			// setBackgroundResource(R.drawable.entry);
			//view.setBackgroundResource(R.drawable.entry);
			ImageView imageView = (ImageView) view.findViewById(R.id.historyRowImage);
			Cursor cursor = getCursor();
			int category = cursor.getInt(cursor.getColumnIndex("category"));
			imageView.setImageResource(Utilities.getIcon(category));
			return view;
		}

	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
}
