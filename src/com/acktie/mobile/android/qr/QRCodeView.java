package com.acktie.mobile.android.qr;

import net.sourceforge.zbar.Symbol;

import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutArrangement;
import org.appcelerator.titanium.view.TiUIView;

import android.content.pm.ActivityInfo;

import com.acktie.mobile.android.camera.CameraCallback;
import com.acktie.mobile.android.camera.CameraManager;
import com.acktie.mobile.android.camera.CameraSurfaceView;

public class QRCodeView extends TiUIView {

	CameraSurfaceView cameraPreview = null;
	CameraManager cameraManager = null;
	

	private static int[] QR_CODE_SYMBOL = { Symbol.QRCODE };

	public QRCodeView(TiViewProxy proxy, final CameraManager cameraManager,
			QRInputArgs args) {
		super(proxy);
		this.cameraManager = cameraManager;
		final QRCodeViewProxy qrCodeViewProxy = (QRCodeViewProxy) proxy;
		LayoutArrangement arrangement = LayoutArrangement.DEFAULT;

		if (proxy.hasProperty(TiC.PROPERTY_LAYOUT)) {
			String layoutProperty = TiConvert.toString(proxy
					.getProperty(TiC.PROPERTY_LAYOUT));
			if (layoutProperty.equals(TiC.LAYOUT_HORIZONTAL)) {
				arrangement = LayoutArrangement.HORIZONTAL;
			} else if (layoutProperty.equals(TiC.LAYOUT_VERTICAL)) {
				arrangement = LayoutArrangement.VERTICAL;
			}
		}

		proxy.getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		TiCompositeLayout layout = new TiCompositeLayout(proxy.getActivity(),
				arrangement);

		CameraCallback cameraCallback = new CameraCallback(QR_CODE_SYMBOL, qrCodeViewProxy, cameraManager, args);
		cameraManager.setCameraCallback(cameraCallback);
		cameraPreview = new CameraSurfaceView(proxy.getActivity(), cameraCallback, cameraManager);

		layout.addView(cameraPreview);
		setNativeView(layout);
	}
}
