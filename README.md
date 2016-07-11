# AndroidCanOpenDemo
A demo app that monitors a canopen network
##Synopsis[](TODO: Name the java-canopen package and provide links)
This app is part of a larger project to control and monitor canopen networks from the conveniance of a mobile device.  
A Java-CanOpen interface  listens to the network and updates the object dictionary accordingly. The subentries and values in them are represented by [profiles](#profiles) within the app
The application listens to events fired by changes in the object dictionary and acts upon them. This app is meant to 
demonstrate a potential usage of the java-canopen package.  

##Network Setup[](TODO: Name busmaster and provide links)[](TODO: Get name of board being used)
We have a raspberry pi connected to two nodes sending temperature, voltage, and current data over a can network. A busmaster program iscontrolling the network from the raspberry pi. The raspberry pi is also running [socat](http://www.dest-unreach.org/socat/),a networking tool that is being used to send messages on wifi. The android app will no work correctly unless both thebusmaster and socat script are running correctly.

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
The fuel gauge widget was originally created in this repository: https://github.com/tethridge/fuel-gauge
