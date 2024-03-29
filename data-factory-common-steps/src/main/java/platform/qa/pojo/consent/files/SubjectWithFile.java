/*
 * Copyright 2022 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package platform.qa.pojo.consent.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import platform.qa.pojo.consent.Subject;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SubjectWithFile extends Subject {
    private ScanCopy scanCopy;

    @Builder(builderMethodName = "SubjectWithFileBuilder")
    public SubjectWithFile(String consentSubjectId,
            String consentId,
            String legalEntityName,
            String edrpou,
            String roleId,
            ScanCopy scanCopy) {
        super(consentSubjectId, consentId, legalEntityName, edrpou, roleId);
        this.scanCopy = scanCopy;
    }
}
