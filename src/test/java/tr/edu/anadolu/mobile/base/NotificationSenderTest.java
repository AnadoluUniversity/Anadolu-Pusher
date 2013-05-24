package tr.edu.anadolu.mobile.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import tr.edu.anadolu.mobile.pusher.DeviceType;
import tr.edu.anadolu.mobile.pusher.ResultType;
import tr.edu.anadolu.mobile.pusher.WPType;
import tr.edu.anadolu.mobile.pusher.base.NotificationReceiver;
import tr.edu.anadolu.mobile.pusher.base.NotificationSender;
import tr.edu.anadolu.mobile.pusher.message.APNSConfig;
import tr.edu.anadolu.mobile.pusher.message.GCMConfig;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.message.WPConfig;
import tr.edu.anadolu.mobile.pusher.result.MessageResult;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;
import tr.edu.anadolu.mobile.pusher.sender.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Provides testing send method of NotificationSender class.
 * {@link NotificationSender}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({NotificationSender.class, APNSSender.class, MPNSTileSender.class, GCMSender.class})
public class NotificationSenderTest {

    /**
     * Tests what happens when notifications are sent to the three devices(Android, iOS, WindowsPhone devices) successfully.
     * @throws Exception
     */
    @Test
    public void send_WorksCorrect() throws Exception {

        NotificationSender sender = new NotificationSender();
        WPConfig wpConfig = new WPConfig("Notification", "Blue.jpg", "Message", "Red.jpg");
        wpConfig.setWpType(WPType.Tile);
        APNSConfig apnsConfig = new APNSConfig("Certificates.p12", "alanya", false);
        GCMConfig gcmConfig = new GCMConfig("AIzaSyCzYLxhh3Cn5Brfbm4Ifz7Iy9PqI2vMiWs");
        sender.setWpConfig(wpConfig);
        sender.setApnsConfig(apnsConfig);
        sender.setGcmConfig(gcmConfig);

        APNSSender mockAPNS = Mockito.mock(APNSSender.class);
        PowerMockito.whenNew(APNSSender.class).withArguments(apnsConfig).thenReturn(mockAPNS);
        GCMSender mockGCM = Mockito.mock(GCMSender.class);
        PowerMockito.whenNew(GCMSender.class).withArguments(gcmConfig).thenReturn(mockGCM);
        MPNSTileSender mockWP = Mockito.mock(MPNSTileSender.class);
        PowerMockito.whenNew(MPNSTileSender.class).withArguments(wpConfig).thenReturn(mockWP);

        //message details
        Map<String, String> messageData = new HashMap<String, String>();
        messageData.put("messageId", "1");

        //message receivers
        List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();
        NotificationReceiver receiver = new NotificationReceiver();
        receiver.setDeviceId("9d9eec0c668117c88b5d6763215e8dfdfcb2c4e4164ddd49409aef08f1f83fe3");
        receiver.setDeviceType(DeviceType.IOS);
        receiverList.add(receiver);

        NotificationReceiver secondReceiver = new NotificationReceiver();
        secondReceiver.setDeviceId("APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw");
        secondReceiver.setDeviceType(DeviceType.ANDROID);
        receiverList.add(secondReceiver);

        NotificationReceiver thirdReceiver = new NotificationReceiver();
        thirdReceiver.setDeviceId("http://db3.notify.live.net/throttledthirdparty/01.00/AAEQ3zzJQTTMR7EZEfGnDbmoAgAAAAADBAAAAAQUZm52OkJCMjg1QTg1QkZDMkUxREQ");
        thirdReceiver.setDeviceType(DeviceType.WP);
        receiverList.add(thirdReceiver);

        //message
        Message message = new Message(messageData, "This is a message.", 1, "default", receiverList);

        MessageResult actualResult = new MessageResult();
        List<ResultModel> actualResultModels = actualResult.getResults();
        actualResultModels.add(new ResultModel(ResultType.SUCCESSFUL, "9d9eec0c668117c88b5d6763215e8dfdfcb2c4e4164ddd49409aef08f1f83fe3"));
        actualResultModels.add(new ResultModel(ResultType.SUCCESSFUL, "APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw"));
        actualResultModels.add(new ResultModel(ResultType.SUCCESSFUL, "http://db3.notify.live.net/throttledthirdparty/01.00/AAEQ3zzJQTTMR7EZEfGnDbmoAgAAAAADBAAAAAQUZm52OkJCMjg1QTg1QkZDMkUxREQ"));
        actualResult.setResult(actualResultModels);

        List<ResultModel> expectedGCMResultModel= new ArrayList<ResultModel>();
        expectedGCMResultModel.add(new ResultModel(ResultType.SUCCESSFUL, "APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw"));
        List<ResultModel> expectedAPNSResultModel= new ArrayList<ResultModel>();
        expectedAPNSResultModel.add(new ResultModel(ResultType.SUCCESSFUL, "9d9eec0c668117c88b5d6763215e8dfdfcb2c4e4164ddd49409aef08f1f83fe3"));
        List<ResultModel> expectedMPNSResultModel= new ArrayList<ResultModel>();
        expectedMPNSResultModel.add(new ResultModel(ResultType.SUCCESSFUL, "http://db3.notify.live.net/throttledthirdparty/01.00/AAEQ3zzJQTTMR7EZEfGnDbmoAgAAAAADBAAAAAQUZm52OkJCMjg1QTg1QkZDMkUxREQ"));


        when(mockAPNS.sendNotification(message)).thenReturn(expectedAPNSResultModel);
        when(mockGCM.sendNotification(message)).thenReturn(expectedGCMResultModel);
        when(mockWP.sendNotification(message)).thenReturn(expectedMPNSResultModel);

        MessageResult result = sender.send(message);
        assertEquals(actualResult.getResults().get(0).getResultType(), result.getResults().get(0).getResultType());
        assertEquals(actualResult.getResults().get(1).getDeviceID(), result.getResults().get(1).getDeviceID());
        assertEquals(actualResult.getResults().get(2).getResultType(), result.getResults().get(2).getResultType());


    }

}
