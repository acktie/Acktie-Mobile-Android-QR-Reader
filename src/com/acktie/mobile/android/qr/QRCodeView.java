package com.acktie.mobile.android.qr;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.zbar.Symbol;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutArrangement;

import android.content.Context;
import android.content.pm.ActivityInfo;
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
			boolean useJISEncoding, boolean continuous,
			boolean scanQRFromImageCapture) {
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
				qrCodeViewProxy, cameraManager, useJISEncoding, continuous,
				scanQRFromImageCapture);
		cameraManager.setCameraCallback(cameraCallback);
		cameraPreview = new CameraSurfaceView(proxy.getActivity(),
				cameraCallback, cameraManager);

		/*
		 * TiCompositeLayout.LayoutParams previewLayoutParams = new
		 * TiCompositeLayout.LayoutParams(); previewLayoutParams.optionWidth =
		 * new TiDimension("100%", TiDimension.TYPE_WIDTH);
		 * previewLayoutParams.optionHeight = new TiDimension("90%",
		 * TiDimension.TYPE_HEIGHT); previewLayoutParams.optionTop = new
		 * TiDimension(0, TiDimension.TYPE_TOP);
		 * 
		 * Button closeButton = new Button(proxy.getActivity());
		 * closeButton.setText("Close"); TiCompositeLayout.LayoutParams
		 * closeButtonLayoutParams = new TiCompositeLayout.LayoutParams();
		 * closeButtonLayoutParams.optionBottom = new TiDimension("1%",
		 * TiDimension.TYPE_BOTTOM); closeButtonLayoutParams.optionLeft = new
		 * TiDimension("1%", TiDimension.TYPE_LEFT);
		 * 
		 * closeButton.setOnClickListener(new View.OnClickListener() { public
		 * void onClick(View v) { qrCodeViewProxy.closeCallback(); } });
		 * 
		 * 
		 * ToggleButton lightSwitch = new ToggleButton(proxy.getActivity());
		 * TiCompositeLayout.LayoutParams lightSwitchLayoutParams = new
		 * TiCompositeLayout.LayoutParams();
		 * lightSwitchLayoutParams.optionBottom = new TiDimension("1%",
		 * TiDimension.TYPE_BOTTOM); lightSwitchLayoutParams.optionRight = new
		 * TiDimension("1%", TiDimension.TYPE_RIGHT);
		 * 
		 * lightSwitch.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() { public void
		 * onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		 * cameraManager.toggleTorch(); } });
		 * 
		 * layout.addView(lightSwitch, lightSwitchLayoutParams);
		 * layout.addView(closeButton, closeButtonLayoutParams);
		 * layout.addView(cameraPreview, previewLayoutParams);
		 */

		layout.addView(cameraPreview);
		// layout.addView(new DrawOnTop(proxy.getActivity()));
		URL urlPath = getClass().getResource("/assets/Center-Blue.png");

		if (urlPath != null) {
			try {
				URLConnection urlConnection = urlPath.openConnection();
				Bitmap myBitmap = BitmapFactory.decodeStream(urlConnection
						.getInputStream());
				ImageView imageView = new ImageView(proxy.getActivity());
				imageView.setImageBitmap(myBitmap);
				layout.addView(imageView);
			} catch (IOException e) {
				// Log?
			}
		}

		setNativeView(layout);
	}

	public void processProperties(KrollDict d) {
		super.processProperties(d);
	}

	class DrawOnTop extends View {

		public DrawOnTop(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub

			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLACK);
			canvas.drawText("Test Text", 10, 10, paint);

			super.onDraw(canvas);
		}

	}
}
