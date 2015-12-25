import com.ericsson.otp.erlang.*;


public class Server {

	public static void main(String[] args) throws Exception {
		//System.setProperty("OtpConnection.trace", "4");
		OtpEpmd.useEpmdPort(5678);
		OtpNode self = new OtpNode("server");
		OtpMbox mbox = self.createMbox("echo");
		OtpErlangObject o;
		OtpErlangTuple msg;
		OtpErlangPid from;
		int count = 0;
		while(true) {
			System.out.println("listening..");
			try{
				o = mbox.receive();
				count++;
				System.out.println("message received "+count);
				if(o instanceof OtpErlangTuple) {
					msg = (OtpErlangTuple) o;
					from = (OtpErlangPid) msg.elementAt(0);
					mbox.send(from, new OtpErlangTuple(new OtpErlangObject[]{msg.elementAt(1),new OtpErlangAtom("from server")}));
					
					System.out.println("message sended");
				}
			}catch(Exception e){
				System.out.println(e.toString());
			}
		}

	}

}
