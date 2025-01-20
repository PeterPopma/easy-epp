package com.peterpopma.easy_epp.controller;

import com.peterpopma.easy_epp.connection.EPPConnection;
import com.peterpopma.easy_epp.service.EPPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EPPController {
  private final EPPService eppService;

  @PostMapping(value="/epp/{hostname}/{port}")
  public String sendEPP(@PathVariable("hostname") String hostname, @PathVariable("port") Integer port, @RequestBody String command, @RequestParam(value = "useSsl", defaultValue = "false") boolean useSsl) {
    String response;
    try {
      EPPConnection eppConnection = eppService.getEPPConnection(hostname, port, useSsl);
      response = eppConnection.readWrite(command);
    } catch (IOException e) {
      try {
        // connection may have been closed. reconnect and try once more.
        // note that you now need to do an epp login again, so this part may be not so valuable.
        EPPConnection eppConnection = eppService.getEPPConnection(hostname, port, useSsl, true);
        response = eppConnection.readWrite(command);
      } catch (IOException ex) {
        log.debug("error reading/writing message", ex);
        response = "read/write error";
      }
    }

    return response;
  }
}
