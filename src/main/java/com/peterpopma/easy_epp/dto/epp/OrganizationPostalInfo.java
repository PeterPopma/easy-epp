package com.peterpopma.easy_epp.dto.epp;

public record OrganizationPostalInfo (
   String name,
   String org,
   OrganizationAddress addr
) {}
