import com.ericsson.otp.erlang.OtpEpmd;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;


public class Client {

	public static void main(String[] args) throws Exception {
		System.setProperty("OtpConnection.trace", "4");
		OtpEpmd.useEpmdPort(5678);
		OtpNode node = new OtpNode("client");
		OtpMbox mailbox = node.createMbox();
		OtpErlangObject[] msg = new OtpErlangObject[2];
		msg[0] = mailbox.self();
		msg[1] = new OtpErlangAtom("hello world");
		OtpErlangTuple tuple = new OtpErlangTuple(msg);
		if(node.ping("server", 2000)){
			System.out.println("server is up");
		}else{
			System.out.println("server is not up");
		}	
		mailbox.send("echo", "server", tuple);
		System.out.println("message sended");
		OtpErlangObject reply = mailbox.receive();
		System.out.println("reply: " + reply.toString());

	}

}
