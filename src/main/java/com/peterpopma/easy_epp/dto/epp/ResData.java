package com.peterpopma.easy_epp.dto.epp;

public record ResData(
        ContactObject contactObject,
        DomainObject domainObject,
        HostObject hostObject
) {
}
