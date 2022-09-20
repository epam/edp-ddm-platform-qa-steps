package platform.qa.pojo.common.settings.request;

import lombok.Data;
import lombok.ToString;
import lombok.Value;

@Data
@ToString
@Value(staticConstructor = "of")
public class Email {
	String address;
}