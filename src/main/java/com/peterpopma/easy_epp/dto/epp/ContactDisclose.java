package com.peterpopma.easy_epp.dto.epp;


public record ContactDisclose (
   String flag,
   String nameInt,
   String nameLoc,
   String orgLoc,
   String orgInt,
   String addrLoc,
   String addrInt,
   String voice,
   String fax,
   String email
) {}

