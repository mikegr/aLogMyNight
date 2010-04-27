package at.madexperts.logmynight;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.LiveFolders;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HistoryActivity extends Activity {

	private SQLiteDatabase db;
	
	private ListView listView;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHelper(this).getReadableDatabase();
		
		
		setContentView(R.layout.history);
		listView = (ListView) findViewById(R.id.historyListView);

		showHistory(1);
	}

	private void showHistory(int days) {
		//SELECT d._id, d.name as name, COUNT(l._id) as counter, SUM(l.price) as itemsum FROM drinks d JOIN drinklog l ON d._id = l.drink_id WHERE l.log_time >= datetime('now', '-2 day') GROUP BY d._id, d.name;
		Cursor cursor = db.rawQuery(
				"SELECT d._id as _id, d.name as name, COUNT(l._id) as counter, SUM(l.price) as itemsum " +
				"FROM drinks d JOIN drinklog l ON d._id = l.drink_id " +
				"WHERE l.log_time >= datetime('now', '-1 day') " +
				"GROUP BY d._id, d.name", null); //new String[] {Integer.toString(days)});
		SimpleCursorAdapter adapter =  new SimpleCursorAdapter(this, R.layout.historyrow, cursor, new String[] {"name","counter","itemsum"}, new int[] {R.id.historyRowName, R.id.historyRowAmount, R.id.historyRowSum});		
		listView.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
}
