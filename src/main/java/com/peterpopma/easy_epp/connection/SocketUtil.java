package com.peterpopma.easy_epp.connection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

@Slf4j
public class SocketUtil {

	private Socket tcpSocket = new Socket();

	private final int BUF_SIZE = 4096;
	private DataInputStream in;
	private DataOutputStream out;

	@Value("${epp.tcp.port}")
	private int eppTcpPort;

	public void createSocket(String host, int port, int soTimeout)
			throws IOException {

		tcpSocket.connect(new InetSocketAddress(host, port), soTimeout);
		tcpSocket.setSoTimeout(soTimeout);
		tcpSocket.setKeepAlive(true);
		in = new DataInputStream(new BufferedInputStream(tcpSocket
				.getInputStream()));
		out = new DataOutputStream(new BufferedOutputStream(tcpSocket
				.getOutputStream(), BUF_SIZE));

	}

	public void createSocket(String host, int port) throws IOException {
		this.createSocket(host, port, 0);
	}

	public void createSocket(String host) throws IOException {
		this.createSocket(host, eppTcpPort, 0);
	}

	public void createSSLSocket(String host, int port, int soTimeout) throws IOException {
		// Set up the SSL context to bypass validation
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{
					new X509TrustManager() {
						@Override
						public void checkClientTrusted(X509Certificate[] certs, String authType) {}

						@Override
						public void checkServerTrusted(X509Certificate[] certs, String authType) {}

						@Override
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}
					}
			}, new java.security.SecureRandom());

			// Create an SSLSocketFactory that uses the bypassing SSL context
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
			sslSocket.setSoTimeout(soTimeout);
			sslSocket.setKeepAlive(true);
			this.tcpSocket = sslSocket;
			in = new DataInputStream(new BufferedInputStream(tcpSocket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(tcpSocket.getOutputStream(), BUF_SIZE));
		} catch (Exception e) {
			log.error("Failed to create a secure socket", e);
			throw new IOException("Failed to create secure socket", e);
		}
	}

	public void createSSLSocket(String host, int port) throws IOException {
		this.createSSLSocket(host, port, 0);
	}

	public void createSSLSocket(String host) throws IOException {
		this.createSSLSocket(host, eppTcpPort, 0);
	}

	public boolean isSocketConnected() {
		return tcpSocket != null && tcpSocket.isConnected();
	}

	public boolean isSocketBound() {
		return tcpSocket != null && tcpSocket.isBound();
	}

	public boolean isSocketClosed() {
		return tcpSocket == null || tcpSocket.isClosed();
	}

	public void closeSocket() throws IOException {
		if (out != null) out.flush();
		if (in != null) in.close();
		if (out != null) out.close();
		if (tcpSocket != null) tcpSocket.close();
	}

	public String read() throws IOException {
		// RFC 4934 #4. Data Unit Format
		//
		// The EPP data unit contains two fields: a 32-bit header that describes
		// the total length of the data unit, and the EPP XML instance. The
		// length of the EPP XML instance is determined by subtracting four
		// octets from the total length of the data unit. A receiver must
		// successfully read that many octets to retrieve the complete EPP XML
		// instance before processing the EPP message.
		//
		// EPP Data Unit Format (one tick mark represents one bit position):
		//
		// 0 1 2 3
		// 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
		// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		// | Total Length |
		// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		// | EPP XML Instance |
		// +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+//-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		//
		// Total Length (32 bits): The total length of the EPP data unit
		// measured in octets in network (big endian) byte order. The octets
		// contained in this field MUST be included in the total length
		// calculation.
		//
		// EPP XML Instance (variable length): The EPP XML instance carried in
		// the data unit.
		//
		String eppCommand = null;
		int size = in.readInt() - 4;
		if (size > 0) {
			byte[] inputBuffer = new byte[size];
			in.readFully(inputBuffer, 0, size);
			eppCommand = new String(inputBuffer, StandardCharsets.UTF_8);
		}
		log.debug("receive: " + eppCommand);
		return eppCommand;
	}

	public void write(String str) throws IOException {
		// EPP Protocol requires the first 32 bits to hold the number of octets
		// send.
		log.debug("send: " + str);
		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
		out.writeInt(bytes.length + 4);
		out.write(bytes);
		out.flush();
	}
}
