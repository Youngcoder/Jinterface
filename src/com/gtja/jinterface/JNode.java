package com.gtja.jinterface;
import java.io.IOException;

public class JNode extends OtpNode {
	
	private Acceptor acceptor;

	public JNode(final String nodeName) throws IOException {
		super(nodeName);
	}
	
	public JNode(final String nodeName, final String cookie) throws IOException {
		super(nodeName, cookie, 0);
	}
	
	public OtpMbox addMailbox(String name) {
		return createMbox(name);
	}
	
	public OtpMbox addMailbox() {
		return createMbox();
	}
	
	/**
	 * Send message to another node.
	 * 
	 * @param srcm	the source mailbox name.
	 * @param destn	the destination node name.
	 * @param destm	the destination mailbox name.
	 * @param msg	the message body to send.
	 */
	public void sendMsg(String srcm, String destn, String destm, final OtpErlangObject msg){
		OtpMbox srcmbox = getMailboxes().get(srcm);
		srcmbox.send(destm, destn, msg);
	}
	
	/**
	 * Do remote procedure call.
	 * 
	 * @param srcm	the source mailbox name.
	 * @param destn	the destination node name.
	 * @param mod	the name of the module.
	 * @param fun	the name of the function.
	 * @param arguments	the arguments of the calling function.
	 */
	public void sendRPC(String srcm, String destn, 
			String mod, String fun,OtpErlangObject ...arguments) {
		
		OtpErlangObject[] call = new OtpErlangObject[5];
		OtpErlangObject[] msg = new OtpErlangObject[2];
		OtpErlangList args = new OtpErlangList(arguments);
		call[0] = new OtpErlangAtom("call");
		call[1] = new OtpErlangAtom(mod);
		call[2] = new OtpErlangAtom(fun);
		call[3] = args;
		call[4] = new OtpErlangAtom("user");
		OtpErlangTuple calltuple = new OtpErlangTuple(call);
		msg[0] = getMailboxes().get(srcm).self();
		msg[1] = calltuple;
		sendMsg(srcm, destn, "rex", new OtpErlangTuple(msg));
	}
	
	/**
	 * Receive message using a mailbox on this node.
	 * 
	 * @param mailbox	the mailbox used to receive.
	 * @return OtpErlangObject
	 */
	public OtpErlangObject receiveMsg(String mailbox) {
		OtpErlangObject msg = null;
		try {
			msg = getMailboxes().get(mailbox).receive();
		} catch (OtpErlangExit e) {
			e.printStackTrace();
		} catch (OtpErlangDecodeException e) {
			e.printStackTrace();
		} 	
		return msg;				
	}
	
	
	
	
}
