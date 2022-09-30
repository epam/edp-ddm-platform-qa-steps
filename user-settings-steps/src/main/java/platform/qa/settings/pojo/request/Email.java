package platform.qa.settings.pojo.request;

import lombok.Data;
import lombok.ToString;
import lombok.Value;
import platform.qa.entities.IEntity;

@Data
@ToString
@Value(staticConstructor = "of")
public class Email implements IEntity {
    String address;
}
