package tr.edu.anadolu.mobile.pusher.message;

/**
 * Reperesents the essential properties to push notification to  Apple devices over APNS protocol.
 */
public class APNSConfig {

    /**
     * Specifies the path of the certificate file.
     * Certificate file provides specifying the iOS application to push notifications.
     */
    private String APNSCertPath;

    /**
     * Specifies a password to use a certificate file
     */
    private String password;

    /**
     * Specifies the notification will be pushed by using development environment or production environment.
     * True means production environment.
     * False means development environment.
     */
    private boolean production;

    /**
     * Constructs an APNSConfig object with no parameters.
     */
    public APNSConfig(){}

    /**
     * Constructs an APNSConfig object with parameters.
     * @param APNSCertPath  specifies the classpath location of the certificate file.
     * @param password  specifies the password to use certificate
     * @param production specifies which one is used production or environment.
     */
    public APNSConfig(String APNSCertPath, String password, boolean production) {
        this.APNSCertPath = APNSCertPath;
        this.password = password;
        this.production = production;
    }

    /**
     * Returns path of the cerificate file.
     * @return
     */
    public String getAPNSCertPath() {
        return APNSCertPath;
    }

    /**
     * Sets the path of the certificate file.
     * @param APNSCertPath  the path of the certificate file
     */
    public void setAPNSCertPath(String APNSCertPath) {
        this.APNSCertPath = APNSCertPath;
    }

    /**
     * Returns the password to use the certificate file.
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password to use the certificate file.
     * @param password a password for a certificate file
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns whether the production environment will be used or not.
     * @return
     */
    public boolean getProduction() {
        return production;
    }

    /**
     * Sets whether the production environment will be used or not.
     * @param production Whether production environment will be used or not.
     */
    public void setProduction(boolean production) {
        this.production = production;
    }
}
