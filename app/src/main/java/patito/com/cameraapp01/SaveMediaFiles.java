package patito.com.cameraapp01;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by braulio on 9/22/14.
 */
public class SaveMediaFiles {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     *  create a file uri for saving an image or video
     * @param type
     * @return
     */
    static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     * @param type
     * @return
     */
    static File getOutputMediaFile(int type) {

        File directory;

        switch (type) {
            case MEDIA_TYPE_IMAGE:
                directory = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                break;
            case MEDIA_TYPE_VIDEO:
                directory = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                break;
            default:
                directory = null;
        }

        // to be safe, you should check that the SD-card is mounted
        // using Environment.getExternalStorageSate() before doing this.
        File mediaStorageDir = new File(directory, "MyCameraapp");
        // this location work best if you want to created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create a storage directory if it does not exist
        if ( ! mediaStorageDir.exists() ) {
            if ( ! mediaStorageDir.mkdirs() ){
                Log.d("MyCameraApp", "Failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        switch (type) {
            case MEDIA_TYPE_IMAGE:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                break;
            case MEDIA_TYPE_VIDEO:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
                break;
            default:
                mediaFile = null;
                break;
        }

        return mediaFile;
    }
}
