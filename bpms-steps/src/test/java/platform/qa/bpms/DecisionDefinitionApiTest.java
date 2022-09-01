package platform.qa.bpms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.DecisionDefinitionApi;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DecisionDefinitionApiTest {
    DecisionDefinitionApi decisionDefinitionApi = mock(DecisionDefinitionApi.class);

    Map<String, String> decisionDefinitionByKeyResponse = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("id", "cddbcede-fb9b-11ec-9bab-0a580a801088")
    );

    List<HashMap> allDecisionDefinitionsResponse = List.of(
            new HashMap<String,String>() {{
                put("decisionDefinition1","decisionDefinition1Data");
                put("decisionDefinition2","decisionDefinition2Data");
            }}
    );

    @Test
    public void getDecisionDefinitionByKeyTest() {
       when(decisionDefinitionApi.getDecisionDefinitionByKey(anyString()).get(0))
               .thenReturn(decisionDefinitionByKeyResponse);

       var decisionDefinitionByKey = decisionDefinitionApi
               .getDecisionDefinitionByKey("testKey");

       assertThat(decisionDefinitionByKey.size()).isEqualTo(1);
       assertThat(decisionDefinitionByKey).containsExactlyEntriesOf(decisionDefinitionByKeyResponse);
    }

    @Test
    public void getAllDecisionDefinitionsTest() {
        when(decisionDefinitionApi.getAllDecisionDefinitions())
                .thenReturn(allDecisionDefinitionsResponse);

        var allDecisionDefinitions = decisionDefinitionApi
                .getAllDecisionDefinitions();

        assertThat(allDecisionDefinitions.size()).isEqualTo(1);
        assertThat(allDecisionDefinitions.get(0))
                .containsExactlyEntriesOf(allDecisionDefinitionsResponse.get(0));
    }
}
