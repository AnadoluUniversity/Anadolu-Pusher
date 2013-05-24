package tr.edu.anadolu.mobile.pusher.message;

/**
 * Represents the essential properties to push notification to Android devices over GCM protocol.
 */
public class GCMConfig {

    /**
     * Specifies the API key that belongs to one Android application.
     */
    private String APIKey;

    /**
     * Constructs GCMConfig object with no parameters
     */
    public GCMConfig(){}

    /**
     * Constructs GCMConfig object with a key
     * @param APIKey a key that belongs to one Android application.
     */
    public GCMConfig(String APIKey) {
        this.APIKey = APIKey;
    }

    /**
     * Returns API key that belongs to one Android application.
     * @return
     */
    public String getAPIKey() {
        return APIKey;
    }

    /**
     * Sets APIKey attribute.
     * @param APIKey a key that belongs to one Android application.
     */
    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }
}
