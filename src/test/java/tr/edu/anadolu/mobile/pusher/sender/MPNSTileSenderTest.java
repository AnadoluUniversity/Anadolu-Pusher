package tr.edu.anadolu.mobile.pusher.sender;

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
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.message.WPConfig;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Provides testing sendOneNotification method of MPNSTileSender class.
 * {@link MPNSTileSender}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MPNSTileSender.class, URLConnection.class, URL.class, OutputStreamWriter.class})
public class MPNSTileSenderTest {

    private WPConfig wpConfig;
    private Message message;

    /**
     * Provides initializing necessary attributes to perform tests.
     * This method is called before each test method.
     *
     * @throws Exception
     */
    @Before
    public void set() throws Exception {
        wpConfig = new WPConfig("Notification", "Blue.jpg", "Message", "Red.jpg");
        Map<String, String> messageData = new HashMap<String, String>();
        messageData.put("messageId", "1");
        //message receivers
        List<NotificationReceiver> receiverList = new ArrayList<NotificationReceiver>();

        NotificationReceiver secondReceiver = new NotificationReceiver();
        secondReceiver.setDeviceId("http://db3.notify.live.net/throttledthirdparty/01.00/AAEQ3zzJQTTMR7EZEfGnDbmoAgAAAAADAQAAAAQUZm52OkJCMjg1QTg1QkZDMkUxREQ");
        secondReceiver.setDeviceType(DeviceType.ANDROID);
        receiverList.add(secondReceiver);

        message = new Message(messageData, "This is a message.", 1, "default", receiverList);
    }

    /**
     * Tests what happens after sending an unsuccessful tile notification to a Windows Phone device with an invalid device id.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenPushingFail_ReturnsUnsuccessfulDelete() throws Exception {

        URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments(message.getMessageReceivers().get(0).getDeviceId()).thenReturn(mockUrl);
        URLConnection mockConn = PowerMockito.mock(URLConnection.class);
        when(mockUrl.openConnection()).thenReturn(mockConn);
        PowerMockito.when(mockConn.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int i) throws IOException {

            }
        });
        String expectedNotificationStatus = "N/A";
        String expectedChannelStatus = "Expired";
        when(mockConn.getHeaderField("X-NotificationStatus")).thenReturn(expectedNotificationStatus);
        when(mockConn.getHeaderField("X-SubscriptionStatus")).thenReturn(expectedChannelStatus);

        ResultModel actualResultModel = new MPNSTileSender(wpConfig).sendOneNotification(message);
        assertEquals(actualResultModel.getResultType(), ResultType.UNSUCCESS_DELETE);
    }

    /**
     * Tests what happens after sending an unsuccessful tile notification to a Windows Phone device.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenPushingFail_ReturnsUnsuccessful() throws Exception {
        URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments(message.getMessageReceivers().get(0).getDeviceId()).thenReturn(mockUrl);
        URLConnection mockConn = Mockito.mock(URLConnection.class);
        when(mockUrl.openConnection()).thenReturn(mockConn);
        PowerMockito.when(mockConn.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int i) throws IOException {

            }
        });
        String expectedNotificationStatus = "N/A";
        String expectedChannelStatus = "Active";
        when(mockConn.getHeaderField("X-NotificationStatus")).thenReturn(expectedNotificationStatus);
        when(mockConn.getHeaderField("X-SubscriptionStatus")).thenReturn(expectedChannelStatus);

        ResultModel actualResultModel = new MPNSTileSender(wpConfig).sendOneNotification(message);
        assertEquals(actualResultModel.getResultType(), ResultType.UNSUCCESSFUL);
    }

    /**
     * Tests what happens after sending an successful tile notification to a Windows Phone device.
     * @throws Exception
     */
    @Test
    public void sendNotification_WhenPushingSuccess_ReturnsSuccessful() throws Exception {
        URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments(message.getMessageReceivers().get(0).getDeviceId()).thenReturn(mockUrl);
        URLConnection mockConn = Mockito.mock(URLConnection.class);
        when(mockUrl.openConnection()).thenReturn(mockConn);
        PowerMockito.when(mockConn.getOutputStream()).thenReturn(new OutputStream() {
            @Override
            public void write(int i) throws IOException {

            }
        });
        String expectedNotificationStatus = "Received";
        String expectedChannelStatus = "Active";
        when(mockConn.getHeaderField("X-NotificationStatus")).thenReturn(expectedNotificationStatus);
        when(mockConn.getHeaderField("X-SubscriptionStatus")).thenReturn(expectedChannelStatus);

        ResultModel actualResultModel = new MPNSTileSender(wpConfig).sendOneNotification(message);
        assertEquals(actualResultModel.getResultType(), ResultType.SUCCESSFUL);
    }
}
