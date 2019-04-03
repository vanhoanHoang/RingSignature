package oodrive.com.phd.RingSignature;

import java.util.ArrayList;
import java.util.HashMap;

import it.unisa.dia.gas.jpbc.Element;
import oodrive.com.phd.RingSignature.Parameters.SignatureParameters;
import oodrive.com.phd.RingSignature.Parameters.SystemParameters;
import oodrive.com.phd.RingSignature.Resources.RingParameters;

public class VerifyingEngine {

	public SystemParameters systemParams;
	public boolean isValidated = false;

	public VerifyingEngine(SystemParameters systemParameters) {
		this.systemParams = systemParameters;
	}

	public boolean ringVerifing(SignatureParameters signature, ArrayList<Element> ringPublicKeysList) {
		HashMap<String, Element> HList = signature.getHList();
		HashMap<String, Element> RList = signature.getRList();

		String signingData = new String(signature.getSigningMessage());
		Element delta = systemParams.getPairing().getZr().newRandomElement();
		delta = signature.getDelta().duplicate();

		Element g_delta = systemParams.getGenerator().duplicate();

		g_delta.powZn(delta);

		Element R = systemParams.getPairing().getG1().newRandomElement();
		R.sub(R);

		for (int i = 1; i <= RingParameters.RING_SIZE; i++) {
			Element Ri = systemParams.getPairing().getG1().newElement();
			Element yi = systemParams.getPairing().getG1().newElement();
			Element Hi = systemParams.getPairing().getZr().newElement();

			Ri = RList.get(SignatureParameters.RLIST_PREFIX + i).duplicate();
			Hi = HList.get(SignatureParameters.HLIST_PREFIX + i).duplicate();

			yi = ringPublicKeysList.get(i - 1).duplicate();
			yi.powZn(Hi);
			R.mul(Ri);
			R.mul(yi);
		}
		R.sub(g_delta);

		isValidated = R.isZero();
		return isValidated;
	}
}
