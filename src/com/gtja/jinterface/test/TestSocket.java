package com.gtja.jinterface.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gtja.jinterface.*;


public class TestSocket {
	
	JNode node1;

	@Before
	public void setUp() throws Exception {
		System.setProperty("OtpConnection.trace", "4");
		node1 = new JNode("node1");
		node1.registerStatusHandler(new OtpNodeStatus());
	}

	@After
	public void tearDown() throws Exception {
		node1.close();
	}

	@Test
	public void testCreateSocketConnection() throws UnknownHostException, OtpErlangException, IOException {
		Socket socket1 = node1.createSocketConnection("node2@wuhan", 1000);
		Socket socket2 = node1.createSocketConnection("node2@wuhan", 1001);
		Socket socket3 = node1.createSocketConnection("node3@wuhan", 1002);
		OtpErlangObject[] msgs = new OtpErlangObject[2];
		msgs[0] = new OtpErlangAtom("123");
		msgs[1] = new OtpErlangAtom("abc");
		OtpErlangTuple tuple = new OtpErlangTuple(msgs);
	    OtpOutputStream stream = new OtpOutputStream(tuple);
	    OtpErlangObject o = stream.getOtpInputStream(0).read_any();
	    System.out.println("" + o);
	    stream.writeTo(socket1.getOutputStream());
	    stream.writeTo(socket2.getOutputStream());
	    stream.writeTo(socket3.getOutputStream());
	    stream.close();		
	}
	
	@Test
	public void testSocketServer() throws IOException, OtpErlangDecodeException {
		node1.createMbox("mbox");
		ServerSocket server = new ServerSocket(1234);
		Socket s = server.accept();
		InputStream ins = s.getInputStream();
		byte[] buf = new byte[1024];
		ins.read(buf);
		OtpInputStream o = new OtpInputStream(buf);
		System.out.println(o.read_any().toString());
	}

}
