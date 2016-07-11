# AndroidCanOpenDemo
A demo app that monitors a canopen network
##Synopsis[](TODO: Name the java-canopen package and provide links)
This app is part of a larger project to control and monitor canopen networks from the conveniance of a mobile device.  
A Java-CanOpen interface  listens to the network and updates the object dictionary accordingly.
The application listens to events fired by changes in the object dictionary and acts upon them. This app is meant to 
demonstrate a potential usage of the java-canopen package.  

##Network Setup[](TODO: Name busmaster and provide links)[](TODO: Get name of board being used)
We have a raspberry pi connected to two nodes sending temperature, voltage, and current data. A busmaster program is
controlling the network from the raspberry pi. The raspberry pi is also running [socat](http://www.dest-unreach.org/socat/),
a networking tool that is being used to send messages on wifi. The android app will no work correctly unless both the
busmaster and socat script are running correctly.

