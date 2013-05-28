#ANADOLU PUSHER

## Overview
<br>	<center><img src="http://ios.anadolu.edu.tr/opensource/Anadolu-Pusher/aplogo.png"><br><br>
Anadolu pusher is an open source maven project that provides its users to push notifications over __Google Cloud Messaging, Apple Push Notification Service and Microsoft Push Notification Service__ protocols.

It makes easier to push notifications by preventing developers to deal with details about each specific protocol.
<br>
## Requirements
<br>
In order to use Anadolu Pusher to send notifications over **APNS** protocol, you should allow related ports in Firewall to communicate with host addresses **gateway.sandbox.push.apple.com** for development and **gateway.push.apple.com** for production and the port **2195**. 

Also, to communicate with **APNS**, you must create a certificate file for an iOS application. It provides pushing notifications to the specific application specified by the certificate file.

Steps for creating a certificate file:<br>
1. Login to iOS Provisioning Portal.<br>
2. Activate the push notification option on your app by going to the 'App IDs' section. Click on the 'Configure' link of you app.<br>
3. Check the 'Enable for Apple Push Notification service' and click on the 'Configure' link of the production push.<br>
4. Launch the Keychain application on your Mac, choose the 'Certificate Assistant' entry in the menu, and then 'Request a certificate'.<br>
5. Fill in the fields required and save the file on your drive. Quit the Keychain application.<br>
6. Go back the 'Provisioning portal', choose the file you just saved on your drive, and click on the 'Generate' button. <br>
7. Press the 'Continue' button and then download the file. Double click on the downloaded file: it will launch the Keychain application again.<br>
8. Select the new line certificate and key, then right click and export both as 'certificate-push.p12'. You have now generated your certificate push notification .p12 file!<br>

For detailed information, please visit [APNS](http://developer.apple.com/library/mac/#documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/Introduction.html#//apple_ref/doc/uid/TP40008194-CH1-SW1).  

If Anadolu Pusher is used in a project that is running on an application server, in order to send notifications over **GCM** protocol, you should upload **Google Certificate** to the aplication server. 

In order to push notifications to an Android device, both server side and client side applications must introduce themselves to **GCM**. For this purpose, API Key and SenderId must be provided. API Key is used by server side application to push notifications to an application specified by the key. SenderId is used by client side application to get notifications from a server specified by SenderId.

For detailed information about getting API Key and SenderId, please visit [GCM](http://developer.android.com/google/gcm/gs.html).

For detailed information about pushing notifications to Windows Phone devices, please visit [MPNS](http://msdn.microsoft.com/en-us/library/windowsphone/develop/ff402558(v=vs.105\).aspx).
<br>
## Code Examples
<br>
There is a sample code to push notifications to Android, iOS and Windows Phone devices.

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

There is a sample code pushing notifications only to Android devices.
	
	NotificationSender sender = new NotificationSender();
	GCMConfig gcmConfig = newGCMConfig('__API KEY__');
    sender.setGcmConfig(gcmConfig);
    
    List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();
	NotificationReceiver receiver = new NotificationReceiver();
    receiver.setDeviceId('__DEVICE ID for an ANDROID DEVICE__');
    receiver.setDeviceType(DeviceType.ANDROID);
    receiverList.add(receiver);

	Map<String, String> messageData = new HashMap<String, String>();
    messageData.put("messageId", "1");
    Message message = new Message(messageData, "This is a message.", 1, "default", receiverList);
    
    MessageResult result = sender.send(message);
    
There is a sample code pushing notifications only to iOS devices.

	NotificationSender sender = new NotificationSender();
    
    APNSConfig apnsConfig = new APNSConfig('__Path of the certificate file with .p12 extension__' , '__the password to use the certificate file__', '__true for production, false for development__');
    sender.setApnsConfig(apnsConfig);
    
    List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();
    NotificationReceiver receiver = new NotificationReceiver();
    receiver.setDeviceId('__DEVICE ID for an iOS DEVICE__');
    receiver.setDeviceType(DeviceType.IOS);
    receiverList.add(receiver);

	Map<String, String> messageData = new HashMap<String, String>();
    messageData.put("messageId", "1");
    Message message = new Message(messageData, "This is a message.", 1, "default", receiverList);
    
    MessageResult result = sender.send(message);
    
There is a sample code pushing notifications only to Windows Phone devices.

	NotificationSender sender = new NotificationSender();
	
    WPConfig wpConfig = new WPConfig("Notification", "Blue.jpg", "Message", "Red.jpg");
    wpConfig.setWpType(WPType.Tile);
    sender.setWpConfig(wpConfig);
    
    List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();
    NotificationReceiver receiver = new NotificationReceiver();
    receiver.setDeviceId('__URL for a WINDOWS PHONE DEVICE__');
    receiver.setDeviceType(DeviceType.WP);
    receiverList.add(receiver);

	Map<String, String> messageData = new HashMap<String, String>();
    messageData.put("messageId", "1");
    Message message = new Message(messageData, "This is a message.", 1, "default", receiverList);
    
    MessageResult result = sender.send(message);

##Communication
For any question or suggestion, please send an email to **mobile(at)anadolu(dot)edu(dot)tr**.
 
## Credits<br>
Anadolu Pusher's logo was designed by [berkaey](http://berkaey.com).<br>
[JavaPNS](https://code.google.com/p/javapns/)<br>
## Creators
<br>
[Zeynep Özdemir github](https://github.com/zozdemir)<br>
[İbrahim Esen github](https://github.com/iesen)<br>
[Erk Ekin github](https://github.com/erkekin), [twitter](https://twitter.com/erkekin)<br>
