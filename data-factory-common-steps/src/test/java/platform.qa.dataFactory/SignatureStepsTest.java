package platform.qa.dataFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.data.common.SignatureSteps;

import org.junit.jupiter.api.Test;

public class SignatureStepsTest {
    SignatureSteps signatureSteps = mock(SignatureSteps.class);

    @Test
    public void signDeleteRequestTest() {
        when(signatureSteps.signDeleteRequest("id")).thenReturn("signatureKey");

        var signDeleteRequest = signatureSteps.signDeleteRequest("id");

        assertThat(signDeleteRequest).isEqualTo("signatureKey");
    }

}
