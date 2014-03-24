package com.acktie.mobile.android.qr;

import org.appcelerator.kroll.KrollFunction;

import com.acktie.mobile.android.InputArgs;
import com.acktie.mobile.android.camera.CameraManager;

public class QRInputArgs implements InputArgs{
	
	public static final String USE_JIS_ENCODING = "useJISEncoding";
	private boolean useJISEncoding = false;
	
	public static final String SUCCESS_CALLBACK = "success";
	private KrollFunction successCallback = null;
	
	public static final String CANCEL_CALLBACK = "cancel";
	private KrollFunction cancelCallback = null;
	
	public static final String USE_FRONT_CAMERA = "useFrontCamera";
	private int cameraDevice = CameraManager.AUTO_DETACT_CAMERA_FACING;
	
	private static final boolean USE_ZBAR = true;

	public boolean isUseJISEncoding() {
		return useJISEncoding;
	}

	public void setUseJISEncoding(boolean useJISEncoding) {
		this.useJISEncoding = useJISEncoding;
	}

	public KrollFunction getSuccessCallback() {
		return successCallback;
	}

	public void setSuccessCallback(KrollFunction successCallback) {
		this.successCallback = successCallback;
	}

	public KrollFunction getCancelCallback() {
		return cancelCallback;
	}

	public void setCancelCallback(KrollFunction cancelCallback) {
		this.cancelCallback = cancelCallback;
	}

	public int getCameraDevice() {
		return cameraDevice;
	}

	public void setCameraDevice(int cameraDevice) {
		this.cameraDevice = cameraDevice;
	}

	@Override
	public boolean scanUsingZBar() {
		return USE_ZBAR;
	}
}
