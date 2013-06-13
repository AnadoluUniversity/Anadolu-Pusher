package tr.edu.anadolu.mobile.pusher.sender;

import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.util.List;


/**
 * Provides sending messages to Windows Phone devices with specified protocol.
 */
public interface SenderStrategy  {

    /**
     * Sends specified message to multiple devices, which are described in the message object, over specified protocol
     * Returns the results of  each sending notification process.
     *
     * @param message
     * @return a list of ResultModel object.
     * Each one represents a status of a pushed notification and  a device id for a device that notification was sent to.
     */
    public List<ResultModel> sendNotification(Message message, Integer threadNumber);

}
