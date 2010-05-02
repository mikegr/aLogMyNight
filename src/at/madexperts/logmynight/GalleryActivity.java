package at.madexperts.logmynight;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryActivity extends Activity {
	
	GridView gallery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		gallery = (GridView) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this, Utilities.getImageDirectory()));
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
            view.setLayoutParams(new GridView.LayoutParams(85, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

	        Bitmap bm =  Bitmap.createScaledBitmap(BitmapFactory.decodeFile(directory.listFiles()[position].getAbsolutePath()),100, 100, true);
	        imageView.setImageBitmap(bm);
	        
	        textView.setText("My Name is Earl");
	        //imageView.setImageURI(Uri.fromFile());
	        return view;
	    }
	}

}
