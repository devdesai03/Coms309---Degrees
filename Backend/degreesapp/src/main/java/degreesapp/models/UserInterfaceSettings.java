package degreesapp.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@ApiModel(value = "UserInterfaceSettings", description = "An object containing a user's personalization settings" +
        " regarding the user interface of the app.")
public class UserInterfaceSettings {
    public static enum NightModeSetting {
        on, off, followSystem;

        public static NightModeSetting getDefault() {
            return NightModeSetting.followSystem;
        }
    }

    @Id
    @ApiModelProperty("The user ID.")
    private Long userId;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId
    @ApiModelProperty("The user's general information.")
    private User user;

    @ApiModelProperty("The user's night mode setting. May be one of three values: `on`, `off`, `followSystem`. " +
            "By default, this is `followSystem`.")
    private NightModeSetting nightMode = NightModeSetting.getDefault();

    @ApiModelProperty("The user's preferred background color. " +
            "This can either be a color hex value, such as `#FFFFFF`, or `null`. " +
            "If `null`, the default background color should be used.")
    private String backgroundColor = null;

    public UserInterfaceSettings() {
        this(null, NightModeSetting.getDefault(), null);
    }

    public UserInterfaceSettings(User user, NightModeSetting nightMode, String backgroundColor) {
        this.user = user;
        this.nightMode = nightMode;
        this.backgroundColor = backgroundColor;
    }

    public static UserInterfaceSettings getDefault(User user) {
        UserInterfaceSettings userInterfaceSettings = new UserInterfaceSettings();
        userInterfaceSettings.setUser(user);
        return userInterfaceSettings;
    }

    public Long getUserId() {
        return this.userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getUserId();
    }

    public NightModeSetting getNightMode() {
        return nightMode;
    }

    public void setNightMode(NightModeSetting nightMode) {
        this.nightMode = nightMode;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
