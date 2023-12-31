/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jotangi.healthy.googlevision;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.annotation.KeepName;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.Barcode;
import com.jotangi.healthy.HpayMall.ApiUrl;
import com.jotangi.healthy.HpayMall.CustomDaialog;
import com.jotangi.healthy.HpayMall.MemberBean;
import com.jotangi.healthy.R;
import com.jotangi.healthy.googlevision.barcodescanner.BarcodeScannerProcessor;
import com.jotangi.healthy.googlevision.preference.PreferenceUtils;
import com.jotangi.healthy.ui.ProjConstraintFragment;
import com.jotangi.healthy.ui.home.HomeMainFragment;
import com.jotangi.jotangi2021.AndroidModule.Api.ApiConUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Live preview demo app for ML Kit APIs using CameraX.
 * ♡♡♡♡♡♡♡♡♡♡♡Kelly 's Note♡♡♡♡♡♡´ސު｀
 * ====================================================
 * ProjBaseFragment==>健康配的切換都是用這個??新增fragment 到時都要用他extends
 * ProjConstraintFragment==>用來繼承ProjBaseFragment
 */

@KeepName
@RequiresApi(VERSION_CODES.LOLLIPOP)
public final class CameraXPreviewFragment extends ProjConstraintFragment
        implements OnRequestPermissionsResultCallback,
        OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "CameraXLivePreview";

    private static final String OBJECT_DETECTION = "Object Detection";
    private static final String OBJECT_DETECTION_CUSTOM = "Custom Object Detection";
    private static final String CUSTOM_AUTOML_OBJECT_DETECTION =
            "Custom AutoML Object Detection (Flower)";
    private static final String FACE_DETECTION = "Face Detection";
    private static final String BARCODE_SCANNING = "Barcode Scanning";
    private static final String IMAGE_LABELING = "Image Labeling";
    private static final String IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)";
    private static final String CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)";
    private static final String POSE_DETECTION = "Pose Detection";
    private static final String SELFIE_SEGMENTATION = "Selfie Segmentation";
    private static final String TEXT_RECOGNITION_LATIN = "Text Recognition Latin";
    private static final String TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese (Beta)";
    private static final String TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari (Beta)";
    private static final String TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese (Beta)";
    private static final String TEXT_RECOGNITION_KOREAN = "Text Recognition Korean (Beta)";

    private static final String STATE_SELECTED_MODEL = "selected_model";

    private PreviewView previewView;
    private GraphicOverlay graphicOverlay;
    private ImageView captureView;

    @Nullable
    private ProcessCameraProvider cameraProvider;
    @Nullable
    private Preview previewUseCase;
    @Nullable
    private ImageAnalysis analysisUseCase;
    @Nullable
    private VisionImageProcessor imageProcessor;
    private boolean needUpdateGraphicOverlayImageSourceInfo;

    private String selectedModel = BARCODE_SCANNING;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private CameraSelector cameraSelector;

    private Handler handler;

    public static CameraXPreviewFragment newInstance() {
        CameraXPreviewFragment fragment = new CameraXPreviewFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityTitleRid = R.string.scan_pay;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_camerax_preview, container, false);

        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();

        handler = new Handler();

        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();

        previewView = rootView.findViewById(R.id.preview_view);
        if (previewView == null) {
            Log.d(TAG, "previewView is null");
        }
        graphicOverlay = rootView.findViewById(R.id.graphic_overlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        captureView = rootView.findViewById(R.id.iv_capture);
        captureView.setVisibility(View.INVISIBLE);

        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            Toast.makeText(
                            getContext(),
                            "CameraX is only supported on SDK version >=21. Current SDK version is "
                                    + VERSION.SDK_INT,
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindAllCameraUseCases();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(STATE_SELECTED_MODEL, selectedModel);
    }

    @Override
    public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedModel = parent.getItemAtPosition(pos).toString();
        Log.d(TAG, "Selected model: " + selectedModel);
        bindAnalysisUseCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (cameraProvider == null) {
            return;
        }
        int newLensFacing =
                lensFacing == CameraSelector.LENS_FACING_FRONT
                        ? CameraSelector.LENS_FACING_BACK
                        : CameraSelector.LENS_FACING_FRONT;
        CameraSelector newCameraSelector =
                new CameraSelector.Builder().requireLensFacing(newLensFacing).build();
        try {
            if (cameraProvider.hasCamera(newCameraSelector)) {
                Log.d(TAG, "Set facing to " + newLensFacing);
                lensFacing = newLensFacing;
                cameraSelector = newCameraSelector;
                bindAllCameraUseCases();
                return;
            }
        } catch (CameraInfoUnavailableException e) {
            // Falls through
        }
        Toast.makeText(
                        getContext(),
                        "This device does not have lens with facing: " + newLensFacing,
                        Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        bindAllCameraUseCases();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (imageProcessor != null) {
            imageProcessor.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageProcessor != null) {
            imageProcessor.stop();
        }
        MemberBean.channel_id = 3;
    }

    private void bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider.unbindAll();
            bindPreviewUseCase();
            bindAnalysisUseCase();
        }
    }

    private void bindPreviewUseCase() {
        if (!PreferenceUtils.isCameraLiveViewportEnabled(getContext())) {
            return;
        }
        if (cameraProvider == null) {
            return;
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }

        Preview.Builder builder = new Preview.Builder();
        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(getContext(), lensFacing);
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        previewUseCase = builder
                .setTargetResolution(new Size(displayMetrics.widthPixels, displayMetrics.widthPixels))
                .build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, previewUseCase);

    }

    private void bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }
        if (imageProcessor != null) {
            imageProcessor.stop();
        }

        try {
            switch (selectedModel) {
                case BARCODE_SCANNING:
                    Log.i(TAG, "Using Barcode Detector Processor");
                    imageProcessor = new BarcodeScannerProcessor(getContext(), new BarcodeScannerProcessor.BarcodeFoundListener() {
                        @Override
                        public void onFound(List<Barcode> barcodes) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Log.i(TAG, "Using Barcode Detector Processor" + barcodes);

                                    Bitmap bmp = previewView.getBitmap();
                                    captureView.setImageBitmap(bmp);
                                    captureView.setVisibility(View.VISIBLE);
                                    previewView.setVisibility(View.INVISIBLE);
                                    imageProcessor.stop();

                                    //cameraProvider.unbind(previewUseCase);
                                    cameraProvider.unbindAll();
                                    if (fragmentListener != null) {
//                                        if (MemberBean.barcode != null && MemberBean.barcode.startsWith("hpay") && MemberBean.barcode.contains("id=")) {
//                                            String str1 = MemberBean.barcode.substring(0, MemberBean.barcode.indexOf("id="));
//                                            String str2 = MemberBean.barcode.substring(str1.length() + 3, MemberBean.barcode.length());
//                                            Log.i(TAG, "str1fff" + str1);
//                                            Log.i(TAG, "str2fff" + str2);
//                                            MemberBean.barcode_id = str2;
//                                            CheckStoreInfo();
////                                            fragmentListener.onAction(FUNC_SCAN_TO_WEBPAY, null);
//                                        } else
//                                        if (MemberBean.barcode != null && MemberBean.barcode.startsWith("https://medicalec") && MemberBean.barcode.contains("sid=")) {
//                                        if (MemberBean.barcode != null && MemberBean.barcode.startsWith(ApiUrl.API_URL) && MemberBean.barcode.contains("sid="))
                                        if (MemberBean.barcode != null) {
                                            //https://medicalec.jotangi.net/medicalec/app-stores.php?sid=149
                                            String str1 = MemberBean.barcode.substring(0, MemberBean.barcode.indexOf("sid="));
                                            MemberBean.payUrl = str1;
                                            String str2 = MemberBean.barcode.substring(str1.length() + 4, MemberBean.barcode.length());
                                            Log.d(TAG, "MemberBean.barcode: " + MemberBean.barcode);
                                            Log.d(TAG, "str1: " + str1);
                                            Log.i(TAG, "str2" + str2);
                                            MemberBean.barcode_id = str2;
                                            CheckStoreInfo();
                                        } else {
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    CustomDaialog.showNormal(requireActivity(), "", "請掃描健康店家的專屬條碼", "確定", new CustomDaialog.OnBtnClickListener() {
                                                        @Override
                                                        public void onCheck() {

                                                        }

                                                        @Override
                                                        public void onCancel() {

                                                            requireActivity().onBackPressed();
                                                            CustomDaialog.closeDialog();

                                                        }
                                                    });
                                                }
                                            });
                                            return;

                                        }

                                    }

                                }
                            }, 100);

                        }
                    });
                    break;
                default:
                    throw new IllegalStateException("Invalid model name");
            }
        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: " + selectedModel, e);
            Toast.makeText(
                            getContext(),
                            "Can not create image processor: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        ImageAnalysis.Builder builder = new ImageAnalysis
                .Builder();
        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(getContext(), lensFacing);
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }
        analysisUseCase = builder
                .setTargetResolution(new Size(displayMetrics.widthPixels, displayMetrics.widthPixels))
                .build();

        needUpdateGraphicOverlayImageSourceInfo = true;
        analysisUseCase.setAnalyzer(
                // imageProcessor.processImageProxy will use another thread to run the detection underneath,
                // thus we can just runs the analyzer itself on main thread.
                ContextCompat.getMainExecutor(getContext()),
                imageProxy -> {
                    if (needUpdateGraphicOverlayImageSourceInfo) {
                        boolean isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT;
                        int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                        if (rotationDegrees == 0 || rotationDegrees == 180) {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getWidth(), imageProxy.getHeight(), isImageFlipped);
                        } else {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getHeight(), imageProxy.getWidth(), isImageFlipped);
                        }
                        needUpdateGraphicOverlayImageSourceInfo = false;
                    }
                    try {
                        imageProcessor.processImageProxy(imageProxy, graphicOverlay);
                    } catch (MlKitException e) {
                        Log.e(TAG, "Failed to process image. Error: " + e.getLocalizedMessage());
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, analysisUseCase);
    }

    private void CheckStoreInfo() {
        String sid = MemberBean.barcode_id;
        ApiConUtils.store_info(ApiUrl.API_URL, ApiUrl.store_info, MemberBean.member_id, MemberBean.member_pwd, sid, new ApiConUtils.OnConnect() {
            @Override
            public void onSuccess(String jsonString) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String channel_price = "";
                            Log.d("登入", "CheckStoreInfo()" + jsonString);
                            try {

                                JSONArray jsonArray = new JSONArray(jsonString);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = (JSONObject) jsonArray.get(i);
                                    channel_price = jo.getString("channel_price");
                                }
                                Bundle data = new Bundle();
                                if (channel_price.equals("1")) {
                                    MemberBean.channel_id = 2;

                                    // requireActivity().onBackPressed();
                                } else {
                                    MemberBean.channel_id = 1;
                                    // requireActivity().onBackPressed();
                                }
                            } catch (Exception e) {

                            }
                            if (requireActivity() != null && isAdded()) {
                                FragmentTransaction transaction =
                                        requireActivity().getSupportFragmentManager()
                                                .beginTransaction();
                                transaction.replace(R.id.nav_host_fragment_activity_main, HomeMainFragment.newInstance());
                                transaction.commit();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        });
    }
}
