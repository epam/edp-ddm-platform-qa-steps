package platform.qa.document;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.DigitalDocumentServiceApi;
import platform.qa.entity.ErrorResponse;
import platform.qa.entity.UploadDocumentResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DigitalDocumentServiceApiTest {
    DigitalDocumentServiceApi digitalDocumentServiceApi = mock(DigitalDocumentServiceApi.class);
    UploadDocumentResponse uploadDocumentResponse = new UploadDocumentResponse();
    ErrorResponse uploadDocumentErrorResponse = new ErrorResponse();

    @Test
    public void uploadDocumentTest() {
        when(digitalDocumentServiceApi.uploadDocument(anyObject(), anyString(), anyString(), anyString()))
                .thenReturn(uploadDocumentResponse);

        var uploadDocument = digitalDocumentServiceApi
                .uploadDocument(anyObject(), anyString(), anyString(), anyString());

        Assertions.assertThat(uploadDocument).isEqualTo(uploadDocumentResponse);
    }

    @Test
    public void uploadDocumentErrorResponse() {
        when(digitalDocumentServiceApi.uploadDocument(anyObject(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(uploadDocumentErrorResponse);

        var uploadDocumentError = digitalDocumentServiceApi
                .uploadDocument(anyObject(), anyString(), anyString(), anyString(), anyInt());

        Assertions.assertThat(uploadDocumentError).isEqualTo(uploadDocumentErrorResponse);
    }
}
