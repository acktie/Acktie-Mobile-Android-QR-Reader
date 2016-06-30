# Acktie Mobile QR Reader Module (Android)

***THIS FORK WAS REBUILT WITH THE FIXES FROM THE LATEST Acktie-Zbar-Android PROJECT***

To use this as the Android QR Scanning module in a titanium project, unzip [dist/com.acktie.mobile.android.qr-android-2.1.zip](https://github.com/jkotchoff/Acktie-Mobile-Android-QR-Reader/blob/master/dist/com.acktie.mobile.android.qr-android-2.1.zip?raw=true) into your titanium android modules directory and use it as per the instructions at [Acktie-Mobile-Android-QR-Reader](https://github.com/acktie/Acktie-Mobile-Android-QR-Reader).

This fork addresses stability issues documented at:
[issues/12](https://github.com/acktie/Acktie-Mobile-Android-QR-Reader/issues/12)
[issues/6](https://github.com/acktie/Acktie-Mobile-Android-QR-Reader/issues/6)
[issues/2](https://github.com/acktie/Acktie-Mobile-Android-QR-Reader/issues/2)

Those issues were resolved in the latest [commits](https://github.com/acktie/Acktie-Zbar-Android/commits/master) to the [Acktie-Zbar-Android](https://github.com/acktie/Acktie-Zbar-Android) project

ie. This addresses Android QR crashes like:
```
[ERROR] AndroidRuntime: FATAL EXCEPTION: main
[ERROR] AndroidRuntime: Process: com.geoplus.qrapp, PID: 18765
[ERROR] AndroidRuntime: java.lang.RuntimeException: autoFocus failed
[ERROR] AndroidRuntime:   at android.hardware.Camera.native_autoFocus(Native Method)
[ERROR] AndroidRuntime:   at android.hardware.Camera.autoFocus(Camera.java:1347)
[ERROR] AndroidRuntime:   at com.acktie.mobile.android.camera.CameraManager$2.run(CameraManager.java:232)
[ERROR] AndroidRuntime:   at android.os.Handler.handleCallback(Handler.java:739)


[ERROR] AndroidRuntime: FATAL EXCEPTION: main
[ERROR] AndroidRuntime: Process: com.geoplus.qrapp, PID: 31643
[ERROR] AndroidRuntime: java.lang.RuntimeException: Camera is being used after Camera.release() was called
[ERROR] AndroidRuntime:   at android.hardware.Camera.native_autoFocus(Native Method)
[ERROR] AndroidRuntime:   at android.hardware.Camera.autoFocus(Camera.java:1347)
[ERROR] AndroidRuntime:   at com.acktie.mobile.android.camera.CameraManager$2.run(CameraManager.java:232)
[ERROR] AndroidRuntime:   at android.os.Handler.handleCallback(Handler.java:739)
[ERROR] AndroidRuntime:   at android.os.Handler.dispatchMessage(Handler.java:95)
[ERROR] AndroidRuntime:   at android.os.Looper.loop(Looper.java:158)

[ERROR] AndroidRuntime: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.hardware.Camera.setPreviewCallback(android.hardware.Camera$PreviewCallback)' on a null object reference
[ERROR] AndroidRuntime:   at com.acktie.mobile.android.camera.CameraSurfaceView.surfaceCreated(CameraSurfaceView.java:87)
[ERROR] AndroidRuntime:   at android.view.SurfaceView.updateWindow(SurfaceView.java:712)
[ERROR] AndroidRuntime:   at android.view.SurfaceView.onWindowVisibilityChanged(SurfaceView.java:316)
```

## Example

A working example of how to use this module can be found on Github at
[https://github.com/acktie/Acktie-Mobile-QR-Example](https://github.com/acktie
/Acktie-Mobile-QR-Example).

## Description

This module allows for a quick integration of a QR reader into your
Appcelerator Mobile application. This QR reading ability comes in two scanning
modes.

  * Scan from Camera Feed
  * Read image from Camera image capture (User manually clicks scan QR button)
Additionally, both the Camera Feed and Camera Image capture (Image Capture
Android Only) has the ability to provide an overlay on the feed. Several
colors and layout are provide by default with the module. Documented below are
a list of the preloaded overlays **NOTE**: This module was developed using a
Nexus S, LG Optimus Elite, and Nexus 7 tablet. Android versions: 2.3.3, 4.0.4,
4.1.1, and 4.2 **NOTE**: The current version of the Android QR module does not
support the equivalent iOS feature of scanning an image from the Android Image
Gallary. We discovered that it did not provide a good user experience on lower
end Android device.

## Accessing the Acktie Mobile QR Module

To get started, review the [module install instructions](http://docs.appcelera
tor.com/titanium/2.0/#!/guide/Using_Titanium_Modules) on the Appcelerator
website. To access this module from JavaScript, you would do the following:

    
    var qrreader = require("com.acktie.mobile.android.qr"); 

The qr reader variable is a reference to the Module object.

## Reference

The following are the Javascript functions you can call with the module.

### Callback

The following are the callbacks used within this module. The callbacks are set
on the "createQRCodeView" function.

  * success - Called in the event of a successful scan. Callback data - This callback returns the data of the QR Code scan.
Example:

    
    function success(data){ var qrData = data.data; }; 

  * cancel - Called if the user clicks the cancel button.
  * error - Called if the scan was not successful in reading the QR code. (Only called from Image Capture)
NOTE: Both cancel and error do not return data.

### createQRCodeView

This function returns a view (TIView) that scans a QR code from the live
Camera Feed. Unlike the iOS version, this single view will be used for the
automatic QR Code detection as well as the manual user capture detection.
Since this view is based off the [Titanium.UI.View](http://docs.appcelerator.c
om/titanium/2.1/index.html#!/api/Titanium.UI.View) you can the majority of
Titanium.UI.View to set the size and look of the window. NOTE: We have not
tested all the view options against the QRCodeView but the basic ones we used
(size and position) have work fine. Example of Scanning from Camera without
overlay:

    
    var options = { // ** Android QR Reader properties (ignored by iOS) backgroundColor : 'black', width : '100%', height : '90%', top : 0, left : 0, // ** // ** Used by both iOS and Android success : success, cancel : cancel, error : error, }; createQRCodeView(options); 

Example of Scanning from Camera with overlay:

    
    var options = { // ** Android QR Reader properties (ignored by iOS) backgroundColor : 'black', width : '100%', height : '90%', top : 0, left : 0, // ** // ** Used by both iOS and Android overlay : { color : "blue", layout : "center", alpha : .75 }, success : success, cancel : cancel, error : error, }; createQRCodeView(options); 

#### Valid options for createQRCodeView

#### Valid overlay options

The following are the value JSON values for overlay.

#### color (optional):

  * blue
  * purple
  * red
  * yellow

#### layout (optional):

  * full
  * center
NOTE: Both color and layout must be specified together.

#### imageName (optional):

Use this property if you want to use your own overlay image. See the customize
overlay section for more details.

#### alpha (optional):

A float value between 0 - 1. 0 being fully transparent and 1 being fully
visible. Example: alpha: 0.5 // half transparent

#### Valid additional options for createQRCodeView

#### scanQRFromImageCapture (optional):

Use this property to indicate to the View to NOT auto-detect the QR Code. This
property is used in conjuction with the scanQR() function. Scans a QR code
from an image taken from the Camera. The user will have to manually click scan
(scanQR()) for the QR Code to be scanned. Example:

    
    var options = { // ** Android QR Reader properties (ignored by iOS) backgroundColor : 'black', width : '100%', height : '90%', top : 0, left : 0, scanQRFromImageCapture : true, // ** // ** Used by both iOS and Android scanButtonName : 'Scan Code!', success : success, cancel : cancel, error : error, }; var qrCodeView = createQRCodeView(options); ... var scanQRCode = Titanium.UI.createButton({ title : options.scanButtonName, bottom : 0, left : '40%' }); scanQRCode.addEventListener('click', function() { qrCodeView.scanQR(); }); 

#### continuous (optional):

This feature will continuously scan for QR codes even after one has been
detected. The user will have to click the a button (stop()) to exit the QR
scan screen. With each QR code that is detected the "success" event will be
triggers so you program will be able to process each QR code. Also, the
application can use the phone virate feature to indicate a scan took place.
See example app.js for details. Additionally, if this property is not used the
Camera will automatically stop scanning. Example: continuous: true, By default
this value is false.

#### useFrontCamera (optional):

This option is used to enable the camera view to use the front facing camera.
If the option is set to true and no front camera exist then the first camera
found will be used. NOTE: Most (if not all), front facing cameras are a fixed
focus camera that will not auto-focus on an object. This can result in a lower
read success rate for scanning in low light. Take this into consideration when
developing your app. Example: useFrontCamera: true, By default this value is
false and to the back camera.

#### useJISEncoding (optional):

This option is used to try decode the QR code result with the Shift JIS
encoding. This is necessary when decoding Kanji Characters and UTF-8 is not
sufficient. By default the QR code is decoded in UTF-8. For most circumstances
UTF-8 will work fine. Example: useJISEncoding: true, By default this value is
false.

#### Valid functions for createQRCodeView object

#### toggleLight (Function on createQRCodeView)

This function can be used to control the light on the camera. Example:

    
    ... var qrCodeView = createQRCodeView(options); var lightToggle = Ti.UI.createSwitch({ value : false, bottom : 0, right : 0 }); lightToggle.addEventListener('change', function() { qrCodeView.toggleLight(); }) 

#### scanQR (Function on createQRCodeView)

This function is used to manually scan an image and used in conjunction with
the property scanQRFromImageCapture. This function will most likely be in the
addEventListener for a button. For example see "scanQRFromImageCapture".

#### stop (Function on createQRCodeView)

This function is used to stop the Camera in the QR View and release all Camera
resources. Additionally, the module will call the "cancel" callback (if
passed). If the user has an option to navigate away from the QR View it is
advised you call the "stop" function. Just call "createQRCodeView" to re-
enable the QR View. Example:

    
    qrCodeView = qrreader.createQRCodeView(options); var closeButton = Titanium.UI.createButton({ title : "close", bottom : 0, left : 0 }); closeButton.addEventListener('click', function() { qrCodeView.stop(); }); 

## Customize Overlay

In order to customize the overlay you will need to do 2 things:

  * Create a directory under your mobile app's "Resources" directory called "modules/com.acktie.mobile.android.qr". This is the directory where you will put your custom images.
  * Use the property "imageName" in the createQRCodeView arguments (see above).
Example:

    
    var options = { // ** Android QR Reader properties (ignored by iOS) backgroundColor : 'black', width : '100%', height : '90%', top : 0, left : 0, // ** // ** Used by both iOS and Android overlay: { imageName: "myOverlay.png", alpha: 0.35f }, success : success, cancel : cancel, error : error, }; var qrCodeView = createQRCodeView(options); 

NOTE: Specifying an imageName will override any color/layout that is also
specified in the same overlay property. Meaning, when they are both specified
imageName will take precedence. However, alpha works on both regardless of
what is used (color/layout or imageName). Included in the example/images
subdirectory is an example Photoshop file and .png files.

## Known Issues:

The core QR Code reader uses UTF-8 to decode the QR code. There have been
instances where characters have been mis translated (this only applies to
Chinses, Japanses, and German characters). To ensure your QR code is
transalated correctly. It is advised that you encode your QR Codes with a
UTF-8 Byte order mark (BOM). Here is of an example of using Kaywa to specify
the UTF-8 BOM. http://qr.kaywa.com/img.php?s=8&amp;d=%EF%BB%BF Example: http://qr.
kaywa.com/img.php?s=8&amp;d=%EF%BB%BF{%22name%22:%22%E7%8E%89%E7%B1%B3%22}

### Default Images (ones that come with the module) are not showing up

It seems there is a bug in the build process where on a non-full build the
module assests are not being copied over into the .apk. As a result, the
module will not display the overlays that come with the module. Currently, the
only way to fix this situation is to force a "full build" (e.g. delete the
build/android directory or modify the tiapp.xml (add something save, remove it
save)). NOTE: This issue does not affect the custom images (e.g. imageName)
you provide in your mobile project. Also, there is no adverse affect to your
mobile application if the images are missing (other than the overlays not
showing up).

## Change Log

  * 1.0 Initial Release
  * 1.1 Fixed an auto-focus issue with certain Android phones
  * 1.2 Fixed an issue with HTC Desire phones
  * 1.3 Added Front Camera Support. Additional testing with Nexus 7 tablets.

## Author

Tony Nuzzi @ Acktie 
Twitter: @Acktie 
Email: support@acktie.com

Code licensed under Apache License v2.0, documentation under CC BY 3.0.

Libaries Used:

Portions of this software utilize the ZBar bar code reader:
  For more information you can go to: http://zbar.sourceforge.net/

Attribution is welcome but not required.
