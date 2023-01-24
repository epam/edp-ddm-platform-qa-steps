package platform.qa.redash;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.RedashApi;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class RedashApiTest {
    RedashApi redashApi = mock(RedashApi.class);
    Map<String, Integer> dashboardListResponse = Map.of(
            "dashboard1", 1,
            "dashboard2", 2
    );

    @Test
    public void getDashboardsListTest() {
        AtomicInteger iterator = new AtomicInteger(0);
        when(redashApi.getDashboardsList()).thenReturn(dashboardListResponse);

        var dashboardList = redashApi.getDashboardsList();

        assertThat(dashboardList.size()).isEqualTo(2);
        assertThat(dashboardList).isEqualTo(dashboardListResponse);
    }

    @Test
    public void waitForJobCompletionTest() {
        doNothing().when(redashApi).deleteDashboardWithDashboardId(anyString(), anyInt());

        redashApi.deleteDashboardWithDashboardId(anyString(), anyInt());
    }
}
