package com.imprexion.adplayer.main.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import com.imprexion.adplayer.tools.ALog;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

public class CameraTools {

    private static final String TAG = "CameraTools";
    private static Camera mCamera;
    private Camera.Parameters mParameters;
    private int cameraId;
    private int mScreenWidth;
    private int mScreenHeight;
    private static CameraTools mCameraTools;

    public boolean isRelease() {
        return isRelease;
    }

    private boolean isRelease;

    public CameraTools() {
    }

    public static CameraTools getInstance() {
        if (mCameraTools == null) {
            mCameraTools = new CameraTools();
        }
        return mCameraTools;
    }

    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            toast(context, "有相机");
            ALog.d(TAG, "有相机");
            init(context);
            return true;
        } else {
//            toast(context, "无相机");
            ALog.d(TAG, "无相机");
            return false;
        }
    }

    public void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point p = new Point();
        wm.getDefaultDisplay().getSize(p);
        mScreenWidth = p.x;
        mScreenHeight = p.y;
        ALog.d(TAG, "mScreenWidth=" + mScreenWidth + " mScreenHeight=" + mScreenHeight);
    }

    public Camera getCameraInstance() {
        try {
            if (mCamera == null) {
                synchronized (CameraTools.class) {
                    if (mCamera == null) {
                        findFrontFacingCamera();
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.d(TAG, "相机打开失败");
            return null;
        }
        initParam();
        return mCamera;
    }

    private void initParam() {
        if (mCamera == null) {
            return;
        }
        mCamera.setParameters(generateCameraParameters());
    }

    private Camera.Parameters generateCameraParameters() {
        mParameters = mCamera.getParameters();
        // 设置聚焦
        if (mParameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }
//        mCamera.cancelAutoFocus();//自动对焦。
        // 设置图片格式
        mParameters.setPictureFormat(PixelFormat.JPEG);
        // 设置照片质量
        mParameters.setJpegQuality(100);
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            // 默认打开前置摄像头，旋转90度即可
            mCamera.setDisplayOrientation(90);
            mParameters.setRotation(90);
        } else if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // 打开后置摄像头，旋转270，这个待验证
            mCamera.setDisplayOrientation(270);
            mParameters.setRotation(180);
        }

        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> picSizeList = mParameters.getSupportedPictureSizes();
//        for (Camera.Size size : picSizeList) {
//            ALog.i(TAG, "pictureSizeList size.width=" + size.width + "  size.height=" + size.height);
//        }
        Camera.Size picSize = getProperSize(picSizeList, ((float) mScreenHeight / mScreenWidth));
        mParameters.setPictureSize(picSize.width, picSize.height);

        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = mParameters.getSupportedPreviewSizes();
//        for (Camera.Size size : previewSizeList) {
//            ALog.i(TAG, "previewSizeList size.width=" + size.width + "  size.height=" + size.height);
//        }
        Camera.Size preSize = getProperSize(previewSizeList, ((float) mScreenHeight) / mScreenWidth);
        ALog.i(TAG, "final size is: " + picSize.width + " " + picSize.height);
        if (null != preSize) {
            ALog.i(TAG, "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
            mParameters.setPreviewSize(preSize.width, preSize.height);
        }

        return mParameters;
    }

    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        ALog.i(TAG, "screenRatio=" + screenRatio);
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;//0.5625
            ALog.d(TAG, "currentRatio = " + currentRatio);
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }

        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    ALog.d(TAG, "currentRatio = 4f / 3");
                    result = size;
                    break;
                }
            }
        }

        return result;
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
        }
        mCamera = null;
        isRelease = true;
    }

    public void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }


    public void findFrontFacingCamera() {
        int numberOfCamera = Camera.getNumberOfCameras();
        ALog.d(TAG, "CameraInfo.CAMERA_FACING_FRONT = " + Camera.CameraInfo.CAMERA_FACING_FRONT);
        for (int i = 0; i < numberOfCamera; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            ALog.d(TAG, "info.facing = " + info.facing + " ,i=" + i);
        }
    }
}
