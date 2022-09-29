package platform.qa.settings.enumerations;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ChannelType {
    EMAIL("email"), DIIA("diia");

    final String type;
    ChannelType(String type) {
        this.type = type;
    }

}
