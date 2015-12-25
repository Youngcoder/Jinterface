package com.gtja.jinterface;

public class MessageUtil {

	public static void resolveMessage(OtpErlangObject msg) {
		if(msg instanceof OtpErlangTuple){
			OtpErlangTuple tuple = (OtpErlangTuple) msg;
			int len = tuple.arity();
			for(int i = 0; i < len; i++) {	
				resolveMessage(tuple.elementAt(i));
			}
		}else{
			System.out.println(msg.toString());
		}
	}
	
	public static OtpErlangObject createMessage(){
		return null;
	}
}
