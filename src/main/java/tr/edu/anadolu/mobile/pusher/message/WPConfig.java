package tr.edu.anadolu.mobile.pusher.message;

import com.google.common.base.Preconditions;
import tr.edu.anadolu.mobile.pusher.WPType;

/**
 * Represents the essential properties to push notification to  Apple devices over MPNS protocol.
 */
public class WPConfig {

    /**
     * Represents a title that will be shown in the foreground when a notification will be got for tile notification.
     */
    private String title;

    /**
     * Represents an image that will be shown in the background when a notification will be got for tile notification.
     */
    private String backGroundImage;

    /**
     * Represents a title for the message that pushed for tile notification.
     */
    private String backTitle;

    /**
     * Represents an image that will be shown in the background when back title is seen for tile notification.
     */
    private String backBackGroundImage;

    /**
     * Represents the page that will be navigated after clicking the notification for toast notification
     */
    private String page;

    /**
     * Represents how messages will be pushed.
     * They will be pushed as tile, toast or raw notifications.
     * {@link WPType}
     */
    private WPType wpType;

    /**
     * Constructs WPConfig object with tile notification parameters.
     * @param title a title that will be shown in the foreground
     * @param backGroundImage an image that will be shown in the background
     * @param backTitle an image that will be shown in the background
     * @param backBackGroundImage  an image that will be shown in the background
     */
    public WPConfig(String title, String backGroundImage, String backTitle, String backBackGroundImage) {
        Preconditions.checkNotNull(title, "Title should not be null");
        Preconditions.checkNotNull(backGroundImage, "BackGroundImage should not be null");
        Preconditions.checkNotNull(backTitle, "BackTitle should not be null");
        Preconditions.checkNotNull(backBackGroundImage, "BackBackGroundImage should not be null");
        this.title = title;
        this.backGroundImage = backGroundImage;
        this.backTitle = backTitle;
        this.backBackGroundImage = backBackGroundImage;
        this.wpType = WPType.Tile;
    }

    /**
     * Constructs WPConfig object with no parameters.
     */
    public WPConfig(){}

    /**
     * Constructs WPConfig object with toast notification parameters.
     * @param page a page that will be navigated
     */
    public WPConfig(String page) {
        Preconditions.checkNotNull(page, "Page should not be null");
        this.page = page;
        this.wpType = WPType.Toast;
    }

    /**
     * Returns the title that will be shown in the foreground when a notification  will be got for tile notification.
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title attribute
     * @param title the title that will be shown in the foreground when a notification  will be got for tile notification
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the image that will be shown in the background when a notification will be got for tile notification.
     * @return
     */
    public String getBackGroundImage() {
        return backGroundImage;
    }

    /**
     * Sets the backGroundImge attribute.
     * @param backGroundImage an image that will be shown in the background when a notification will be got for tile notification
     */
    public void setBackGroundImage(String backGroundImage) {
        this.backGroundImage = backGroundImage;
    }

    /**
     * Returns the title for the message that pushed for tile notification.
     * @return
     */
    public String getBackTitle() {
        return backTitle;
    }

    /**
     * Sets the backTitle attribute.
     * @param backTitle a title for the message that pushed for tile notification
     */
    public void setBackTitle(String backTitle) {
        this.backTitle = backTitle;
    }

    /**
     * Returns the image that will be shown in the background when back title is seen for tile notification.
     * @return
     */
    public String getBackBackGroundImage() {
        return backBackGroundImage;
    }

    /**
     * Sets the backBackGroundAttribute
     * @param backBackGroundImage an image that will be shown in the background when back title is seen for tile notification
     */
    public void setBackBackGroundImage(String backBackGroundImage) {
        this.backBackGroundImage = backBackGroundImage;
    }

    /**
     * Returns which notification type will be used to push notifications.
     * @return
     */
    public WPType getWpType() {
        return wpType;
    }

    /**
     * Sets wpType attribute.
     * @param wpType a notification type will be used to push notifications
     */
    public void setWpType(WPType wpType) {
        this.wpType = wpType;
    }

    /**
     * Returns the page that will be navigated after clicking the notification for toast notification.
     * @return
     */
    public String getPage() {
        return page;
    }

    /**
     * Sets the page attribute.
     * @param page a page that will be navigated after clicking the notification for toast notification
     */
    public void setPage(String page) {
        this.page = page;
    }
}
