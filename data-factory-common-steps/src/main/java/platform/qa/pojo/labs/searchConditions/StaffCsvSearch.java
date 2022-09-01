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

package platform.qa.pojo.labs.searchConditions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import platform.qa.entities.IEntity;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StaffCsvSearch implements IEntity {
    private String laboratoryId;
    private String specializationDate;
    private boolean hygienistFlag;
    private String fullName;
    private String staffId;
    private boolean fullTimeFlag;
    private Double salary;

    public static StaffCsvSearch getEmptyEntity(){
        return new StaffCsvSearch();
    }
}
