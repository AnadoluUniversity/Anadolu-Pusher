package tr.edu.anadolu.mobile.pusher.sender;

import javapns.Push;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.*;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import tr.edu.anadolu.mobile.pusher.DeviceType;
import tr.edu.anadolu.mobile.pusher.ResultType;
import tr.edu.anadolu.mobile.pusher.base.NotificationReceiver;
import tr.edu.anadolu.mobile.pusher.message.APNSConfig;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;
import java.io.InputStream;
import java.util.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

/**
 * Provides testing sendNotification method of APNSSender class.
 * {@link APNSSender}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({APNSSender.class, Push.class, PushedNotification.class, PushedNotifications.class})
public class APNSSenderTest {
    private APNSConfig apnsConfig;
    private Message message;
    private PushNotificationPayload payload;

    /**
     * Provides initializing necessary attributes to perform tests.
     * This method is called before each test method.
     * @throws JSONException
     */
    @Before
    public void set() throws JSONException {
        apnsConfig = new APNSConfig("Certificates.p12", "alanya", false);
        List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();
        NotificationReceiver receiver = new NotificationReceiver();
        receiver.setDeviceId("9d9eec0c668117c88b5d6763215e8dfdfcb2c4e4164ddd49409aef08f1f83fe3");
        receiver.setDeviceType(DeviceType.IOS);
        receiverList.add(receiver);
        //message
        Map<String, String> messageData = new HashMap<String, String>();
        messageData.put("messageId", "1");
        message = new Message(messageData, "This is a message.", 1, "default", receiverList);

        payload = PushNotificationPayload.complex();

        payload.addAlert(message.getMessage());
        payload.addBadge(message.getBadgeNo());
        payload.addSound(message.getSound());

        Set<String> keySets = message.getMessageData().keySet();
        Object[] keySetList = keySets.toArray();
        for (Object aKeySetList : keySetList) {
            String keyName = (String) aKeySetList;
            payload.addCustomDictionary(keyName, message.getMessageData().get(keyName));
        }
    }

    /**
     * Tests what happens after sending a failed notification to an iOS device with an invalid device id.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenSendFail_ReturnsUnsuccessfulDelete() throws Exception {

        PushedNotifications notifications = new PushedNotifications();
        PushedNotification notification = new PushedNotification(new BasicDevice("9d9eec0c668117c88b5d6763215e8dfdfcb2c4e4164ddd49409aef08f1f83fe3"), payload, null);
        notifications.add(notification);

        PowerMockito.mockStatic(Push.class);
        Mockito.when(Push.payload(any(PushNotificationPayload.class),
                any(InputStream.class),
                eq(apnsConfig.getPassword()),
                eq(apnsConfig.getProduction()),
                eq(1),
                any(List.class))).thenReturn(notifications);

        APNSSender s = new APNSSender(apnsConfig);
        List<ResultModel> results = s.sendNotification(message);
        assertEquals(results.get(0).getResultType(), ResultType.UNSUCCESS_DELETE);
    }

    /**
     * Tests what happens after sending a failed notification to an iOS device.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenSendFail_ReturnsUnsuccessful() throws Exception {

        ResponsePacket responsePacketMock = Mockito.mock(ResponsePacket.class);
        PushedNotifications notifications = new PushedNotifications();
        PushedNotification notificationMock = Mockito.mock(PushedNotification.class);
        Mockito.when(notificationMock.isSuccessful()).thenReturn(false);
        Mockito.when(notificationMock.getDevice()).thenReturn(new BasicDevice("9d9eec0c668117c88b5d6763215e8dfdfcb2c4e4164ddd49409aef08f1f83fe3"));
        Mockito.when(notificationMock.getResponse()).thenReturn(responsePacketMock);

        notifications.add(notificationMock);
        PowerMockito.mockStatic(Push.class);
        Mockito.when(Push.payload(any(PushNotificationPayload.class),
                any(InputStream.class),
                eq(apnsConfig.getPassword()),
                eq(apnsConfig.getProduction()),
                eq(1),
                any(List.class))).thenReturn(notifications);

        APNSSender s = new APNSSender(apnsConfig);
        List<ResultModel> results = s.sendNotification(message);
        assertEquals(results.get(0).getResultType(), ResultType.UNSUCCESSFUL);

    }

    /**
     * Tests what happens after sending a successful notification to an iOS device.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenSendSuccess_ReturnsSuccessful() throws Exception {

        PushedNotifications notifications = new PushedNotifications();
        PushedNotification notificationMock = Mockito.mock(PushedNotification.class);
        Mockito.when(notificationMock.isSuccessful()).thenReturn(true);
        Mockito.when(notificationMock.getDevice()).thenReturn(new BasicDevice("9d9eec0c668117c88b5d6763215e8dfdfcb2c4e4164ddd49409aef08f1f83fe3"));
        notifications.add(notificationMock);

        PowerMockito.mockStatic(Push.class);
        Mockito.when(Push.payload(any(PushNotificationPayload.class),
                any(InputStream.class),
                eq(apnsConfig.getPassword()),
                eq(apnsConfig.getProduction()),
                eq(1),
                any(List.class))).thenReturn(notifications);

        APNSSender s = new APNSSender(apnsConfig);
        List<ResultModel> results = s.sendNotification(message);
        assertEquals(results.get(0).getResultType(), ResultType.SUCCESSFUL);

    }

}
