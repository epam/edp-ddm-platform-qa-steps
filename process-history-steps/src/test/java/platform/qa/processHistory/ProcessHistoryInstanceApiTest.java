package platform.qa.processHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.ProcessHistoryInstanceApi;
import platform.qa.entities.HistoryProcessInstance;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ProcessHistoryInstanceApiTest {
    ProcessHistoryInstanceApi processHistoryInstanceApi = mock(ProcessHistoryInstanceApi.class);
    List<HistoryProcessInstance> processHistoryInstanceListResponse = List.of(
            HistoryProcessInstance.builder().processInstanceId("instId1").processDefinitionId("definitionId1").build(),
            HistoryProcessInstance.builder().processInstanceId("instId2").processDefinitionId("definitionId2").build()
    );

    @Test
    public void getHistoryProcessInstancesListTest() {
        when(processHistoryInstanceApi.getHistoryProcessInstancesList()).thenReturn(processHistoryInstanceListResponse);

        var processHistoryInstanceList = processHistoryInstanceApi
                .getHistoryProcessInstancesList();

        assertThat(processHistoryInstanceList.size()).isEqualTo(2);
        assertThat(processHistoryInstanceList).isEqualTo(processHistoryInstanceListResponse);
    }
}
