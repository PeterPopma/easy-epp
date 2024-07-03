Easy EPP - a simple EPP client

Prerequisites: java 21+ installed
Run with: java -jar bin/easy-epp.jar
Usage: 
  - Call with: http://localhost:8099/epp/[hostname]/[EPP port]
  - Put the EPP command in the body.

Example:

curl --request POST \
  --url http://localhost:8099/epp/localhost/700 \
  --header 'Content-Type: text/plain' \
  --data '<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<epp xmlns="urn:ietf:params:xml:ns:epp-1.0">
  <command>
    <login>
      <clID>eppclient</clID>
      <pw>secret</pw>
      <options>
      <version>1.0</version>
      <lang>en</lang>
      </options>
      <svcs>
        <objURI>urn:ietf:params:xml:ns:contact-1.0</objURI>
        <objURI>urn:ietf:params:xml:ns:host-1.0</objURI>
        <objURI>urn:ietf:params:xml:ns:domain-1.0</objURI>
      </svcs>
    </login>
  </command>
</epp>'