package platform.qa.bpms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.AuthorizationApi;
import platform.qa.entities.auth.AuthorizationRequest;
import platform.qa.enums.AuthorizationType;
import platform.qa.enums.Permission;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class AuthorizationApiTest {
    AuthorizationApi authorizationApi = mock(AuthorizationApi.class);

    String authorizationId = "testAuthorizationId";

    List<Map> authorizationResponse = List.of(Map.of(
            "id", "cddbcede-fb9b-11ec-9bab-0a580a801088",
            "type", "testType"
    ));

    AuthorizationRequest authorizationRequest = AuthorizationRequest.builder()
            .type(AuthorizationType.AUTH_TYPE_GRANT.getType())
            .permissions(List.of(Permission.READ.getPermission(), Permission.CREATE_INSTANCE.getPermission()))
            .groupId("testGroupId")
            .resourceId("testProcessKey")
            .build();

    @Test
    public void getAuthorizationsByParamMockTest() {
        when(authorizationApi.getAuthorizationsByParam(anyString(), anyString()))
                .thenReturn(authorizationResponse);

        var auth = authorizationApi
                .getAuthorizationsByParam("testName", "testValue");

        assertThat(auth.size()).isEqualTo(1);
        assertThat(auth.get(0)).containsExactlyEntriesOf(authorizationResponse.get(0));
    }

    @Test
    public void createAuthorizationTest(){
        when(authorizationApi.createAuthorization(authorizationRequest))
                .thenReturn(authorizationId);

        var newAuth = authorizationApi.createAuthorization(authorizationRequest);

        assertThat(newAuth).isEqualTo(authorizationId);
    }
}
