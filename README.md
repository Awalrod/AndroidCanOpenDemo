# AndroidCanOpenDemo
A demo app that monitors a canopen network
##Synopsis[](TODO: Name the java-canopen package and provide links)
This app is part of a larger project to control and monitor canopen networks from the conveniance of a mobile device.  
A Java-CanOpen interface  listens to the network and updates the object dictionary accordingly.
The application listens to events fired by changes in the object dictionary and acts upon them. This app is meant to 
demonstrate a potential usage of the java-canopen package.  

##Network Setup[](TODO: Name busmaster and provide links)[](TODO: Get name of board being used)
We have a raspberry pi connected to two nodes sending temperature, voltage, and current data over a can network. A busmaster program iscontrolling the network from the raspberry pi. The raspberry pi is also running [socat](http://www.dest-unreach.org/socat/),a networking tool that is being used to send messages on wifi. The android app will no work correctly unless both thebusmaster and socat script are running correctly.

##Application Description
###The Main Screen
![main screen]( https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/appMainScreen.png )  

The main screen is 10 monitors in different tabs that can be set up to monitor different profiles. What profiles are and how to create/edit/delete them is covered in the [Settings](#settings). Clicking the option menu below the gauge shows a menu of all profiles that have been created. You can set which profile the gauge should monitor here.
![profile dropdown](https://github.com/Awalrod/AndroidCanOpenDemo/blob/master/images/appProfileDropdown.png)  



###Settings
