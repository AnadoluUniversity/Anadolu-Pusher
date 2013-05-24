package tr.edu.anadolu.mobile.pusher.base;

import tr.edu.anadolu.mobile.pusher.DeviceType;
import tr.edu.anadolu.mobile.pusher.WPType;
import tr.edu.anadolu.mobile.pusher.message.APNSConfig;
import tr.edu.anadolu.mobile.pusher.message.GCMConfig;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.message.WPConfig;
import tr.edu.anadolu.mobile.pusher.sender.*;
import tr.edu.anadolu.mobile.pusher.result.MessageResult;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a service for sending notifications to multiple/singular devices/device,
 * setting specific properties of protocols (APNS, GCM, MPNS),
 * getting the result status of the pushed notifications.
 */
public class NotificationSender {

    /**
     * Represents the essential properties to push notifications with MPNS.
     * * {@link WPConfig}
     */
    private WPConfig wpConfig;

    /**
     * Represents the essential properties to push notifications with APNS.
     * * {@link APNSConfig}
     */
    private APNSConfig apnsConfig;

    /**
     * Represents the essential properties to push notifications with GCM.
     * * {@link GCMConfig}
     */
    private GCMConfig gcmConfig;

    /**
     * Represents the SenderStrategy that specifies which protocol will be used to push notifications.
     * {@link SenderStrategy}
     */
    private SenderStrategy strategy;

    /**
     * Pushes the message to the device/devices described in the message object.
     * Returns the result of the pushing notification process for specified message.
     *
     * @param message the message will be pushed.
     * @return the status of notification process.
     */
    public MessageResult send(Message message) {
        List<ResultModel> resultModelList;
        MessageResult result = new MessageResult();
        result.setMessage(message);

        List<NotificationReceiver> androidReceivers = new ArrayList<NotificationReceiver>();
        List<NotificationReceiver> iosReceivers = new ArrayList<NotificationReceiver>();
        List<NotificationReceiver> wpReceivers = new ArrayList<NotificationReceiver>();

        for (NotificationReceiver receiver : message.getMessageReceivers()) {
            if (receiver.getDeviceType() == DeviceType.ANDROID) {
                androidReceivers.add(receiver);
            }

            if (receiver.getDeviceType() == DeviceType.IOS) {
                iosReceivers.add(receiver);
            }

            if (receiver.getDeviceType() == DeviceType.WP) {
                wpReceivers.add(receiver);
            }
        }

        if (iosReceivers.size() > 0) {
            Message apnsMessage = message;
            apnsMessage.setMessageReceivers(iosReceivers);
            strategy = new APNSSender(apnsConfig);
            resultModelList = result.getResults();
            resultModelList.addAll(strategy.sendNotification(message));
            result.setResult(resultModelList);
        }

        if (androidReceivers.size() > 0) {
            Message gcmMessage = message;
            gcmMessage.setMessageReceivers(androidReceivers);
            strategy = new GCMSender(gcmConfig);
            resultModelList = result.getResults();
            resultModelList.addAll(strategy.sendNotification(message));
            result.setResult(resultModelList);
        }

        if (wpReceivers.size() > 0) {
            Message wpMessage = message;
            wpMessage.setMessageReceivers(wpReceivers);
            if (wpConfig.getWpType().compareTo(WPType.Raw) == 0) {
                strategy = new MPNSRawSender();
            }
            else if (wpConfig.getWpType().compareTo(WPType.Tile) == 0) {
                strategy = new MPNSTileSender(wpConfig);
            }
            else
                strategy = new MPNSToastSender(wpConfig);

            resultModelList = result.getResults();
            resultModelList.addAll(strategy.sendNotification(message));
            result.setResult(resultModelList);
        }

        return result;
    }

    /**
     * Sets the essential properties of MPNS protocol.
     * @param wpConfig
     */
    public void setWpConfig(WPConfig wpConfig) {
        this.wpConfig = wpConfig;
    }

    /**
     * Sets the essential properties of APNS protocol.
     * @param apnsConfig
     */
    public void setApnsConfig(APNSConfig apnsConfig) {
        this.apnsConfig = apnsConfig;
    }

    /**
     * Sets the essential properties for GCM protocol.
     * @param gcmConfig
     */
    public void setGcmConfig(GCMConfig gcmConfig) {
        this.gcmConfig = gcmConfig;
    }

}
