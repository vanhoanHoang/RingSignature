package oodrive.com.phd.RingSignature.Parameters;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import oodrive.com.phd.RingSignature.SystemParameterGenerators.SystemParameterGenerator;

public class SystemParameters {
	public Pairing pairing; 
	public Element g; // The generator of the group
	
	
	public SystemParameters(SystemParameterGenerator systemParameterGenerator){
		this.pairing = PairingFactory.getPairing(systemParameterGenerator.getPairingParameters());
		g = this.pairing.getG1().newRandomElement();
	}

	public Pairing getPairing() {
		return pairing;
	}

	public Element getGenerator() {
		return g;
	}
	
}
