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
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIView;

import com.acktie.mobile.android.camera.CameraManager;

import android.app.Activity;
import android.hardware.Camera;

/**
 * @author TNuzzi
 *
 */
@Kroll.proxy(creatableInModule = AcktiemobileandroidqrModule.class)
public class QRCodeViewProxy extends TiViewProxy {
	private static final String LCAT = "QRCodeViewProxy";
	private CameraManager cameraManager = null;
	private QRInputArgs args = null;
	
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
		
		args = new QRInputArgs();
		
		if (hasProperty(QRInputArgs.SUCCESS_CALLBACK)) {
			args.setSuccessCallback((KrollFunction) getProperty(QRInputArgs.SUCCESS_CALLBACK));
		}
		if (hasProperty(QRInputArgs.CANCEL_CALLBACK)) {
			args.setCancelCallback((KrollFunction) getProperty(QRInputArgs.CANCEL_CALLBACK));
		}
		if (hasProperty(QRInputArgs.USE_JIS_ENCODING)) {
			args.setUseJISEncoding(TiConvert.toBoolean(getProperty(QRInputArgs.USE_JIS_ENCODING)));
		}
		if (hasProperty(QRInputArgs.USE_FRONT_CAMERA)) {
			if(TiConvert.toBoolean(getProperty(QRInputArgs.USE_FRONT_CAMERA)))
			{
				args.setCameraDevice(Camera.CameraInfo.CAMERA_FACING_FRONT);
			}
			else
			{
				args.setCameraDevice(Camera.CameraInfo.CAMERA_FACING_BACK);
			}
		}
	}
	
	@Kroll.method
	public void toggleLight()
	{
		cameraManager.toggleTorch();
	}
	
	@Kroll.method
	public void turnLightOn()
	{
		cameraManager.turnOnTorch();
	}
	
	@Kroll.method
	public void turnLightOff()
	{
		cameraManager.turnOffTorch();
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
	
	@Kroll.method
	public void stop()
	{
		cancelCallback();
		cameraManager.stop();
	}
}
