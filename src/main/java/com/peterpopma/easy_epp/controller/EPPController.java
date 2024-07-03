package com.peterpopma.easy_epp.controller;

import com.peterpopma.easy_epp.connection.EPPConnection;
import com.peterpopma.easy_epp.dto.epp.Response;
import com.peterpopma.easy_epp.service.EPPService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class EPPController {
  private final EPPService eppService;

  @PostMapping(value="/epp/{hostname}/{port}")
  public String sendEPP(@PathVariable("hostname") String hostname, @PathVariable("port") Integer port, @RequestBody String command) {
    EPPConnection eppConnection = eppService.getEPPConnection(hostname, port);
    String response = eppConnection.readWrite(command);

    return response;
  }
}
