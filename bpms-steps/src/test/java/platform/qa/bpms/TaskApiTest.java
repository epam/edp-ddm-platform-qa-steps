package platform.qa.bpms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.TaskApi;
import platform.qa.entities.Task;

import java.util.List;
import org.junit.jupiter.api.Test;

public class TaskApiTest {
    TaskApi taskApi = mock(TaskApi.class);
    int taskCountByProcessInstanceIdResponse = 1;
    List<Task> taskInstancesResponse = List.of(
            Task.builder().id("id1").processInstanceId("processInstanceId1").build(),
            Task.builder().id("id2").processInstanceId("processInstanceId2").build()
    );

    @Test
    public void getTasksInstancesTest() {
        when(taskApi.getTasksInstances()).thenReturn(taskInstancesResponse);

        var taskInstances = taskApi.getTasksInstances();

        assertThat(taskInstances.size()).isEqualTo(2);
        assertThat(taskInstances).isEqualTo(taskInstancesResponse);
    }

    @Test
    public void getTaskCountByProcessInstanceIdTest() {
        when(taskApi.getTaskCountByProcessInstanceId(anyString()))
                .thenReturn(taskCountByProcessInstanceIdResponse);

        var taskCountByProcessInstanceId = taskApi.getTaskCountByProcessInstanceId("processInstanceId");

        assertThat(taskCountByProcessInstanceId).isEqualTo(taskCountByProcessInstanceIdResponse);
    }

}
