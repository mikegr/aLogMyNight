package at.madexperts.logmynight;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


public class CameraActivity extends Activity implements SurfaceHolder.Callback,
			Camera.AutoFocusCallback, Camera.PictureCallback {

	private static final String TAG = CameraActivity.class.getName(); 
	Camera mCamera;
	boolean mPreviewRunning = false;
	ImageButton takePictureButton;
	//View waitView;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		
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

	public void onPictureTaken(byte[] data, Camera camera) {
		Log.d(TAG, "onPictureTaken() started ");
		// Matrix m = new Matrix();
		// m.postRotate(90);
		// Bitmap nb = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
		// m, true);

		try {
			FileOutputStream fos = openFileOutput(file_name, MODE_PRIVATE);
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
