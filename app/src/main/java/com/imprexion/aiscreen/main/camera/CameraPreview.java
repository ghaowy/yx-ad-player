package com.imprexion.aiscreen.main.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
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
            Log.d(TAG, "Error setting camera view: surfaceCreated");
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
            Log.d(TAG, "Error setting camera view: surfaceChanged");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        mSurfaceHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.lock();
        CameraTools.getInstance().releaseCamera();
        mCamera = null;

    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    }
}

