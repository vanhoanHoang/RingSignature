package oodrive.com.phd.RingSignature.Parameters;

import it.unisa.dia.gas.jpbc.Element;

public class PrivateKeyParameters {
	public Element privateKey; 

	public PrivateKeyParameters() {
		
	}
	
	public PrivateKeyParameters(Element privateKey){
		this.privateKey = privateKey;
	}
	
	public void setPrivateKey(Element privateKey) {
		this.privateKey = privateKey;
	}
	
	public Element getPrivate(){
		return this.privateKey;
	}
}
