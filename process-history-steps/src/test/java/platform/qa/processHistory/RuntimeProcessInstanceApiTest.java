package platform.qa.processHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.RuntimeProcessInstanceApi;
import platform.qa.entities.RuntimeProcessInstance;

import java.util.List;
import org.junit.jupiter.api.Test;

public class RuntimeProcessInstanceApiTest {
    RuntimeProcessInstanceApi runtimeProcessInstanceApi = mock(RuntimeProcessInstanceApi.class);
    List<RuntimeProcessInstance> runtimeProcessInstancesListResponse = List.of(
            RuntimeProcessInstance.builder().processInstanceId("id1").build(),
            RuntimeProcessInstance.builder().processInstanceId("id2").build()
    );

    @Test
    public void getRuntimeProcessInstancesListTest() {
        when(runtimeProcessInstanceApi.getRuntimeProcessInstancesList())
                .thenReturn(runtimeProcessInstancesListResponse);

        var runtimeProcessInstancesList = runtimeProcessInstanceApi
                .getRuntimeProcessInstancesList();

        assertThat(runtimeProcessInstancesList.size()).isEqualTo(2);
        assertThat(runtimeProcessInstancesList).isEqualTo(runtimeProcessInstancesListResponse);
    }
}
