package com.amachi.app.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum CivilStatus {

    SINGLE("Single"),
    MARRIED("Married"),
    DOMESTIC_PARTNERSHIP("Domestic Partnership"),
    SEPARATED("Separated"),
    DIVORCED("Divorced"),
    WIDOWED("Widowed");

    public final String label;

    CivilStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
