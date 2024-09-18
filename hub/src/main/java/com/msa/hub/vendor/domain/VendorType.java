package com.msa.hub.vendor.domain;

public enum VendorType {
    PRODUCER("생산업체"),   // 생산업체
    RECEIVER("수령업체");    // 수령업체

    private final String description;

    VendorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

