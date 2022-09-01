package platform.qa.bpms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.ProcessInstanceApi;
import platform.qa.entities.Instance;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ProcessInstanceApiTest {
    ProcessInstanceApi processInstanceApi = mock(ProcessInstanceApi.class);
    int statusCode = 200;
    List<Instance> processInstancesListResponse = List.of(
            Instance.builder().id("id1").definitionId("definitionId1").businessKey("businessKey1").build(),
            Instance.builder().id("id2").definitionId("definitionId2").businessKey("businessKey2").build()
    );

    @Test
    public void getProcessInstancesListTest() {
        when(processInstanceApi.getProcessInstancesList()).thenReturn(processInstancesListResponse);

        var processInstancesList = processInstanceApi.getProcessInstancesList();

        assertThat(processInstancesList.size()).isEqualTo(2);
        assertThat(processInstancesList).isEqualTo(processInstancesListResponse);
    }

    @Test
    public void getProcessInstanceStatusCodeTest() {
        when(processInstanceApi.getProcessInstanceStatusCode(anyString())).thenReturn(statusCode);

        var processInstanceStatusCode = processInstanceApi.getProcessInstanceStatusCode("instanceId");

        assertThat(processInstanceStatusCode).isEqualTo(statusCode);
    }

}
