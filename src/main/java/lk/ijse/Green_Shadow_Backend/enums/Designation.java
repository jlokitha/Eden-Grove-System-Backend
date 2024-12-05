package lk.ijse.Green_Shadow_Backend.enums;

import lombok.Getter;

@Getter
public enum Designation {
    MANAGER("MANAGER"),
    SENIOR_ASSISTANT_MANAGER("MANAGER"),
    ASSISTANT_MANAGER("MANAGER"),
    ADMIN_AND_HR_STAFF("ADMINISTRATIVE"),
    OFFICE_ASSISTANT("OTHER"),
    SENIOR_AGRONOMIST("SCIENTIST"),
    AGRONOMIST("SCIENTIST"),
    SOIL_SCIENTIST("SCIENTIST"),
    SENIOR_TECHNICIAN("OTHER"),
    TECHNICIAN("OTHER"),
    SUPERVISOR("OTHER"),
    LABOR("OTHER");
    private final String role;
    Designation(String role) {
        this.role = role;
    }
}
