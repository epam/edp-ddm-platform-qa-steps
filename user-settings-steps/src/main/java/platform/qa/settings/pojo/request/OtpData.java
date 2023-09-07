package platform.qa.settings.pojo.request;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Data
@Value(staticConstructor = "of")
public class OtpData {
    String address;
    String verificationCode;

}
