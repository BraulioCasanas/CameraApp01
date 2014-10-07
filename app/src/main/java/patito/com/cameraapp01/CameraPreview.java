package patito.com.cameraapp01;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * A basic Camera preview
 * Created by braulio on 9/20/14.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    private String TAG = String.valueOf(R.string.app_name);


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a surfaceHolder.Callback so we get notified when the
        // underlying surface is create and destroy.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android version prior 3.0
//        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // The Surface has been created, now tell the camera where to draw the preview
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage(), e);
        }
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those event here.
        // Make sure to stop the preview before resizing or reformat it.

        if ( mHolder.getSurface() == null ) {
            // preview surface does not exist
            return;
        }

        try {
            mCamera.stopPreview();
        } catch ( Exception e ){
            // ignore: tried to stop a non-exist preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage(), e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }
}
