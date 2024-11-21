package app.labs.linksy.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class NotificationSettings {
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean smsNotifications;
}
