package oodrive.com.phd.RingSignature.Parameters;

import java.util.HashMap;

import it.unisa.dia.gas.jpbc.Element;

public class SignatureParameters {
	public static String HLIST_PREFIX ="H" ;
	public static String RLIST_PREFIX = "R";
	
	public String signingMessage;
	public HashMap<String, Element> RList;
	public HashMap<String, Element> HList;
	public Element delta;

	public SignatureParameters(String message, HashMap<String, Element> RList, HashMap<String, Element> HList, Element delta){
		this.signingMessage = new String(message);
		this.RList = new HashMap<String, Element>(RList);
		this.HList = new HashMap<String, Element>(HList);
		this.delta = delta.duplicate();
	}

	public String getSigningMessage() {
		return signingMessage;
	}

	public HashMap<String, Element> getRList() {
		return RList;
	}

	public HashMap<String, Element> getHList() {
		return HList;
	}

	public Element getDelta() {
		return delta;
	}
	
}
