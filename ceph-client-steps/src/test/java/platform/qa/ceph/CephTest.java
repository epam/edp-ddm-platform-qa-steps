package platform.qa.ceph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.CephClientSteps;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CephTest {
    CephClientSteps cephClientSteps = mock(CephClientSteps.class);
    File file;
    List<String> filesToCheck = List.of(
            "file1", "file2", "file3"
    );

    List<String> filesExistInCephResponse = List.of(
            "fileId1", "fileId2", "fileId3"
    );

    @Test
    public void checkFilesExistInCephTest() {
        when(cephClientSteps.checkFilesExistInCeph(filesToCheck, "processInstanceId"))
                .thenReturn(filesExistInCephResponse);

        var filesInCeph = cephClientSteps
                .checkFilesExistInCeph(filesToCheck, "processInstanceId");

        assertThat(filesInCeph.size()).isEqualTo(3);
        assertThat(filesInCeph).isEqualTo(filesExistInCephResponse);
    }

    @Test
    public void uploadFileToCephExceptionTest() throws IOException {
        when(cephClientSteps.uploadFileToCeph(file, "processId")).thenThrow(IOException.class);

        assertThatThrownBy(() -> {
            cephClientSteps.uploadFileToCeph(file, "processId");
        }).isInstanceOf(IOException.class)
                .hasAllNullFieldsOrProperties();
    }
}
