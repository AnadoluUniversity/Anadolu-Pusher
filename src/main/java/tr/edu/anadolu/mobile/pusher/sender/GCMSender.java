package tr.edu.anadolu.mobile.pusher.sender;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.edu.anadolu.mobile.pusher.ResultType;
import tr.edu.anadolu.mobile.pusher.message.GCMConfig;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Provides sending messages to Android devices with GCM protocol.
 */
public class GCMSender implements SenderStrategy {

    private static final Logger logger = LoggerFactory.getLogger(GCMSender.class);
    private GCMConfig gcmConfig;

    public GCMSender(GCMConfig gcmConfig) {
        this.gcmConfig = gcmConfig;
    }

    /**
     * Sends specified message to the device/devices, which described in the message object, over GCM protocol.
     * Returns the results of  each sending notification process.
     *
     * @param message
     * @return a list of ResultModel object.
     * Each one represents a status of a pushed notification and  a device id for a device that notification was sent to.
     * {@link ResultType}
     */
    @Override
    public List<ResultModel> sendNotification(Message message) {
        List<ResultModel> resultModelList = new ArrayList<ResultModel>();
        Sender s = new Sender(gcmConfig.getAPIKey());
        com.google.android.gcm.server.Message.Builder builder = new com.google.android.gcm.server.Message.Builder();

        Set<String> keySets = message.getMessageData().keySet();
        Object[] keySetList = keySets.toArray();
        for (Object aKeySetList : keySetList) {
            String keyName = (String) aKeySetList;
            builder.addData(keyName, message.getMessageData().get(keyName));
        }

        builder.addData("message", message.getMessage());
        builder.addData("badgeNo", message.getBadgeNo() + "");
        builder.addData("sound", message.getSound());
        com.google.android.gcm.server.Message gcmMessage = builder.build();

        List<String> partialDeviceList;

        int i = 1000;
        int k = 0;

        try {

            for (int j = 0; j < message.getDevicesIds().size() / 1000; j++) {

                partialDeviceList = message.getDevicesIds().subList(k, i);

                //push notifications
                MulticastResult r = s.send(gcmMessage, partialDeviceList, 1);

                List<Result> results = r.getResults();
                int index;

                //Get results and control whether notifications are sent or not.
                for (index = 0; index < results.size(); index++) {
                    if (results.get(index) != null) {
                        if (results.get(index).getMessageId() != null) {

                            //It is successfully sent to Android device.
                            String canonicalId = results.get(index).getCanonicalRegistrationId();


                            if (canonicalId != null) {
                                ResultModel model = new ResultModel(ResultType.SUCCESS_UPDATE, partialDeviceList.get(index));
                                model.setCanonicalId(canonicalId);
                                resultModelList.add(model);
                            } else {
                                ResultModel model = new ResultModel(ResultType.SUCCESSFUL, partialDeviceList.get(index));
                                resultModelList.add(model);
                            }
                        }
                        else {

                            // notification is not sent
                            String error = results.get(index).getErrorCodeName();
                            if (error.equals(Constants.ERROR_INVALID_REGISTRATION)) {
                                logger.warn(Constants.ERROR_INVALID_REGISTRATION);
                                // application has been removed from device - unregister from database
                                ResultModel model = new ResultModel(ResultType.UNSUCCESS_DELETE, partialDeviceList.get(index));
                                resultModelList.add(model);
                            } else {
                                ResultModel model = new ResultModel(ResultType.UNSUCCESSFUL, partialDeviceList.get(index));
                                resultModelList.add(model);
                            }
                        }
                    }
                }


                k = i;
                i = i + 1000;
            }

            if (k < message.getDevicesIds().size())

            {
                partialDeviceList = message.getDevicesIds().subList(k, message.getDevicesIds().size());

                //push notifications
                MulticastResult r = s.send(gcmMessage, partialDeviceList, 1);
                List<Result> results = r.getResults();
                int index;
                //Get results and control whether notifications are sent or not.
                for (index = 0; index < results.size(); index++) {
                    if (results.get(index) != null) {
                        if (results.get(index).getMessageId() != null) {

                            //It is successfully sent to Android device.
                            String canonicalId = results.get(index).getCanonicalRegistrationId();


                            if (canonicalId != null) {
                                ResultModel model = new ResultModel(ResultType.SUCCESS_UPDATE, partialDeviceList.get(index));
                                model.setCanonicalId(canonicalId);
                                resultModelList.add(model);
                            } else {
                                ResultModel model = new ResultModel(ResultType.SUCCESSFUL, partialDeviceList.get(index));
                                resultModelList.add(model);
                            }
                        }
                        else {

                            // notification is not sent
                            String error = results.get(index).getErrorCodeName();
                            if (error.equals(Constants.ERROR_INVALID_REGISTRATION)) {
                                // application has been removed from device - unregister from database
                                logger.warn(Constants.ERROR_INVALID_REGISTRATION);
                                ResultModel model = new ResultModel(ResultType.UNSUCCESS_DELETE, partialDeviceList.get(index));
                                resultModelList.add(model);
                            } else {
                                ResultModel model = new ResultModel(ResultType.UNSUCCESSFUL, partialDeviceList.get(index));
                                resultModelList.add(model);
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return resultModelList;

    }
}
