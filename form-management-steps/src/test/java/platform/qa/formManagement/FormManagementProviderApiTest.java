package platform.qa.formManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.api.FormManagementProviderApi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class FormManagementProviderApiTest {
    FormManagementProviderApi formManagementProviderApi = mock(FormManagementProviderApi.class);
    List<Map> allFormsResponse = List.of(
            Map.of("form1", "id1"),
            Map.of("form2", "id2")
    );
    Map<String, String> formByIdResponse = Map.of(
            "formName1", "id1"
    );

    @Test
    public void getAllFormsTest() {
        AtomicInteger iterator = new AtomicInteger(0);

        when(formManagementProviderApi.getAllForms()).thenReturn(allFormsResponse);

        var allForms = formManagementProviderApi.getAllForms();

        assertThat(allForms.size()).isEqualTo(2);
        allForms.forEach(form ->
                assertThat(form).isEqualTo(allFormsResponse.get(iterator.getAndIncrement())));
    }

    @Test
    public void getFormByIdTest() {
        when(formManagementProviderApi.getFormById(anyString())).thenReturn(formByIdResponse);

        var formById = formManagementProviderApi.getFormById(anyString());

        assertThat(formById.size()).isEqualTo(1);
        assertThat(formById).isEqualTo(formByIdResponse);
    }
}
