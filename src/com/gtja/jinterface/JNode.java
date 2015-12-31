package com.gtja.jinterface;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class JNode extends OtpNode {

	public JNode(String nodeName) throws IOException, OtpErlangException {
		this(nodeName, defaultCookie);
	}
	
	public JNode(String nodeName, String cookie) throws IOException, OtpErlangException {
		super(nodeName, cookie, 0);	
		if(GlobalNodesView.getAllNodes().isEmpty()){
			GlobalNodesView.addNode(this);
		}		
	}
	
	/**
	 * Send message to another node.
	 * 
	 * @param srcm	the source mailbox name.
	 * @param destn	the destination node name.
	 * @param destm	the destination mailbox name.
	 * @param msg	the message body to send.
	 */
	public void sendMessage(String srcm, String destn, String destm, final OtpErlangObject msg){
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
		sendMessage(srcm, destn, "rex", new OtpErlangTuple(msg));
	}
	
	/**
	 * Receive message using a mailbox on this node.
	 * 
	 * @param mailbox	the mailbox used to receive.
	 * @return OtpErlangObject
	 */
	public OtpErlangObject receiveMessage(String mailbox) {
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
	
	public Socket createSocketConnection(String peer, int port) throws UnknownHostException, IOException, OtpErlangException {
		String srcm = "mbox1";
		createMbox(srcm);
		String mod = "socket_server";
		String fun = "start";
		OtpErlangInt p = new OtpErlangInt(port);
		sendRPC("mbox1", peer, mod, fun, p);
		OtpErlangObject rec = receiveMessage(srcm);
		if(MessageUtil.checkItem(2, 0, "badrpc", rec)){
			System.out.println("server error");
			return null;	
		}else{
			Socket socket;
			if(GlobalNodesView.contains(peer)){
				socket = new Socket(GlobalNodesView.getNode(peer).host(), port);
			}else{
				OtpPeer otppeer = new OtpPeer(peer);
	    	    socket = new Socket(otppeer.host(), port);
			}
	    	return socket;
		}		
	}
	
	public Hashtable<String, OtpCookedConnection> getConnections() {
		return connections;
	}
	
	public Set<String> getConnectingNodes() {
		Set<String> nodes = getConnections().keySet();
		return nodes;
	}


	@Override
	public void close() {
		GlobalNodesView.removeNode(this.node());
		super.close();
	}
	
	
	
}
