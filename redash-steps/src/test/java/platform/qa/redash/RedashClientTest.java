package platform.qa.redash;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.clients.RedashClient;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class RedashClientTest {
    RedashClient redashClient = mock(RedashClient.class);
    Object response = new Object();

    @Test
    public void getRequestTest() {
        when(redashClient.getRequest(anyString(), anyString())).thenReturn(response);

        var request = redashClient.getRequest(anyString(), anyString());

        Assertions.assertThat(request).isEqualTo(response);
    }

    @Test
    public void deleteRequestTest() {
        doNothing().when(redashClient).deleteRequest(anyString());

        redashClient.deleteRequest(anyString());
    }
}
