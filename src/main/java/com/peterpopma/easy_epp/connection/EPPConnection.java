package com.peterpopma.easy_epp.connection;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class EPPConnection {

	private SocketUtil socketUtil = null;

	public String readWrite(String msg) throws IOException {
		socketUtil.write(msg);
		return socketUtil.read();
	}

	public void send(String msg) throws IOException {
		try {
			socketUtil.write(msg);
		} catch (IOException e) {
			log.info("error sending message", e);
		}
	}

	public void connect(String server, int port) throws IOException {
		socketUtil = new SocketUtil();
		try {
			socketUtil.createSocket(server, port, 6000); // 6 sec timeout
			// read the <hello> message from the socket
			socketUtil.read();
		} catch (IOException e) {
			log.info("error connecting", e);
		}
	}

	public void disConnect()  throws IOException {
		try {
			if (socketUtil != null) {
				socketUtil.closeSocket();
			}
		} catch (IOException e) {
			log.info("error disconnecting", e);
		}
	}

}
