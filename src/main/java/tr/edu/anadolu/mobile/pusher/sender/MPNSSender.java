package tr.edu.anadolu.mobile.pusher.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.message.WPConfig;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a general service for sending messages to Windows Phone devices with MPNS protocol.
 */
public abstract class MPNSSender implements SenderStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MPNSSender.class);

    /**
     * Sends specified message to only one device, which described in the message object, over MPNS protocol.
     * Returns the results of  each sending notification process.
     *
     * @param message the message object will be pushed.
     * @return a ResultModel object.
     * The ResultModel object represents a status of a pushed notification and  a device id for a device that notification sent to.
     */
    public abstract ResultModel sendOneNotification(Message message);


}
