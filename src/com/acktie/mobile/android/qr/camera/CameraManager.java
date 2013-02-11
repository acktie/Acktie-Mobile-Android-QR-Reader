package com.acktie.mobile.android.qr.camera;

import java.util.List;

import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.kroll.common.Log;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Handler;

public class CameraManager {
	private static final String LCAT = "AcktiemobileandroidqrModule:CameraManager";
	private static final boolean DBG = TiConfig.LOGD;

	public static final int AUTO_DETACT_CAMERA_FACING = 99;

	private CameraCallback cameraCallback = null;
	private Camera camera = null;
	private int cameraDevice = AUTO_DETACT_CAMERA_FACING;
	private boolean isStopped = true;
	private boolean torchOn = false;
	private Handler autoFocusHandler = null;

	public CameraManager(CameraCallback cameraCallback, int cameraDevice) {
		this(cameraDevice);
		this.cameraCallback = cameraCallback;
		this.cameraDevice = cameraDevice;
	}

	public CameraManager(int cameraDevice) {
		this.cameraDevice = cameraDevice;
	}

	// http://stackoverflow.com/questions/5540981/picture-distorted-with-camera-and-getoptimalpreviewsize
	public Camera.Size getBestPreviewSize(Camera camera, int width, int height) {
		Log.d(LCAT, "width: " + width);
		Log.d(LCAT, "heigth: " + height);

		Camera.Size result = null;
		Camera.Parameters parameters = getCameraParameters();

		Log.d(LCAT, "parameters: " + parameters);

		// If null, likely called after camera has been released.
		if (parameters != null) {
			Log.d(LCAT, "# of Supported Preview Sizes: "
					+ parameters.getSupportedPreviewSizes().size());
			for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
				Log.d(LCAT, "Size: " + size.width + ", " + size.height);
				if (size.width <= width && size.height <= height) {
					if (result == null) {
						result = size;
					} else {
						int resultArea = result.width * result.height;
						int newArea = size.width * size.height;

						if (newArea > resultArea) {
							result = size;
						}
					}
				}
			}
		}

		Log.d(LCAT, "result: " + result);
		return result;
	}

	public void toggleTorch() {
		if (!isStopped) {
			if (torchOn) {
				turnOffTorch();
			} else {
				turnOnTorch();
			}
		}
	}

	public void turnOnTorch() {
		Camera.Parameters parameters = getCameraParameters();

		if (parameters != null) {
			List<String> flashModes = parameters.getSupportedFlashModes();

			if (flashModes != null) {
				if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				}
				camera.setParameters(parameters);
				torchOn = true;
			}
		}
	}

	public void turnOffTorch() {
		Camera.Parameters parameters = getCameraParameters();

		if (parameters != null) {
			List<String> flashModes = parameters.getSupportedFlashModes();
			if (flashModes != null
					&& flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}
			camera.setParameters(parameters);
			torchOn = false;
		}
	}

	public void enableAutoFocus() {
		Camera.Parameters parameters = getCameraParameters();

		if (parameters != null) {
			List<String> focusModes = parameters.getSupportedFocusModes();

			if (focusModes != null
					&& focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				Log.d(LCAT, "FOCUS_MODE_AUTO supported");
				camera.autoFocus(autoFocusCB);
			} else {
				Log.d(LCAT, "FOCUS_MODE_AUTO NOT supported");
			}
		}
	}

	public void stop() {
		if (!isStopped) {
			turnOffTorch();
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			isStopped = true;
		}
	}

	public Camera getCamera(int cameraDevice) {
		int numOfCameras = Camera.getNumberOfCameras();
		if (numOfCameras > 0) {
			Log.d(LCAT, numOfCameras + " cameras has been found.");
			if (cameraDevice == AUTO_DETACT_CAMERA_FACING) {
				camera = Camera.open(0);
			} else {
				int cameraId = getCameraId(cameraDevice);

				if (cameraId != -1) {
					camera = Camera.open(cameraId);
				} else {
					// If user specified a camera facing that the device does
					// not have ignore preference and
					// open the first camera

					camera = Camera.open(0);
				}
			}
			isStopped = false;
		}

		return camera;
	}

	/**
	 * Will return the camera id for the specified cameraFacing See
	 * Camera.CameraInfo for camera facing int. If 99 is specified then it will
	 * auto-detect the first camera.
	 * 
	 * @param cameraFacing
	 * @return
	 */
	public int getCameraId(int cameraDevice) {
		int cameraId = -1;
		int numOfCameras = Camera.getNumberOfCameras();

		// This method will only return the first found camera
		for (int i = 0; i < numOfCameras; i++) {
			Camera.CameraInfo cInfo = new Camera.CameraInfo();
			Camera.getCameraInfo(i, cInfo);

			if (cInfo.facing == AUTO_DETACT_CAMERA_FACING
					|| cInfo.facing == cameraDevice) {
				cameraId = i;
				break;
			}
		}

		return cameraId;
	}

	public void takePicture() {
		if (isStopped) {
			return;
		} else if (cameraCallback == null) {
			Log.d(LCAT,
					"Must pass CameraManager a CameraCallback before calling takePicture.");
			return;
		}

		cameraCallback.setPictureTaken(true);
	}

	public boolean isStopped() {
		return isStopped;
	}

	public void setCameraCallback(CameraCallback cameraCallback) {
		this.cameraCallback = cameraCallback;
	}

	public Camera.Parameters getCameraParameters() {
		Camera.Parameters parameters = null;
		try {
			parameters = camera.getParameters();
		} catch (RuntimeException re) {
			// Ignoring exception. It will be thrown if the camera has been
			// released by another thread.
		}

		return parameters;
	}

	// Mimic continuous auto-focusing
	private AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			if (autoFocusHandler == null) {
				autoFocusHandler = new Handler();
			}

			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (!isStopped) {
				camera.autoFocus(autoFocusCB);
			}
		}
	};

	public Camera getCamera() {
		return getCamera(cameraDevice);
	}
}
