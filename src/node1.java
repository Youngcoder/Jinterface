import com.ericsson.otp.erlang.OtpEpmd;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;


public class node1 {

	public static void main(String[] args) throws Exception {
		//System.setProperty("OtpConnection.trace", "4");
		//OtpEpmd.useEpmdPort(5678);
		OtpNode node = new OtpNode("node1");
		OtpMbox mailbox = node.createMbox();
		OtpMbox mailbox2 = node.createMbox();
		OtpErlangObject[] msg = new OtpErlangObject[3];
		msg[0] = new OtpErlangAtom("config");
		msg[1] = new OtpErlangAtom("mailbox1");
		msg[2] = new OtpErlangAtom("node1@wuhan");
		OtpErlangTuple tuple = new OtpErlangTuple(msg);
		if(node.ping("node2", 2000)){
			System.out.println("Node 2 is up");
			mailbox.send("mbox", "node2", tuple);
			System.out.println("Message sended..");
//			OtpErlangObject reply = mailbox.receive();
//			OtpErlangObject reply2 = mailbox2.receive();
//			System.out.println("Reply1 from node2 is: " + reply.toString());
//			System.out.println("Reply2 from node2 is: " + reply2.toString());
		}else{
			System.out.println("Node 2 is not up");
		}

	}

}
