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
	
	public static boolean checkItem(int level, int index, String ref, OtpErlangObject msg) {
		
		OtpErlangTuple tuple = (OtpErlangTuple) msg;
		if(level != 1){
			int len = tuple.arity();
			for(int i = 0; i < len; i++) {	
				if(tuple.elementAt(i) instanceof OtpErlangTuple){
					return checkItem(level - 1, index, ref, tuple.elementAt(i));
				}
			}
		}
		return tuple.elementAt(index).toString().equals(ref);
	}
	
	
	public static OtpErlangObject createTuple(OtpErlangObject ...erlangObjects){
		return new OtpErlangTuple(erlangObjects);
	}
	
	public static OtpErlangObject createList(OtpErlangObject ...erlangObjects){
		return new OtpErlangList(erlangObjects);
	}
	
}
