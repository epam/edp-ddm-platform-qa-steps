package platform.qa.keycloak.pojo.request;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchEqualsAttributes {
    private String edrpou;
    private String drfo;
    private String fullName;
    @JsonProperty("KATOTTG")
    private String katottg;
}
