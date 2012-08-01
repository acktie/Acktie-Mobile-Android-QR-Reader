package com.acktie.mobile.android.qr.zbar;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

public class ZBarManager {

	private static final int ALL_SYMBOLS[] = 
	{
	    Symbol.NONE,
	    Symbol.PARTIAL,
	    Symbol.EAN8,
	    Symbol.UPCE,
	    Symbol.ISBN10,
	    Symbol.UPCA,
	    Symbol.EAN13,
	    Symbol.ISBN13,
	    Symbol.I25,
	    Symbol.DATABAR,
	    Symbol.DATABAR_EXP,
	    Symbol.CODE39,
	    Symbol.PDF417,
	    Symbol.QRCODE,
	    Symbol.CODE93,
	    Symbol.CODE128,
	    Symbol.CODABAR,
	};
	private static ImageScanner scanner = null;
	
	// Supported image formats -- http://sourceforge.net/apps/mediawiki/zbar/index.php?title=Supported_image_formats
	// TODO: Add the rest of the support image formats
	public static final String Y800 = "Y800";
	public static final String JPEG = "JPEG";
	
	public ZBarManager() {
		// TODO Auto-generated constructor stub
	}

	public static ImageScanner getImageScannerInstance(int[] symbolsToScan)
	{
		if(scanner != null)
		{
			return scanner;
		}
		
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);
		
		disableAllSybmols(ALL_SYMBOLS);
		enableAllSybmols(symbolsToScan);
		
		return scanner;
	}
	
	public static void destoryImageScanner()
	{
		if(scanner != null)
		{
			scanner.destroy();
			scanner = null;
		}
	}
	
	public static Image getImageInstance(int width, int height, String format, byte[] data)
	{
		Image imageBarcode = new Image(width, height, format);
		imageBarcode.setData(data);
		
		return imageBarcode;
	}
	
	private static void disableAllSybmols(int[] sybmols)
	{
		for (int i = 0; i < sybmols.length; i++) {
			scanner.setConfig(sybmols[i], Config.ENABLE, 0);
		}
	}
	
	private static void enableAllSybmols(int[] sybmols)
	{
		for (int i = 0; i < sybmols.length; i++) {
			scanner.setConfig(sybmols[i], Config.ENABLE, 1);
		}
	}
}
