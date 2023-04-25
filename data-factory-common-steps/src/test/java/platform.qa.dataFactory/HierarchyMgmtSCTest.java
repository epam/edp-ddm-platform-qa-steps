package platform.qa.dataFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import platform.qa.data.hierarchymanagement.HierarchyMgmtSC;
import platform.qa.pojo.hierarchymanagement.Unit;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HierarchyMgmtSCTest {

    HierarchyMgmtSC hierarchyMgmtSC = mock(HierarchyMgmtSC.class);

    @Test
    public void hierarchyMgmtSCTest() {
        when(hierarchyMgmtSC.getAllUnits()).thenReturn(List.of(new Unit("unit1", "unit1", "unit1", "unit1")));

        var unitsCount = hierarchyMgmtSC.getAllUnits().size();

        Assertions.assertThat(unitsCount).isEqualTo(1);

    }
}
