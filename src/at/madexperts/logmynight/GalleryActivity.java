package at.madexperts.logmynight;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryActivity extends Activity implements OnClickListener {
	
	private static final String TAG = GalleryActivity.class.getName();
	SQLiteDatabase db;
	GridView gallery;
	Button button;
	ImageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHelper(this).getReadableDatabase();
		setContentView(R.layout.gallery);

		button = (Button) findViewById(R.id.galleryButton);
		button.setOnClickListener(this);
		
		gallery = (GridView) findViewById(R.id.gallery);
		adapter = new ImageAdapter(this, Utilities.getImageDirectory());
		gallery.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
	public void onClick(View v) {
		Intent intent = new Intent(this, CameraActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			adapter.notifyDataSetChanged();
		}
	}
	
			
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private File directory;

	    public ImageAdapter(Context c, File directory) {
	        mContext = c;
	        this.directory = directory;
	    }

	    public int getCount() {
	    	return directory.listFiles().length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view;

	    	if (convertView == null) {  // if it's not recycled, initialize some attributes
	            view = getLayoutInflater().inflate(R.layout.galleryitem, null);
	            
	            
	        } else {
	            view = convertView;
	        }
	    	
            ImageView imageView = (ImageView) view.findViewById(R.id.galleryItemImage);
            TextView textView = (TextView) view.findViewById(R.id.galleryItemText);
            view.setLayoutParams(new GridView.LayoutParams(85, 140));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

            File file = directory.listFiles()[position];
	        Bitmap bm =  Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()),100, 100, true);
	        imageView.setImageBitmap(bm);
	
	        Cursor cursor =  db.rawQuery("SELECT name FROM pics WHERE filename = ?", new String[] {file.getName()});
	        cursor.moveToFirst();
	        String name = cursor.getString(cursor.getColumnIndex("name"));
	        Log.d(TAG, "Read name from db:" + name);
	        textView.setText(name);
	        //imageView.setImageURI(Uri.fromFile());
	        return view;
	    }
	}

}
