package tr.edu.anadolu.mobile.pusher.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.edu.anadolu.mobile.pusher.ResultType;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.message.WPConfig;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Provides sending messages to Windows Phone devices with MPNS protocol as tile notifications.
 */
public class MPNSTileSender extends MPNSSender {

    private static final Logger logger = LoggerFactory.getLogger(MPNSTileSender.class);
    private WPConfig wpConfig;

    public MPNSTileSender(WPConfig wpConfig) {
        this.wpConfig = wpConfig;
    }

    /**
     * Sends specified message to only one device, which is described in the message object, over MPNS protocol as tile notification.
     * Returns the results of  each sending notification process.
     *
     * @param message the message object will be pushed.
     * @return a ResultModel object.
     *         The ResultModel object represents a status of a pushed notification and  a device id for a device that notification was sent to.
     */
    @Override
    public ResultModel sendOneNotification(Message message) {

        ResultModel resultModel = null;

        URL url;
        try {
            url = new URL(message.getMessageReceivers().get(0).getDeviceId());

            URLConnection uc = url.openConnection();
            uc.setRequestProperty("ContentType", "text/xml");
            uc.setRequestProperty("X-WindowsPhone-Target", "token");
            uc.setRequestProperty("X-NotificationClass", "1");
            uc.setDoOutput(true);

            String tileMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<wp:Notification xmlns:wp=\"WPNotification\">" +
                    "<wp:Tile>" +
                    "<wp:BackgroundImage>" + wpConfig.getBackGroundImage() + "</wp:BackgroundImage>" +
                    "<wp:Count>" + message.getBadgeNo() + "</wp:Count>" +
                    "<wp:Title>" + wpConfig.getTitle() + "</wp:Title>" +
                    "<wp:BackBackgroundImage>" + wpConfig.getBackBackGroundImage() + "</wp:BackBackgroundImage>" +
                    "<wp:BackTitle>" + wpConfig.getBackTitle() + "</wp:BackTitle>" +
                    "<wp:BackContent>" + message.getMessage() + "</wp:BackContent>" +
                    "<wp:Sound>" + message.getSound() + "</wp:Sound>";

            Set<String> keySets = message.getMessageData().keySet();
            Object[] keySetList = keySets.toArray();
            for (int i = 0; i < keySetList.length; i++) {
                String keyName = (String) keySetList[i];
                tileMessage += "<wp:" + keyName + ">" + message.getMessageData().get(keyName) + "</wp:" + keyName + ">";


            }

            tileMessage += "</wp:Tile> " + "</wp:Notification>";

            OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream());
            writer.write(tileMessage);
            writer.flush();
            writer.close();
            String notificationStatus = uc.getHeaderField("X-NotificationStatus");
            String channelStatus = uc.getHeaderField("X-SubscriptionStatus");
            String deviceConnectionStatus = uc.getHeaderField("X-DeviceConnectionStatus");
            logger.info(notificationStatus + "|" + channelStatus + "|" + deviceConnectionStatus);

            if (notificationStatus.compareTo("N/A") != 0 && notificationStatus.compareTo("Suppressed") != 0 && notificationStatus.compareTo("Dropped") != 0) {
                resultModel = new ResultModel(ResultType.SUCCESSFUL, message.getMessageReceivers().get(0).getDeviceId());
            } else {
                if (channelStatus.compareTo("Expired") == 0)
                    resultModel = new ResultModel(ResultType.UNSUCCESS_DELETE, message.getMessageReceivers().get(0).getDeviceId());
                else
                    resultModel = new ResultModel(ResultType.UNSUCCESSFUL, message.getMessageReceivers().get(0).getDeviceId());
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return resultModel;
    }

    /**
     * Sends specified message to multiple devices, which is described in the message object, over MPNS protocol as tile notifications.
     * Returns the results of  each sending notification process.
     *
     * @param message
     * @return a list of ResultModel object.
     *         Each one represents a status of a pushed notification and  a device id for a device that notification was sent to.
     */
    @Override
    public List<ResultModel> sendNotification(Message message, Integer threadNumber) {
        List<ResultModel> resultModelList = new ArrayList<ResultModel>();
        int i = message.getDevicesIds().size()/threadNumber;
        int k = 0;
        for (int j = 0; j < message.getDevicesIds().size() / message.getDevicesIds().size()/threadNumber; j++) {
            Message newMessage = new Message(message.getMessageData(), message.getMessage(), message.getBadgeNo(), message.getSound(), message.getMessageReceivers().subList(k, i));
            MPNSJob thread = new MPNSJob(newMessage, this);
            resultModelList.addAll(thread.getResultModelList());
            k = i;
            i = i + message.getDevicesIds().size()/threadNumber;
        }
        if (k < message.getDevicesIds().size()) {
            Message newMessage = new Message(message.getMessageData(), message.getMessage(), message.getBadgeNo(), message.getSound(), message.getMessageReceivers().subList(k, message.getDevicesIds().size()));
            MPNSJob thread = new MPNSJob(newMessage, this);
            resultModelList.addAll(thread.getResultModelList());
        }

        return resultModelList;
    }
}
