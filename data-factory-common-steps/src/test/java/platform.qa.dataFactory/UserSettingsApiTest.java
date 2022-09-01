package platform.qa.dataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.restassured.response.ValidatableResponse;
import platform.qa.data.common.UserSettingsApi;

import org.junit.jupiter.api.Test;

public class UserSettingsApiTest {
    UserSettingsApi userSettingsApi = mock(UserSettingsApi.class);
    ValidatableResponse userSettingsResponse;

    @Test
    public void getUserSettingsTest() {
        when(userSettingsApi.getUserSettings()).thenReturn(userSettingsResponse);

        var userSettings = userSettingsApi.getUserSettings();

        assertThat(userSettings).isEqualTo(userSettingsResponse);
    }
}
