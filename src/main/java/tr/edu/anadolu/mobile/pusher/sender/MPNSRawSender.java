package tr.edu.anadolu.mobile.pusher.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.edu.anadolu.mobile.pusher.ResultType;
import tr.edu.anadolu.mobile.pusher.message.Message;
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
 * Provides sending messages to Windows Phone devices with MPNS protocol as raw notifications.
 */
public class MPNSRawSender extends MPNSSender {

    private static final Logger logger = LoggerFactory.getLogger(MPNSRawSender.class);

    /**
     * Sends specified message to only one device, which described in the message object, over MPNS protocol as raw notification.
     * Returns the results of  each sending notification process.
     *
     * @param message the message object will be pushed.
     * @return a ResultModel object.
     * The ResultModel object represents a status of a pushed notification and  a device id for a device that notification sent to.
     */
    @Override
    public ResultModel sendOneNotification(Message message) {
        ResultModel resultModel = null ;
        URL url;
        try {
            url = new URL(message.getMessageReceivers().get(0).getDeviceId());

            URLConnection uc = url.openConnection();
            uc.setRequestProperty("ContentType", "text/xml");
            uc.setRequestProperty("X-NotificationClass", "3");
            uc.setDoOutput(true);
            String rawMessage = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<root>" +
                    "<message>" + message.getMessage() + "<message>" +
                    "<sound>" + message.getMessage() + "<sound>";
            Set<String> keySets = message.getMessageData().keySet();
            Object[] keySetList = keySets.toArray();
            for (int i = 0; i < keySetList.length; i++) {
                String keyName = (String) keySetList[i];
                rawMessage += "<" + keyName + ">" + message.getMessageData().get(keyName) + "<" + keyName + ">";

            }
            rawMessage += "</root>";
            OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream());
            writer.write(rawMessage);
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
                   resultModel=new ResultModel(ResultType.UNSUCCESS_DELETE, message.getMessageReceivers().get(0).getDeviceId());
                else
                    resultModel=new ResultModel(ResultType.UNSUCCESSFUL, message.getMessageReceivers().get(0).getDeviceId());
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return resultModel;
    }

    /**
     * Sends specified message to multiple devices, which described in the message object, over MPNS protocol as raw notifications.
     * Returns the results of  each sending notification process.
     *
     * @param message
     * @return a list of ResultModel object.
     * Each one represents a status of a pushed notification and  a device id for a device that notification sent to.
     */
    @Override
    public List<ResultModel> sendNotification(Message message) {
        List<ResultModel> resultModelList = new ArrayList<ResultModel>();
        int i = 50;
        int k = 0;
        for (int j = 0; j < message.getDevicesIds().size() / 50; j++) {
            Message newMessage = new Message(message.getMessageData(), message.getMessage(), message.getBadgeNo(), message.getSound(), message.getMessageReceivers().subList(k, i));
            MPNSJob thread = new MPNSJob(newMessage, this);
            resultModelList.addAll(thread.getResultModelList());
            k = i;
            i = i + 50;
        }
        if (k < message.getDevicesIds().size()) {
            Message newMessage = new Message(message.getMessageData(), message.getMessage(), message.getBadgeNo(), message.getSound(), message.getMessageReceivers().subList(k, message.getDevicesIds().size()));
            MPNSJob thread = new MPNSJob(newMessage, this);
            resultModelList.addAll(thread.getResultModelList());
        }

        return resultModelList;
    }
}
