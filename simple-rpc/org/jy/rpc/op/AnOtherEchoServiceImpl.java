package org.jy.rpc.op;

public class AnOtherEchoServiceImpl implements AnOtherEchoService{

	@Override
	public String anOtherEcho(String str) {
		// TODO Auto-generated method stub
		return "from remote impl " + str;
	}
	
}
