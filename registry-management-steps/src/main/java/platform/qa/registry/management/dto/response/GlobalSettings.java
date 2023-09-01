package platform.qa.registry.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSettings {
    private String titleFull;
    private String title;
    private String themeFile;
    private String supportEmail;
    private List<String> blacklistedDomains;
}
