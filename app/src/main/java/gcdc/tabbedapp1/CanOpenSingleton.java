package gcdc.tabbedapp1;


import com.gcdc.can.Driver;
import com.gcdc.can.DriverManager;
import com.gcdc.canopen.CanOpen;
import com.gcdc.canopen.DefaultOD;
import com.gcdc.canopen.ObjectDictionary;

/**
 * Created by gcdc on 6/22/16.
 */


/**
 * Creates a CanOpen instance and serves it to whatever class requests it
 * IP address and Port number must be set before creating an instance
 * Currently(7/11/16) MainActivity is setting IP and Port to a default as soon as it is created
 */
public class CanOpenSingleton {
    private static CanOpen instance = null;
    private static String ip;
    private static Integer port;


    public static CanOpen getInstance() throws NullPointerException{
        if(instance == null){
            createNewInstance(ip,port);
        }
        return instance;
    }
    public static void setIpAndPort(String ip, int port){
        CanOpenSingleton.ip = ip;
        CanOpenSingleton.port = port;
    }
    private static void createNewInstance(String ipAddress, Integer portNum){
        System.out.println(ipAddress+":"+Integer.toString(portNum));
        DriverManager dm = new DriverManager("Datagram",ipAddress,portNum,true);
        System.out.println("Got driver manager");
        Driver driver =dm.getDriver();
        System.out.println("Got driver");
        ObjectDictionary objDict = DefaultOD.create(0x22);//The node ID being used
        instance = new CanOpen(driver,objDict,0x22,true);
        System.out.println("Got Canopen");
    }

}
