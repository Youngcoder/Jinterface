import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpEpmd;
import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import com.ericsson.otp.erlang.OtpPeer;
import com.ericsson.otp.erlang.OtpSelf;


public class ServerRPC {

	
	public static void main(String[] args) throws Exception {
		System.setProperty("OtpConnection.trace", "4");
		//OtpEpmd.useEpmdPort(5678);
		OtpNode node = new OtpNode("node1");
		OtpMbox mailbox0 = node.createMbox("mailbox0");
		OtpMbox mailbox1 = node.createMbox("mailbox1");
		//node.ping("node2", 2000);
		OtpSelf self = new OtpSelf("node1");
		OtpPeer other = new OtpPeer("node2");
		OtpConnection con;
		con = self.connect(other);
		con.sendRPC("os", "timestamp", new OtpErlangList());
		OtpErlangObject rec = con.receiveRPC();
		System.out.println("receive message " + rec.toString());
		//System.out.println(".."+self.pid());
		//OtpConnection con = self.accept();	
		


	}

}
