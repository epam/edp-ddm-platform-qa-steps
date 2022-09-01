package platform.qa.bpms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.DeploymentApi;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DeploymentApiTest {
    DeploymentApi deploymentApi = mock(DeploymentApi.class);

    List<HashMap> allDeploymentsResponse = List.of(
            new HashMap<String,String>() {{
                put("deployment1","deployment1Data");
                put("deployment2","deployment2Data");
            }}
    );

    @Test
    public void deleteDeploymentByIdTest() {
       doNothing().when(deploymentApi).deleteDeploymentById(anyString());

       deploymentApi.deleteDeploymentById("deploymentId");
    }

    @Test
    public void getAllDeploymentsTest() {
        when(deploymentApi.getAllDeployments()).thenReturn(allDeploymentsResponse);

        var allDeployments = deploymentApi.getAllDeployments();

        assertThat(allDeployments.size()).isEqualTo(1);
        assertThat(allDeployments.get(0)).containsExactlyEntriesOf(allDeploymentsResponse.get(0));
    }
}
