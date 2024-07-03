package com.peterpopma.easy_epp.dto.epp;

public record ContactPostalInfo(String name,
        String org,
        ContactAddress addr
) {}
