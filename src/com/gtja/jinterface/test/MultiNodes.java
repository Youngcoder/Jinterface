package com.gtja.jinterface.test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gtja.jinterface.JNode;
import com.gtja.jinterface.MessageUtil;
import com.gtja.jinterface.OtpErlangObject;

public class MultiNodes {

	public static void main(String[] args) throws IOException {
		System.setProperty("OtpConnection.trace", "4");
		JNode jnode1 = new JNode("jnode1","abc");
		JNode jnode2 = new JNode("jnode2","abc");
		JNode jnode3 = new JNode("jnode3","abc");
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new NodeWork(jnode1));
		exec.execute(new NodeWork(jnode2));
		exec.execute(new NodeWork(jnode3));		

	}

}
class NodeWork implements Runnable {
	
	private JNode jnode;
	
	public NodeWork(JNode node) {
		jnode = node;
	}
	
	public void run() {
		jnode.addMailbox("rex");
		jnode.addMailbox("mailbox");

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					OtpErlangObject rec1 = jnode.receiveMsg("rex");
					System.out.println(jnode.node() + "receive");
					MessageUtil.resolveMessage(rec1);	
				}		
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					OtpErlangObject rec2 = jnode.receiveMsg("mailbox");
					System.out.println(jnode.node() + "receive");
					MessageUtil.resolveMessage(rec2);
				}	
			}
		}).start();
	}
}