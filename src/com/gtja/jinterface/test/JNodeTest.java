package com.gtja.jinterface.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gtja.jinterface.JNode;
import com.gtja.jinterface.OtpErlangAtom;
import com.gtja.jinterface.OtpErlangInt;
import com.gtja.jinterface.OtpErlangObject;
import com.gtja.jinterface.OtpMbox;

public class JNodeTest {
	
	private JNode jnode;
	
	@Before
	public void init() {
		try {
			Runtime.getRuntime().exec("epmd", new String[]{"daemon"});
			jnode = new JNode("node1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testJNodeString() {
		try {
			JNode jnode = new JNode("node1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testgetMbox(){
		jnode.getMailboxes();
	}

	@Test
	public void testJNodeStringString() {
		try {
			JNode jnode = new JNode("node1","abc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAddMailboxString() {
		OtpMbox mbox = jnode.addMailbox("mailbox1");
		if(mbox == null){
			fail("name already in use");
		}
		Assert.assertSame(mbox, jnode.getMailboxes().get("mailbox1"));
		
	}

	@Test
	public void testAddMailbox() {
		jnode.addMailbox();
	}

	@Test
	public void testSendMsg() {
		jnode.addMailbox("srcm");
		JNode dnode;
		try {
			dnode = new JNode("destn");
			dnode.addMailbox("destm");
			OtpErlangObject msg = new OtpErlangAtom("test message");
			jnode.sendMsg("srcm", "destn", "destm", msg);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testSendRPC() {
		jnode.addMailbox("srcm");
		jnode.sendRPC("srcm", "node2", "test", "run", new OtpErlangInt(123),new OtpErlangInt(456));
	}

	@Test
	public void testReceiveMsg() {
		jnode.addMailbox("mailbox1");
		OtpErlangObject rec = jnode.receiveMsg("mailbox1");
		System.out.println(rec.toString());
	}

	@After
	public void destroy() {
		jnode.close();
	}
}
