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
import android.hardware.Camera;

/**
 * @author TNuzzi
 *
 */
@Kroll.proxy(creatableInModule = AcktiemobileandroidqrModule.class)
public class QRCodeViewProxy extends TiViewProxy {
	private static final String LCAT = "QRCodeViewProxy";
	private static final boolean DBG = TiConfig.LOGD;
	private CameraManager cameraManager = null;
	private InputArgs args = null;
	
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
		cameraManager = new CameraManager(args.getCameraDevice());
		TiUIView view = new QRCodeView(this, cameraManager, args);
		view.getLayoutParams().autoFillsHeight = true;
		view.getLayoutParams().autoFillsWidth = true;
		return view;
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options) 
	{
		super.handleCreationDict(options);
		
		args = new InputArgs();
		
		if (hasProperty(InputArgs.SUCCESS_CALLBACK)) {
			args.setSuccessCallback((KrollFunction) getProperty(InputArgs.SUCCESS_CALLBACK));
		}
		if (hasProperty(InputArgs.CANCEL_CALLBACK)) {
			args.setCancelCallback((KrollFunction) getProperty(InputArgs.CANCEL_CALLBACK));
		}
		if (hasProperty(InputArgs.ERROR_CALLBACK)) {
			args.setErrorCallback((KrollFunction) getProperty(InputArgs.ERROR_CALLBACK));
		}
		if (hasProperty(InputArgs.CONTINUOUS)) {
			args.setContinuous(TiConvert.toBoolean(getProperty(InputArgs.CONTINUOUS)));
		}
		if (hasProperty(InputArgs.USE_JIS_ENCODING)) {
			args.setUseJISEncoding(TiConvert.toBoolean(getProperty(InputArgs.USE_JIS_ENCODING)));
		}
		if (hasProperty(InputArgs.SCAN_FROM_IMAGE_CAPTURE)) {
			args.setScanQRFromImageCapture(TiConvert.toBoolean(getProperty(InputArgs.SCAN_FROM_IMAGE_CAPTURE)));
		}
		if (hasProperty(InputArgs.USE_FRONT_CAMERA)) {
			if(TiConvert.toBoolean(getProperty(InputArgs.USE_FRONT_CAMERA)))
			{
				args.setCameraDevice(Camera.CameraInfo.CAMERA_FACING_FRONT);
			}
			else
			{
				args.setCameraDevice(Camera.CameraInfo.CAMERA_FACING_BACK);
			}
		}
		if (hasProperty(InputArgs.OVERLAY)) {
			HashMap overlay = (HashMap) getProperty(InputArgs.OVERLAY);
			
			if(overlay.containsKey(InputArgs.OVERLAY_COLOR))
			{
				args.setColor((String) overlay.get(InputArgs.OVERLAY_COLOR));
			}
			if(overlay.containsKey(InputArgs.OVERLAY_LAYOUT))
			{
				args.setLayout((String) overlay.get(InputArgs.OVERLAY_LAYOUT));
			}
			if(overlay.containsKey(InputArgs.OVERLAY_IMAGE_NAME))
			{
				args.setImageName((String) overlay.get(InputArgs.OVERLAY_IMAGE_NAME));
			}
			if(overlay.containsKey(InputArgs.OVERLAY_ALPHA))
			{
				args.setAlpha(TiConvert.toFloat(overlay.get(InputArgs.OVERLAY_ALPHA)));
			}
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
		if(args.getSuccessCallback() != null)
		{
			args.getSuccessCallback().callAsync(getKrollObject(), results);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void cancelCallback()
	{
		cameraManager.stop();
		
		if(args.getCancelCallback() != null)
		{
			args.getCancelCallback().callAsync(getKrollObject(), new HashMap());
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void errorCallback()
	{
		if(args.getErrorCallback() != null)
		{
			args.getErrorCallback().callAsync(getKrollObject(), new HashMap());
		}
	}
	
	@Kroll.method
	public void stop()
	{
		cancelCallback();
		cameraManager.stop();
	}
}
