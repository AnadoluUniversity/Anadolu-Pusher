package tr.edu.anadolu.mobile.pusher.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.edu.anadolu.mobile.pusher.WPType;
import tr.edu.anadolu.mobile.pusher.base.NotificationReceiver;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a runnable object for sending messages to multiple Windows Phone devices over MPNS protocol concurrently.
 */
public class MPNSJob implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MPNSJob.class);

    /**
     * Represents an object that provides concurrently sending a message.
     */
    private Thread t;

    /**
     * Represents the message that will be pushed to the multiple Windows Phone devices.
     */
    private Message genericNotification;

    /**
     * Represents a list of ResultModel objects.
     * ResultModel specifies the status of the pushed notification and specific id of the device.
     * {@link ResultModel}
     */
    private List<ResultModel> resultModelList = new ArrayList<ResultModel>();

    /**
     * Represents MPNSSender object to push notifications as specified type.
     * {@link WPType}
     *
     */
    private MPNSSender sender;

    /**
     * Constructs a MPNSJob object with a Message and a MPNSSender objects
     * @param message message will be pushed.
     * @param sender  MPNSSender object to push notifications.
     */
    public MPNSJob(Message message,MPNSSender sender) {
        genericNotification = message;
        this.sender= sender;
        t = new Thread(this, "MPNSJob");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Returns the collection of ResultModel objects.
     * Each one specifies the status of the pushed notification and specific id of the device.
     * @return a list of ResultModel objects
     */
    public List<ResultModel> getResultModelList() {
        return resultModelList;
    }

    /**
     * Sends notifications to multiple devices.
     */
    @Override
    public void run() {

        for (int i = 0; i < genericNotification.getDevicesIds().size(); i++) {
            List<NotificationReceiver> receivers = new ArrayList<NotificationReceiver>();
            receivers.add(genericNotification.getMessageReceivers().get(i));
            Message message = new Message(genericNotification.getMessageData(),
                    genericNotification.getMessage(),
                    genericNotification.getBadgeNo(),
                    genericNotification.getSound(),
                    receivers);
            resultModelList.add(this.sender.sendOneNotification(message));

        }
    }

}
