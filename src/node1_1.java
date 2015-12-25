import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

import com.ericsson.otp.erlang.OtpAuthException;
import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpEpmd;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangRangeException;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import com.ericsson.otp.erlang.OtpPeer;
import com.ericsson.otp.erlang.OtpSelf;


public class node1_1 {

	public static void main(String[] args) {
		//System.setProperty("OtpConnection.trace", "4");
		OtpNode node = null;
		OtpMbox mailbox0 = null;
		OtpMbox mailbox1 = null;
		OtpMbox mailbox2 = null;
		try {
			node = new OtpNode("node1");
			mailbox0 = node.createMbox("mailbox0");
			mailbox1 = node.createMbox("mailbox1");
			mailbox2 = node.createMbox("mailbox2");
		} catch (IOException e) {
			e.printStackTrace();
		}
		OtpErlangObject[] msg = new OtpErlangObject[3];
		if(mailbox0 != null && mailbox1 != null){
			msg[0] = mailbox0.self();
			msg[1] = new OtpErlangInt(1);
			msg[2] = new OtpErlangInt(2);
			OtpErlangTuple tuple = new OtpErlangTuple(msg);
			String mod = "c";
			String fun = "c";
			OtpErlangList arguments = new OtpErlangList(new OtpErlangObject[]{new OtpErlangAtom("testc")});
			Thread rpc = new Thread(new RemoteProcedureCall(mailbox2, "node2", mod, fun, arguments));
			rpc.start();	
			Thread client = new Thread(new SendMessage(mailbox0, "mbox", "node2", tuple));
			client.start();
			Thread server = new Thread(new ReceiveMessage(mailbox1));
			server.start();
		}

	}
}

class SendMessage implements Runnable {
	
	OtpMbox mbox;
	OtpErlangObject msg;
	String mailbox_to;
	String targetNode;
	
	public SendMessage(OtpMbox mbox,String mbox_to,String node_to,
			OtpErlangObject message){
		this.mbox = mbox;
		this.msg = message;
		this.mailbox_to = mbox_to;
		this.targetNode = node_to;
	}

	@Override
	public void run() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				doSend(msg);
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				doReceive();
			}
		}).start();
		
	}
	
	public void doSend(OtpErlangObject msg){
		Random rand = new Random();
		int counts = 0;
		while(true){
			long time = rand.nextInt(2000);
			try {
				Thread.sleep(time);
				mbox.send(mailbox_to, targetNode, msg);
				counts++;
				System.out.println("send request to " + targetNode + ": " + counts);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void doReceive(){
		int countr = 0;
		while(true){
			try {
				msg = mbox.receive();
				countr++;
				System.out.println("response from node2 is: " + msg.toString() + countr);
			} catch (OtpErlangExit e) {
				e.printStackTrace();
			} catch (OtpErlangDecodeException e) {
				e.printStackTrace();
			}
		}
	}
	
}

class RemoteProcedureCall implements Runnable{
	
	OtpErlangObject[] call = new OtpErlangObject[5];
	OtpErlangObject[] msg = new OtpErlangObject[2];
	OtpMbox mbox;
	String targetnode;
	
	public RemoteProcedureCall(OtpMbox mbox,String node_to,String mod, String fun,
		    OtpErlangList args){
		this.mbox = mbox;
		this.targetnode = node_to;
		call[0] = new OtpErlangAtom("call");
		call[1] = new OtpErlangAtom(mod);
		call[2] = new OtpErlangAtom(fun);
		call[3] = args;
		call[4] = new OtpErlangAtom("user");
		OtpErlangTuple calltuple = new OtpErlangTuple(call);
		msg[0] = mbox.self();
		msg[1] = calltuple;
	}

	@Override
	public void run() {
		OtpErlangObject result = call(targetnode, new OtpErlangTuple(msg));
		System.out.println("RPC result is " + result.toString());
	}
	
	public OtpErlangObject call(String targetnode, OtpErlangObject msg){
		mbox.send("rex", targetnode, msg);
		OtpErlangObject result = null;
		try {
			result = mbox.receive();
		} catch (OtpErlangExit | OtpErlangDecodeException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

class ReceiveMessage implements Runnable {
	
	OtpMbox mbox;
	OtpErlangObject msg;
	
	public ReceiveMessage(OtpMbox mbox){
		this.mbox = mbox;
	}

	@Override
	public void run() {
		OtpErlangObject o;
		OtpErlangTuple msg;
		OtpErlangPid from;
		OtpErlangLong first;
		OtpErlangLong second;
		while(true){
			try {
				o = mbox.receive();
				if(o instanceof OtpErlangTuple) {
					msg = (OtpErlangTuple) o;
					from = (OtpErlangPid) msg.elementAt(0);
					first = (OtpErlangLong) msg.elementAt(1);
					second = (OtpErlangLong) msg.elementAt(2);
					mbox.send(from, new OtpErlangLong(first.intValue()+second.intValue()) );
				}
			} catch (OtpErlangExit e) {
				e.printStackTrace();
			} catch (OtpErlangDecodeException e) {
				e.printStackTrace();
			} catch (OtpErlangRangeException e) {
				e.printStackTrace();
			}
		}
	}
	
}