package com.acktie.mobile.android.qr.camera;

import java.nio.charset.Charset;
import java.util.HashMap;

import com.acktie.mobile.android.qr.InputArgs;
import com.acktie.mobile.android.qr.QRCodeViewProxy;
import com.acktie.mobile.android.qr.zbar.ZBarManager;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;

public class CameraCallback implements PreviewCallback {

	private CameraManager cameraManager = null;
	private ImageScanner scanner = null;
	private QRCodeViewProxy viewProxy = null;
	private InputArgs args = null;
	private long lastQRDetected = System.currentTimeMillis();
	private boolean pictureTaken = false;

	public CameraCallback(int[] symbolsToScan,
			QRCodeViewProxy viewProxy, CameraManager cameraManager, InputArgs args) {
		
		/* Instance barcode scanner */
		scanner = ZBarManager.getImageScannerInstance(symbolsToScan);
		this.cameraManager = cameraManager;
		this.viewProxy = viewProxy;
		this.args = args;
	}
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// These could have been || (or'ed) together but I wanted to make it easier
		// to understand why the image was not processed
		if (hasEnoughTimeElapsedToScanNextImage()) {
			return;
		} else if (args.isScanQRFromImageCapture() && !pictureTaken) {
			return;
		}
		
		scanImageForQR(data, camera, pictureTaken);
		
		pictureTaken = false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void scanImageForQR(byte[] data, Camera camera, boolean fromPictureTaken)
	{
		if(cameraManager.isStopped()) {
			return;
		}
		
		Camera.Parameters parameters = cameraManager.getCameraParameters();
		
		// If null, likely called after camera has been released.
		if(parameters == null)
		{
			return;
		}
		
		Size size = parameters.getPreviewSize();

		// Supported image formats
		// http://sourceforge.net/apps/mediawiki/zbar/index.php?title=Supported_image_formats
		Image barcode = ZBarManager.getImageInstance(size.width, size.height,
				ZBarManager.Y800, data);

		int result = scanner.scanImage(barcode);

		int quality = 0;
		Symbol symbol = null;

		if (result != 0) {
			SymbolSet syms = scanner.getResults();
			for (Symbol sym : syms) {
				System.out.println("Quality of Scan (Higher than 0 is good): "
						+ sym.getQuality());
				if (sym.getQuality() > quality) {
					symbol = sym;
				}
			}

			if (viewProxy != null && symbol != null) {

				Charset cs = Charset.forName("UTF-8");;
				String resultData = new String(symbol.getData().getBytes(), cs);
				
				if(args.isUseJISEncoding())
				{
					cs = Charset.forName("Shift_JIS");
					resultData = new String(symbol.getData().getBytes(), cs);
				}

				System.out.println(resultData);

				if(!args.isContinuous())
				{
					cameraManager.stop();
				}

				HashMap results = new HashMap();

				results.put("data", resultData);
				results.put("type", symbol.getType());

				viewProxy.successCallback(results);

				lastQRDetected = getOneSecondFromNow();
			}
		}
		else if(fromPictureTaken)
		{
			viewProxy.errorCallback();
		}
	}
	
	private boolean hasEnoughTimeElapsedToScanNextImage() {
		return lastQRDetected > System.currentTimeMillis();
	}
	
	private long getOneSecondFromNow() {
		return System.currentTimeMillis() + 3000;
	}

	public void setPictureTaken(boolean pictureTaken) {
		this.pictureTaken = pictureTaken;
	}
}
