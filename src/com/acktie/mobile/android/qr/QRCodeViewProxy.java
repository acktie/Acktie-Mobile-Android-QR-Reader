/**
 * 
 */
package com.acktie.mobile.android.qr;

import java.util.HashMap;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIView;

import com.acktie.mobile.android.qr.camera.CameraManager;

import android.app.Activity;

/**
 * @author TNuzzi
 *
 */
@Kroll.proxy(creatableInModule = AcktiemobileandroidqrModule.class)
public class QRCodeViewProxy extends TiViewProxy {
	private static final String LCAT = "QRCodeViewProxy";
	private static final boolean DBG = TiConfig.LOGD;
	private CameraManager cameraManager = null;
	
	private static final String CONTINUOUS = "continuous";
	private boolean continuous = false;
	
	private static final String SCAN_FROM_IMAGE_CAPTURE = "scanQRFromImageCapture";
	private boolean scanQRFromImageCapture = false;
	
	private static final String USE_JIS_ENCODING = "useJISEncoding";
	private boolean useJISEncoding = false;
	
	private static final String SUCCESS_CALLBACK = "success";
	private KrollFunction successCallback = null;
	
	private static final String CANCEL_CALLBACK = "cancel";
	private KrollFunction cancelCallback = null;
	
	private static final String ERROR_CALLBACK = "error";
	private KrollFunction errorCallback = null;
	
	/**
	 * 
	 */
	public QRCodeViewProxy() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.appcelerator.titanium.proxy.TiViewProxy#createView(android.app.Activity)
	 */
	@Override
	public TiUIView createView(Activity arg0) {
		Log.d(LCAT, "Creating QRCodeView");
		cameraManager = new CameraManager();
		TiUIView view = new QRCodeView(this, cameraManager, useJISEncoding, continuous, scanQRFromImageCapture);
		view.getLayoutParams().autoFillsHeight = true;
		view.getLayoutParams().autoFillsWidth = true;
		return view;
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options) 
	{
		super.handleCreationDict(options);
		
		if (hasProperty(SUCCESS_CALLBACK)) {
			successCallback = (KrollFunction) getProperty(SUCCESS_CALLBACK);
		}
		if (hasProperty(CANCEL_CALLBACK)) {
			cancelCallback = (KrollFunction) getProperty(CANCEL_CALLBACK);
		}
		if (hasProperty(ERROR_CALLBACK)) {
			errorCallback = (KrollFunction) getProperty(ERROR_CALLBACK);
		}
		if (hasProperty(CONTINUOUS)) {
			continuous = TiConvert.toBoolean(getProperty(CONTINUOUS));
		}
		if (hasProperty(USE_JIS_ENCODING)) {
			useJISEncoding = TiConvert.toBoolean(getProperty(USE_JIS_ENCODING));
		}
		if (hasProperty(SCAN_FROM_IMAGE_CAPTURE)) {
			scanQRFromImageCapture = TiConvert.toBoolean(getProperty(SCAN_FROM_IMAGE_CAPTURE));
		}
	}
	
	@Kroll.method
	public void toggleLight()
	{
		cameraManager.toggleTorch();
	}
	
	@Kroll.method
	public void scanQR()
	{
		cameraManager.takePicture();
	}
	
	@SuppressWarnings("rawtypes")
	public void successCallback(HashMap results)
	{
		if(successCallback != null)
		{
			successCallback.callAsync(getKrollObject(), results);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void cancelCallback()
	{
		cameraManager.stop();
		
		if(cancelCallback != null)
		{
			cancelCallback.callAsync(getKrollObject(), new HashMap());
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void errorCallback()
	{
		if(errorCallback != null)
		{
			errorCallback.callAsync(getKrollObject(), new HashMap());
		}
	}
	
	@Kroll.method
	public void stop()
	{
		cancelCallback();
		cameraManager.stop();
	}
}
