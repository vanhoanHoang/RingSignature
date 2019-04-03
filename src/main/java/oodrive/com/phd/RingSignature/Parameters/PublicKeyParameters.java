package oodrive.com.phd.RingSignature.Parameters;

import it.unisa.dia.gas.jpbc.Element;

public class PublicKeyParameters {
	public Element publicKey;

	public PublicKeyParameters() {
		
	}
	
	public PublicKeyParameters(Element publicKey) {
		this.publicKey = publicKey;
	}

	public void setPublicKey(Element publicKey) {
		this.publicKey = publicKey;
	}

	public Element getPublicKey() {
		return this.publicKey;
	}
}
