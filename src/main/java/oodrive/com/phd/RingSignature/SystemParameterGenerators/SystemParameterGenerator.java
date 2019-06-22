package oodrive.com.phd.RingSignature.SystemParameterGenerators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

public class SystemParameterGenerator {
	public PairingParameters pairingParams;

	public SystemParameterGenerator(PairingParameters params){
		this.pairingParams = params;
	}
	
	public SystemParameterGenerator(){
		TypeACurveGenerator generator = new TypeACurveGenerator(160, 512);
		this.pairingParams = generator.generate();
	}
	
	public PairingParameters getPairingParameters(){
		return this.pairingParams;
	}
	
}
