package com.gtja.jinterface.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gtja.jinterface.JNode;
import com.gtja.jinterface.MessageUtil;
import com.gtja.jinterface.OtpErlangInt;
import com.gtja.jinterface.OtpErlangObject;
import com.gtja.jinterface.OtpMbox;
import com.gtja.jinterface.OtpNodeStatus;

public class TestMessage {
	
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
	public void testSendMessage() {
		OtpMbox source = node1.createMbox("source");
		String destnode = "node2@wuhan";
		String destmbox = "mbox";
		OtpErlangObject msg = MessageUtil.createTuple(source.self(),new OtpErlangInt(1),new OtpErlangInt(2));
		node1.sendMessage("source", destnode, destmbox, msg);
	}
	
	@Test
	public void testReceiveMessage() {
		node1.createMbox("receiver");
		OtpErlangObject msg = node1.receiveMessage("receiver");
		System.out.println("receive message: " + msg);
	}
	
	@Test
	public void testSendRPC() {
		OtpMbox source = node1.createMbox("source");
		String destnode = "node2@wuhan";
		String destmbox = "mbox";
		node1.sendRPC("source", destnode, "test", "run");
		OtpErlangObject rec = node1.receiveMessage("source");
		if(MessageUtil.checkItem(2, 0, "badrpc", rec)){
			System.out.println("error");
		}else{
			System.out.println("succces");
		}
	}

}
