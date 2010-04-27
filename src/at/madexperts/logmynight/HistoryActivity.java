package at.madexperts.logmynight;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryActivity extends Activity {

	private SQLiteDatabase db;
	private TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHelper(this).getReadableDatabase();
		text = (TextView) findViewById(R.id.historyText);
		
		//db.rawQuery(sql, selectionArgs);
		
		setContentView(R.layout.history);
		
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
}
