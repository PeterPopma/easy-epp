package com.peterpopma.easy_epp.dto.epp;

public record Command(
        EppCommand commandType,
        ContactObject contactObject,
        DomainObject domainObject,
        HostObject hostObject,
        OrganizationObject organizationObject
) {
}

