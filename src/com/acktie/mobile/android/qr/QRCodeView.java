package com.acktie.mobile.android.qr;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.zbar.Symbol;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiFileHelper;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutArrangement;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.acktie.mobile.android.qr.camera.CameraManager;
import com.acktie.mobile.android.qr.camera.CameraCallback;
import com.acktie.mobile.android.qr.camera.CameraSurfaceView;

public class QRCodeView extends TiUIView {

	CameraSurfaceView cameraPreview = null;
	CameraManager cameraManager = null;
	

	private static int[] QR_CODE_SYMBOL = { Symbol.QRCODE };

	public QRCodeView(TiViewProxy proxy, final CameraManager cameraManager,
			InputArgs args) {
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

		CameraCallback cameraCallback = new CameraCallback(QR_CODE_SYMBOL,
				qrCodeViewProxy, cameraManager, args);
		cameraManager.setCameraCallback(cameraCallback);
		cameraPreview = new CameraSurfaceView(proxy.getActivity(),
				cameraCallback, cameraManager);

		layout.addView(cameraPreview);
		
		URL urlPath = getImageURLPath(args);

		if (urlPath != null) {
			try {
				URLConnection urlConnection = urlPath.openConnection();
				Bitmap myBitmap = BitmapFactory.decodeStream(urlConnection
						.getInputStream());
				ImageView imageView = new ImageView(proxy.getActivity());
				imageView.setImageBitmap(myBitmap);
				imageView.setAlpha(convertFloatToIntForAlpha(args.getAlpha()));
				layout.addView(imageView);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: Log?
			}
		}

		setNativeView(layout);
	}

	private URL getImageURLPath(InputArgs args) {
		String imageName = args.getImageName();
		URL url = null;
		if(imageName != null)
		{
			url = getClass().getResource("/assets/Resources/modules/com.acktie.mobile.android.qr/" + imageName);
		}
		else
		{
			String color = args.getColor();
			String layout = args.getLayout();
			
			if(color != null && layout != null)
			{
				url = getClass().getResource("/assets/" + layout + "-" + color + ".png");
			}
		}
		
		return url;
	}
	
	private int convertFloatToIntForAlpha(float floatAlpha)
	{
		float maxAlpha = 255.0f;
		
		return (int) (maxAlpha * floatAlpha);
		
	}
}
