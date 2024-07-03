package com.peterpopma.easy_epp.dto.epp;

public record OrganizationContact (
   String id,
   String typeName,    // e.g. "legal"
   String type   // admin, billing, tech, abuse or custom
) {}
