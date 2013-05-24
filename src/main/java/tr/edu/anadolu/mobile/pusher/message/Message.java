package tr.edu.anadolu.mobile.pusher.message;

import com.google.common.base.Preconditions;
import tr.edu.anadolu.mobile.pusher.base.NotificationReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an object with some properties that will be pushed to the devices.
 */
public class Message {

    /**
     * Represents a meaningful text that will be shown in the notification bar of devices.
     */
    private String message;

    /**
     * Represents message details that may be essential for page navigation.
     */
    private Map<String, String> messageData;

    /**
     * Represents the device/devices that notifications to be pushed to.
     */
    private List<NotificationReceiver> messageReceivers;

    /**
     * Represents the badge number.
     */
    private Integer badgeNo;

    /**
     * Represents the sound that will be played when a notification is received.
     */
    private String sound;

    /**
     * Constructs a Message object with parameters.
     * @param messageData  message details that may be essential for page navigation
     * @param message  a meaningful text that will be shown in the notification bar of the device/devices
     * @param badgeNo  badge number
     * @param sound    sound that will be played
     * @param receivers  devices that a notification will be pushed to
     */
    public Message(Map<String, String> messageData, String message, Integer badgeNo, String sound, List<NotificationReceiver> receivers) {
        Preconditions.checkNotNull(badgeNo, "BadgeNo should not be null");
        Preconditions.checkNotNull(sound, "Sound should not be null");
        this.messageData = messageData;
        this.message = message;
        this.badgeNo = badgeNo;
        this.sound = sound;
        this.messageReceivers = receivers;
    }

    /**
     * Returns the badge number
     * @return
     */
    public Integer getBadgeNo() {
        return badgeNo;
    }

    /**
     * Returns the sound that will be played when a notification is got.
     * @return
     */
    public String getSound() {
        return sound;
    }

    /**
     * Returns the meaningful text that will be shown in the notification bar of devices.
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the message details that may essential for page navigation.
     * @return
     */
    public Map<String, String> getMessageData() {
        return messageData;
    }

    /**
     * Adds one message detail
     * @param key a specific key to represent a message detail.
     * @param value a message detail
     */
    public void addMessageData(String key, String value) {
        messageData.put(key, value);
    }

    /**
     * Returns the device/devices that wanted to push notification.
     * @return
     */
    public List<NotificationReceiver> getMessageReceivers() {
        return messageReceivers;
    }

    /**
     * Sets the the devices that notifications will be pushed to.
     * @param messageReceivers
     */
    public void setMessageReceivers(List<NotificationReceiver> messageReceivers) {
        this.messageReceivers = messageReceivers;
    }

    /**
     * Returns only a collection of specific ids of the receivers.
     * @return
     */
    public List<String> getDevicesIds() {
        List<String> devices = new ArrayList<String>();
        for (NotificationReceiver receiver : this.getMessageReceivers()) {
            devices.add(receiver.getDeviceId());
        }
        return devices;
    }


}
