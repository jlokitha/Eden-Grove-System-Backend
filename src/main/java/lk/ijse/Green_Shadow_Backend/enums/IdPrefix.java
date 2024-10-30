package lk.ijse.Green_Shadow_Backend.enums;

import lombok.Getter;

@Getter
public enum IdPrefix {
    CROP("C"),
    EQUIPMENT("E"),
    FIELD("F"),
    MONITORING_LOG("M"),
    STAFF("S"),
    USER("U"),
    VEHICLE("V");
    private final String prefix;
    IdPrefix(String prefix) {
        this.prefix = prefix;
    }
}