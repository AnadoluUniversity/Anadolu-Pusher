package tr.edu.anadolu.mobile.pusher.base;


import tr.edu.anadolu.mobile.pusher.DeviceType;

/**
 * Represents the receiver that a notification will be pushed to.
 */
public class NotificationReceiver {

    /**
     * Represents the kind of a device.
     * {@link DeviceType}
     */
    private DeviceType deviceType;

    /**
     * Represents a specific id belonging to a device.
     */
    private String deviceId;

    /**
     * Constructs a NotificationReceiver object with no parameters.
     */
    public NotificationReceiver(){}

    /**
     * Constructs a NotificationReceiver object with deviceType and deviceId parameters.
     * @param deviceType  represents the kid of a device.
     * @param deviceID    represents the specific id of a device.
     */
    public NotificationReceiver( DeviceType deviceType, String deviceID ) {
        this.deviceType = deviceType;
        this.deviceId =deviceID;
    }

    /**
     *
     * @return  the kind of the device.
     */
    public DeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the kind of the device.
     * @param deviceType
     */
    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    /**
     *
     * @return the specific id of the device.
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the specific id of the device.
     * @param deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
