package com.imprexion.adplayer.main.camera;

import android.content.Context;
import android.hardware.Camera;
import com.imprexion.library.logger.YxLogger;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback, Camera.PictureCallback {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private final static String TAG = "CameraPreview";

    public CameraPreview(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        mSurfaceHolder.addCallback(this);
        surfaceCreated(mSurfaceHolder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            YxLogger.d(TAG, "Error setting camera view: surfaceCreated");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (mSurfaceHolder == null) {
            return;
        }

        mCamera.stopPreview();

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            YxLogger.d(TAG, "Error setting camera view: surfaceChanged");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        YxLogger.d(TAG, "surfaceDestroyed");
        holder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.lock();
        CameraTools.getInstance().releaseCamera();
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }
}

