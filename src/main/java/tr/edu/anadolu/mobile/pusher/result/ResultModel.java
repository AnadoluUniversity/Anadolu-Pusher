package tr.edu.anadolu.mobile.pusher.result;

import tr.edu.anadolu.mobile.pusher.ResultType;

/**
 * It represents the result for a notification.
 */
public class ResultModel {

    /**
     * The status about whether a notification is pushed or not.
     * ResultType is enum.
     */
    private ResultType resultType;

    /**
     * An id that specifies one device.
     */
    private String deviceID;

    /**
     * If device id of an Android device changes, Google Cloud Messaging protocol sends a canonical id.
     * It represents a new registration id for the old one.
     */
    private String canonicalId;

    /**
     * Creates a new ResultModel with the given result type and device id.
     *  <pre>
     * <code>
     * ResultModel model = new ResultModel(ResultType.UNSUCCESSFUL,"__DEVICE_ID__")
     * </code>
     * </pre>
     */
    public ResultModel(ResultType resultType, String deviceID) {
        this.resultType = resultType;
        this.deviceID = deviceID;
    }

    /**
     *
     * @return New device id for android device.
     */
    public String getCanonicalId() {
        return canonicalId;
    }

    /**
     *
     * @param canonicalId
     */
    public void setCanonicalId(String canonicalId) {
        this.canonicalId = canonicalId;
    }

    /**
     *
     * @return
     */
    public ResultType getResultType() {
        return resultType;
    }

    /**
     *
     * @return
     */
    public String getDeviceID() {
        return deviceID;
    }
}
