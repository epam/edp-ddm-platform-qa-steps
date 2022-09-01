package platform.qa.processHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.ProcessHistoryTaskApi;
import platform.qa.entities.HistoryTask;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ProcessHistoryTaskApiTest {
    ProcessHistoryTaskApi processHistoryTaskApi = mock(ProcessHistoryTaskApi.class);
    List<HistoryTask> historyTasksInstancesResponse = List.of(
            HistoryTask.builder().taskDefinitionName("name1").build(),
            HistoryTask.builder().taskDefinitionName("name2").build()
    );

    @Test
    public void getHistoryTasksInstancesTest() {
        when(processHistoryTaskApi.getHistoryTasksInstances()).thenReturn(historyTasksInstancesResponse);

        var historyTasksInstances = processHistoryTaskApi.getHistoryTasksInstances();

        assertThat(historyTasksInstances.size()).isEqualTo(2);
        assertThat(historyTasksInstances).isEqualTo(historyTasksInstancesResponse);
        }
}
