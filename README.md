#ANADOLU PUSHER
<br>
## Overview
<br>	
Anadolu pusher is an open source maven project that provides its users to push notifications over __Google Cloud Messaging, Apple Push Notification Service and Microsoft Push Notification Service__ protocols.

It makes easier to push notifications by preventing users to know details about each specific protocol.
<br>
## Requirements
<br>
To use Anadolu Pusher for sending notifications over **APNS** protocol, you must allows related ports to communicate with host addresses **gateway.sandbox.push.apple.com** for development and **gateway.push.apple.com** for production and the port **2195**. 

If Anadolu Pusher is used in a project that runs on an application server, to send notifications over **GCM** protocol, you should upload **Google Certificate** to the aplication server. 

<br>
## Code Examples
<br>

	NotificationSender sender = new NotificationSender();
	
    WPConfig wpConfig = new WPConfig("Notification", "Blue.jpg", "Message", "Red.jpg");
    wpConfig.setWpType(WPType.Tile);
    
    APNSConfig apnsConfig = new APNSConfig('__Path of the certificate file with .p12 extension__' , '__the password to use the certificate file__', '__true for production, false for development__');
    
    GCMConfig gcmConfig = newGCMConfig('__API KEY__');
    
    sender.setWpConfig(wpConfig);
    sender.setApnsConfig(apnsConfig);
    sender.setGcmConfig(gcmConfig);
    
    List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();
    NotificationReceiver receiver = new NotificationReceiver();
    receiver.setDeviceId('__DEVICE ID for an iOS DEVICE__');
    receiver.setDeviceType(DeviceType.IOS);
    receiverList.add(receiver);

    NotificationReceiver secondReceiver = new NotificationReceiver();
    secondReceiver.setDeviceId('__DEVICE ID for an ANDROID DEVICE__');
    secondReceiver.setDeviceType(DeviceType.ANDROID);
    receiverList.add(secondReceiver);

    NotificationReceiver thirdReceiver = new NotificationReceiver();
    thirdReceiver.setDeviceId('__URL for a WINDOWS PHONE DEVICE__');
    thirdReceiver.setDeviceType(DeviceType.WP);
    receiverList.add(thirdReceiver);

	Map<String, String> messageData = new HashMap<String, String>();
    messageData.put("messageId", "1");
    Message message = new Message(messageData, "This is a message.", 1, "default", receiverList);
    
    MessageResult result = sender.send(message);
 
## Acknowledgement
<br>
[JavaPNS](https://code.google.com/p/javapns/)<br>
<br>
## Creators
<br>
[Zeynep Özdemir twitter](https://twitter.com/zeynep_ozdmr_ce)
[İbrahim Esen github](https://github.com/iesen)<br>
[Erk Ekin github](https://github.com/erkekin), [twitter](https://twitter.com/erkekin)<br>
