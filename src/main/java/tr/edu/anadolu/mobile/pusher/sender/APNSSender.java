package tr.edu.anadolu.mobile.pusher.sender;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.edu.anadolu.mobile.pusher.message.APNSConfig;
import tr.edu.anadolu.mobile.pusher.message.Message;
import tr.edu.anadolu.mobile.pusher.ResultType;
import tr.edu.anadolu.mobile.pusher.result.ResultModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Provides sending messages to Apple devices with APNS protocol.
 */
public class APNSSender implements SenderStrategy {
    private static final Logger logger = LoggerFactory.getLogger(APNSSender.class);
    private APNSConfig apnsConfig;

    public APNSSender(APNSConfig apnsConfig) {
        this.apnsConfig = apnsConfig;
    }

    /**
     * Sends specified message to the device/devices, which described in the message object, over APNS protocol.
     * Returns the results of  each sending notification process.
     *
     * @param message
     * @return a list of ResultModel object.
     * Each one represents a status of a pushed notification and  a device id for a device that notification sent to.
     */
    @Override
    public List<ResultModel> sendNotification(Message message,Integer threadNumber) {
        List<ResultModel> resultModelList = new ArrayList<ResultModel>();
        int threads = threadNumber;

       /* Prepare a simple payload to push */
        PushNotificationPayload payload = PushNotificationPayload.complex();
        try {
            payload.addAlert(message.getMessage());
            payload.addBadge(message.getBadgeNo());
            payload.addSound(message.getSound());

            Set<String> keySets = message.getMessageData().keySet();
            Object[] keySetList = keySets.toArray();
            for (Object aKeySetList : keySetList) {
                String keyName = (String) aKeySetList;
                payload.addCustomDictionary(keyName, message.getMessageData().get(keyName));
            }

            //Get results and control whether notifications are sent or not.
            PushedNotifications notifications = Push.payload(payload,
                    getClass().getClassLoader().getResourceAsStream(apnsConfig.getAPNSCertPath()),
                    apnsConfig.getPassword(),
                    apnsConfig.getProduction(),
                    threads,
                    message.getDevicesIds());
            for (PushedNotification notification : notifications) {

                if (notification.isSuccessful()) {
                    ResultModel model = new ResultModel(ResultType.SUCCESSFUL, notification.getDevice().getToken());
                    resultModelList.add(model);

                } else {
                /* If the problem was an error-response packet returned by Apple, get it */
                    ResponsePacket theErrorResponse = notification.getResponse();
                    if (theErrorResponse != null) {
                        ResultModel model = new ResultModel(ResultType.UNSUCCESSFUL, notification.getDevice().getToken());
                        resultModelList.add(model);
                    }
                    else {
                        ResultModel model = new ResultModel(ResultType.UNSUCCESS_DELETE, notification.getDevice().getToken());
                        resultModelList.add(model);
                    }
                }
            }
        } catch (JSONException e) {
            logger.error(e.getMessage());
        } catch (KeystoreException e) {
            logger.error(e.getMessage());
        } catch (CommunicationException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.out.println(e);
        }

        return resultModelList;
    }
}
