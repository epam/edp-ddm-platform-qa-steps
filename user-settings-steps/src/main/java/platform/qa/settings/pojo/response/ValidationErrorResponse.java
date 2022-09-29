package platform.qa.settings.pojo.response;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationErrorResponse{
	private String traceId;
	private String code;
	private String localizedMessage;
	private String message;
}