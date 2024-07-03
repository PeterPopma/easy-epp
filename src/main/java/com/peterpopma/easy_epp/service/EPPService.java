package com.peterpopma.easy_epp.service;

import com.peterpopma.easy_epp.connection.EPPConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EPPService {
    private EPPConnection eppConnection = new EPPConnection();
    private String currentHostName;
    private int currentPort;

    public EPPConnection getEPPConnection(String hostName, int port) {
        if (!hostName.equals(currentHostName) || port!=currentPort)
        {
            currentHostName = hostName;
            currentPort = port;
            eppConnection.disConnect();
            eppConnection.connect(hostName, port);
        }

        return eppConnection;
    }
}
