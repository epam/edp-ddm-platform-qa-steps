package platform.qa.settings.pojo.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingsResponse{
	private List<Channel> channels;
	private String settingsId;

	@Data
	@ToString
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Channel {
		private String deactivationReason;
		private String address;
		private String channel;
		private boolean activated;
	}
}