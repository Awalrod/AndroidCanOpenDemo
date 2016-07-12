# AndroidCanOpenDemo
A demo app that monitors a canopen network
##Synopsis
This app is part of a [larger project](https://github.com/mpcrowe/canopen-raspberrypi) to control and monitor canopen networks from the conveniance of a mobile device.  
A [Java-CanOpen interface](https://github.com/Awalrod/CanOpenJavaLibrary)  listens to the network and updates the object dictionary accordingly. The subentries and values in them are represented by [profiles](#profiles) within the app
The application listens to events fired by changes in the object dictionary and acts upon them. This app is meant to 
demonstrate a potential usage of the java-canopen package.  

##Installation
###If you have Android studio installed:  
1. Click File->New->Project from Vesion Control->GitHub 
2. Enter the correct info into the dialog box. 
![menu tree](https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/ImportFromVersionControl.png)  
 
![clone repository](https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/CloneRepository.png)
3. Attach your device via usb and run the app on the device. Your device must developer options turned on to work correctly.

###If you do not have android studio installed:
####Step 1: Acquire APK
1. Open a terminal and change directories to wherever you want to keep the `AndroidCanOpenDemo` directory
2. Type the following commands:
  `git clone https://github.com/Awalrod/AndroidCanOpenDemo`  
  `cd AndroidCanOpenDemo`
3. If you have the `ANDROID_HOME` environment variable already set, run `./gradlew assembleDebug`
4. If you do not have the `ANDROID_HOME` environment variable already set, you need to find the location of your SDK.  
  Once you've found it, one of the two following commands will work:
  `export ANDROID_HOME=/path/to/SDK`
  
  or
  
  `echo sdk.dir=/path/to/SDK > /path/to/AndroidCanOpenDemo/local.properties`  
 
  The second command will save the location.
  Run `./gradlew assembleDebug`  



####Step 2: Install APK
1. Locate APK at `AndroidCanOpenDemo/app/build/outputs/apk/app-debug.apk`  
2. Connect your Android device via usb and allow use as a mass storage device.
3. Move the APK file onto the Android Device.
4. On the android Device, use a file browser to locate and install the APK. You may have to install your own browser as some devices do not come with one.

Note: Third party apps must be enabled in your android settings
##Network Setup[](TODO: Name busmaster and provide links)[](TODO: Get name of board being used)
We have a raspberry pi connected to two nodes sending temperature, voltage, and current data over a can network. A busmaster program iscontrolling the network from the raspberry pi. The raspberry pi is also running [socat](http://www.dest-unreach.org/socat/),a networking tool that is being used to send messages on wifi. The android app will no work correctly unless both thebusmaster and socat script are running correctly.  

More detailed setup info can be found at https://github.com/mpcrowe/canopen-raspberrypi

##Application Description
###The Main Screen
![main screen]( https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/appMainScreen.png )  

The main screen is 10 monitors in different tabs that can be set up to monitor different profiles. What [profiles](#profiles) are and how to create/edit/delete them is covered in the [Settings](#settings).  
Clicking the option menu below the gauge shows a menu of all profiles that have been created. You can set which profile the gauge should monitor here.  

![profile dropdown](https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/appProfileDropdown.png)  

Slide left or right to view other monitors. Up to 10 different profiles may be monitored at once. Underneath the dropdown menu a label displays the integer value of the profile.  

The start and stop buttons make the monitor start or stop listening to the specified profile.

![listening to profile](https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/appMainListening.png)


###Settings  

![settings screen](https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/appSettingsScreen.png)  

The settings screen contains the controls for the profiles and network. The first two fields should contain the IP address socat is sending from and port number. Below that are the controls for profile management.

###Profiles
Profiles contain infor regarding the information that nodes will send. The Java-canopen interface will populate the object dictionary with information from these nodes. Profiles contain info about a specific subentry.  

![add profile](https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/appAddProfileDialog.png)

|Field   |Description|
|:------:|:----------|
|Name    |Chosen name for profile. Can be any alphanumeric character.|
|Index   |Index of Object Dictionary entry in hexadecimal.|
|Subindex|Subindex for subentry of the entry at the given Index. This contains the actual value you will use.|
|Min     |Minimum expected value. This is used to display the gauge.|
|Max     |Maximum expected value. This is used to display the gauge.|  

All of your current profiles are listed underneath the "Add Profile" button. To edit or delete and existing profile click the "Edit" or "Delete" buttong below the profile information.

#Links
Repository for the fuel gauge widget: https://github.com/tethridge/fuel-gauge  
Repository for the raspberry pi setup and code: https://github.com/mpcrowe/canopen-raspberrypi  
Repository for Java CanOpen code: https://github.com/Awalrod/CanOpenJavaLibrary
