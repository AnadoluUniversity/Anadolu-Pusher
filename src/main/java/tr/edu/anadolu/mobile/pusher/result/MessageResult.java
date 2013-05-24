package tr.edu.anadolu.mobile.pusher.result;

import com.google.common.collect.Lists;
import tr.edu.anadolu.mobile.pusher.AnadoluPusherException;
import tr.edu.anadolu.mobile.pusher.message.Message;

import java.util.List;

/**
 * Represents pushed message and results for all pushed notifications.
 */
public class MessageResult {

    /**
     * The message that pushed.
     */
    private Message message;

    /**
     * Represents a list of ResultModel objects.
     * ResultModel specifies the status of the pushed notification and specific id of the device.
     * {@link ResultModel}
     */
    private List<ResultModel> result;

    /**
     * Constructs a MessageResult object with no parameters.
     */
    public MessageResult() {
        result = Lists.newArrayList();
    }

    /**
     *
     * @return  the message object that pushed.
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Sets the message object.
     * @param message
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     *
     * @return  a list of ResultModel
     */
    public List<ResultModel> getResults() {
        return result;
    }

    /**
     *
     * @return the first ResultModel element of the list.
     */
    public ResultModel getSingleResult() {
        if (result.isEmpty()) {
            throw new AnadoluPusherException("Result is empty");
        }
        return result.get(0);
    }

    /**
     * Sets the list of ResultModel.
     * @param result
     */
    public void setResult(List<ResultModel> result) {
        this.result = result;
    }

}
