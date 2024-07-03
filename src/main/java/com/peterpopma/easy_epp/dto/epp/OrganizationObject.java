package com.peterpopma.easy_epp.dto.epp;

import java.util.List;

public record OrganizationObject (
   String id,
   String roid,
   OrganizationRole role,
   String status,
   List<OrganizationPostalInfo> postalInfoList,

   String voice,
   String fax,
   String email,
   String url,
   List<OrganizationContact> contacts,
   String crID,
   String crDate,
   String upID,
   String upDate
) {}
