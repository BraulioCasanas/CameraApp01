package patito.com.cameraapp01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {

    static String TAG = String.valueOf(R.string.app_name);

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ( ! checkCameraHardware(this))
            return;

        initializeCamera();

        // add a listener to the capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get a image from the camera
                mCamera.takePicture(null, null, mPicture);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Check is this device has a camera
     * @param context
     * @return boolean
     */
    private boolean checkCameraHardware(Context context) {

        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     *  A safe way to get an instance of the Camera Object
     * @return {@link android.hardware.Camera}
     */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available ( in use or does not exist)
            Log.e(TAG, "Error at attempt to get a Camera instance ", e);
        }
        return c; // return null if camera in unavailable
    }

    // add listener to the Capture button

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            // start preview camera
            mCamera.startPreview();

            File pictureFile = SaveMediaFiles.getOutputMediaFile(SaveMediaFiles.MEDIA_TYPE_IMAGE);
            if ( pictureFile == null ){
                Log.d(TAG, "Error creating media file, check storage permissions ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                galleryAddPic(pictureFile);

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found ",  e);
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file ",  e);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera == null) {
            initializeCamera();
        }
    }

    @Override
    protected void onPause() {
        releaseCamera();
        releasePreview();
        super.onPause();
    }

    private void galleryAddPic(File pictureFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(pictureFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void releasePreview() {
        preview.removeView(mPreview);
    }

    private void releaseCamera() {
        if ( mCamera != null ) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    private void initializeCamera(){

        // create an instance of a Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }
}