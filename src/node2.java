import java.io.IOException;

import com.ericsson.otp.erlang.*;
public class node2 {

	public static void main(String[] args) throws Exception {
		//OtpEpmd.useEpmdPort(5678);
		OtpNode node = new OtpNode("node2");
		OtpMbox mbox = node.createMbox("mailbox");
		OtpErlangObject o = mbox.receive();
		OtpErlangTuple msg;
		OtpErlangPid from;
		System.out.println("message received ");
		if(o instanceof OtpErlangTuple) {
			msg = (OtpErlangTuple) o;
			from = (OtpErlangPid) msg.elementAt(0);
			OtpErlangString s = (OtpErlangString) msg.elementAt(1);
			mbox.send(from, s);	
			System.out.println("message replyed");
		}

	}

}
