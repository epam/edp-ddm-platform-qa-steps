package platform.qa.bpms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.ProcessDefinitionApi;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ProcessDefinitionApiTest {
    ProcessDefinitionApi processDefinitionApi = mock(ProcessDefinitionApi.class);
    List<String> allDefinitionKeysResponse = List.of(
            "definitionKey1", "definitionKey2"
    );

    Map<String, String> definitionByIdResponse = Map.of(
            "definitionId1", "definitionId1Value"
    );

    @Test
    public void getAllDefinitionKeysTest() {
        when(processDefinitionApi.getAllDefinitionKeys()).thenReturn(allDefinitionKeysResponse);

        var allDefinitionKeys = processDefinitionApi.getAllDefinitionKeys();

        assertThat(allDefinitionKeys.size()).isEqualTo(2);
        assertThat(allDefinitionKeys).isEqualTo(allDefinitionKeysResponse);
    }

    @Test
    public void getDefinitionByIdTest() {
        when(processDefinitionApi.getDefinitionById(anyString())).thenReturn(definitionByIdResponse);

        var definitionById = processDefinitionApi.getDefinitionById("definitionId");

        assertThat(definitionById.size()).isEqualTo(1);
        assertThat(definitionById).isEqualTo(definitionByIdResponse);
    }

}
