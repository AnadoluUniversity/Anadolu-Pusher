package tr.edu.anadolu.mobile.pusher.sender;


import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
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
import tr.edu.anadolu.mobile.pusher.message.GCMConfig;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.result.MessageResult;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;
import java.util.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;

/**
 * Provides testing sendNotification method of GCMSender class.
 * {@link GCMSender}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GCMSender.class,Sender.class,MulticastResult.class,Result.class})
public class GCMSenderTest {
    private GCMConfig gcmConfig;
    private Sender sMock;
    Message message;
    com.google.android.gcm.server.Message gcmMessage;

    /**
     * Provides initializing necessary attributes to perform tests.
     * This method is called before each test method.
     * @throws Exception
     */
    @Before
    public void set() throws Exception {
        sMock = Mockito.mock(Sender.class);
        gcmConfig = new GCMConfig("AIzaSyCzYLxhh3Cn5Brfbm4Ifz7Iy9PqI2vMiWs");
        PowerMockito.whenNew(Sender.class).withArguments(gcmConfig.getAPIKey()).thenReturn(sMock);

        Map<String, String> messageData = new HashMap<String, String>();
        messageData.put("messageId", "1");
        //message receivers
        List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();

        NotificationReceiver receiver = new NotificationReceiver();
        receiver.setDeviceId("APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw");
        receiver.setDeviceType(DeviceType.ANDROID);
        receiverList.add(receiver);

        message = new Message(messageData, "This is a message.", 1, "default", receiverList);

        com.google.android.gcm.server.Message.Builder builder = new com.google.android.gcm.server.Message.Builder();

        Set<String> keySets = message.getMessageData().keySet();
        Object[] keySetList = keySets.toArray();
        for (Object aKeySetList : keySetList) {
            String keyName = (String) aKeySetList;
            builder.addData(keyName, message.getMessageData().get(keyName));
        }

        builder.addData("message", message.getMessage());
        builder.addData("badgeNo", message.getBadgeNo() + "");
        builder.addData("sound", message.getSound());
        gcmMessage = builder.build();

    }

    /**
     * Tests what happens after sending a successful notification to an Android device.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenPushingFail_ReturnsSuccessful() throws Exception {

        MulticastResult multicastResultMock = PowerMockito.mock(MulticastResult.class);

        Result resultMock = PowerMockito.mock(Result.class);
        PowerMockito.when(resultMock.getMessageId()).thenReturn("");
        PowerMockito.when(resultMock.getCanonicalRegistrationId()).thenReturn(null);

        List<Result> expectedResults = new ArrayList<Result>();
        expectedResults.add(resultMock);

        MessageResult actualResult = new MessageResult();
        List<ResultModel> actualResultModels = actualResult.getResults(); actualResultModels.add(new ResultModel(ResultType.SUCCESSFUL, "APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw"));
        actualResult.setResult(actualResultModels);

        PowerMockito.when(multicastResultMock.getResults()).thenReturn(expectedResults);
        Mockito.when(sMock.send(any(com.google.android.gcm.server.Message.class),anyList(),eq(1))).thenReturn(multicastResultMock);
        GCMSender sender = new GCMSender(gcmConfig);
        List<ResultModel> results = sender.sendNotification(message);
        assertEquals(results.get(0).getResultType(),ResultType.SUCCESSFUL);

    }

    /**
     * Tests what happens after sending a successful notification to an Android if GCM sends a canonical id in the response.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenPushingFail_ReturnsSuccessfulUpdate() throws Exception {

        MulticastResult multicastResultMock = PowerMockito.mock(MulticastResult.class);

        Result resultMock = PowerMockito.mock(Result.class);
        PowerMockito.when(resultMock.getMessageId()).thenReturn("");
        PowerMockito.when(resultMock.getCanonicalRegistrationId()).thenReturn("APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXl");

        List<Result> expectedResults = new ArrayList<Result>();
        expectedResults.add(resultMock);


        MessageResult actualResult = new MessageResult();
        List<ResultModel> actualResultModels = actualResult.getResults(); actualResultModels.add(new ResultModel(ResultType.SUCCESSFUL, "APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw"));
        actualResult.setResult(actualResultModels);

        PowerMockito.when(multicastResultMock.getResults()).thenReturn(expectedResults);
        PowerMockito.when(sMock.send(any(com.google.android.gcm.server.Message.class),anyList(),eq(1))).thenReturn(multicastResultMock);
        GCMSender sender = new GCMSender(gcmConfig);
        List<ResultModel> results = sender.sendNotification(message);
        assertEquals(results.get(0).getResultType(),ResultType.SUCCESS_UPDATE);

    }

    /**
     * Tests what happens after sending a failed notification to an Android device.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenPushingFail_ReturnsUnSuccessful() throws Exception {

        MulticastResult multicastResultMock = PowerMockito.mock(MulticastResult.class);
        Result resultMock = PowerMockito.mock(Result.class);
        PowerMockito.when(resultMock.getMessageId()).thenReturn(null);
        PowerMockito.when(resultMock.getErrorCodeName()).thenReturn(Constants.ERROR_UNAVAILABLE);

        List<Result> expectedResults = new ArrayList<Result>();
        expectedResults.add(resultMock);

        MessageResult actualResult = new MessageResult();
        List<ResultModel> actualResultModels = actualResult.getResults(); actualResultModels.add(new ResultModel(ResultType.SUCCESSFUL, "APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw"));
        actualResult.setResult(actualResultModels);
        PowerMockito.when(multicastResultMock.getResults()).thenReturn(expectedResults);
        PowerMockito.when(sMock.send(any(com.google.android.gcm.server.Message.class),anyList(),eq(1))).thenReturn(multicastResultMock);
        GCMSender sender = new GCMSender(gcmConfig);
        List<ResultModel> results = sender.sendNotification(message);
        assertEquals(results.get(0).getResultType(),ResultType.UNSUCCESSFUL);

    }

    /**
     * Tests what happens after sending a failed notification to an Android device with an invalid device id.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenPushingFail_ReturnsUnSuccessfulDelete() throws Exception {

        MulticastResult multicastResultMock = PowerMockito.mock(MulticastResult.class);
        Result resultMock = PowerMockito.mock(Result.class);
        PowerMockito.when(resultMock.getMessageId()).thenReturn(null);
        PowerMockito.when(resultMock.getErrorCodeName()).thenReturn(Constants.ERROR_INVALID_REGISTRATION);

        List<Result> expectedResults = new ArrayList<Result>();
        expectedResults.add(resultMock);


        MessageResult actualResult = new MessageResult();
        List<ResultModel> actualResultModels = actualResult.getResults(); actualResultModels.add(new ResultModel(ResultType.SUCCESSFUL, "APA91bGW-fYWUi-H91hFyvmZFANudYZOt3SbkOS4dewCX0FhQ4kMNTnU6kCuAM5tq1HkpJxSjn-Swnf6B6dX-qCDSvHgRDfi15xp3MPMuH41xIdSaqmsMK9FAzXczvQjRU3ZsNfMz_ll2r0KG2KTjIop8j-Ker5fXw"));
        actualResult.setResult(actualResultModels);

        PowerMockito.when(multicastResultMock.getResults()).thenReturn(expectedResults);
        PowerMockito.when(sMock.send(any(com.google.android.gcm.server.Message.class),anyList(),eq(1))).thenReturn(multicastResultMock);
        GCMSender sender = new GCMSender(gcmConfig);
        List<ResultModel> results = sender.sendNotification(message);
        assertEquals(results.get(0).getResultType(),ResultType.UNSUCCESS_DELETE);

    }
}
