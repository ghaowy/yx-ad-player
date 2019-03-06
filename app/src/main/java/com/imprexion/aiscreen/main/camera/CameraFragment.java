package com.imprexion.aiscreen.main.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.imprexion.aiscreen.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CameraFragment extends Fragment {
    @BindView(R.id.fl_Camera)
    FrameLayout flCamera;

    private Camera mCamera;
    private Unbinder mUnbinder;
    private CameraPreview mCameraPreview;
    private final static String TAG = "CameraFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraPreview();
    }

    private void startCameraPreview() {
        if (CameraTools.getInstance().checkCameraHardware(getActivity())) {
            mCamera = CameraTools.getInstance().getCameraInstance();
        } else {
            return;
        }
        if(mCamera == null){
            Log.d(TAG,"camera is null");
            return;
        }
        mCameraPreview = new CameraPreview(getActivity());
        mCameraPreview.setCamera(mCamera);
        if (flCamera.getChildCount()  == 0) {
            flCamera.addView(mCameraPreview);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        flCamera.removeAllViews();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
