package at.madexperts.logmynight;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class CameraActivity extends Activity implements SurfaceHolder.Callback,
			Camera.AutoFocusCallback, Camera.PictureCallback {

	SQLiteDatabase db;
	private static final String TAG = CameraActivity.class.getName(); 
	Camera mCamera;
	boolean mPreviewRunning = false;
	ImageButton takePictureButton;
	//View waitView;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		db = new DatabaseHelper(this).getWritableDatabase();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		
		
		setContentView(R.layout.camera);

		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);

		//waitView = findViewById(R.id.wait_icon);

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);
		}
		catch (Throwable t) {
			Log.e("PreviewDemo-surfaceCallback",
			"Exception in setPreviewDisplay()", t);
			Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		
		Camera.Parameters parameters= mCamera.getParameters();
		parameters.setPreviewSize(w, h);
		parameters.setPictureFormat(PixelFormat.JPEG);
		
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
	}

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;

	public void onAutoFocus(boolean success, Camera camera) {
		Log.d(TAG, "onAutoFocus() called");
		camera.takePicture(null, null, this);
	}

	private static String file_name = "temp_img";

	public void onPictureTaken(final byte[] data, Camera camera) {
		Log.d(TAG, "onPictureTaken() started ");
		// Matrix m = new Matrix();
		// m.postRotate(90);
		// Bitmap nb = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
		// m, true);
		//DateFormat df = new DateFormat();

		final Dialog dlg = new Dialog(this);
		View view = getLayoutInflater().inflate(R.layout.cameradialog, null);
		Button okButton = (Button) view.findViewById(R.id.cameraDialogOKButton);
		final EditText edit = (EditText) view.findViewById(R.id.cameraDialogEditText);
		okButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dlg.dismiss();
				Log.d(TAG, "Entered name: " + edit.getText().toString());
				String filename = DateFormat.format("yyyyMMdd-kkmmss", new Date()) + ".jpg";
				new SavePhotoAsyncTask(filename).execute(data);
			}
		});
		dlg.setContentView(view);
		
		dlg.setTitle("Enter name:");
		dlg.show();

		
		/*
		Intent i = new Intent(this, PhotoEditActivity.class);
		i.putExtra("data", file_name);
		this.finish();
		startActivity(i);
		*/
	
		Log.d(TAG, "onPictureTaken() finished");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown() called: " + keyCode  + "|Event:" + event.getKeyCode());
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_CAMERA) {
			mCamera.autoFocus(this);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
