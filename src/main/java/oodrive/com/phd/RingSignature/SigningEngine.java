package oodrive.com.phd.RingSignature;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import it.unisa.dia.gas.jpbc.Element;
import oodrive.com.phd.RingSignature.Parameters.PrivateKeyParameters;
import oodrive.com.phd.RingSignature.Parameters.SignatureParameters;
import oodrive.com.phd.RingSignature.Parameters.SystemParameters;
import oodrive.com.phd.RingSignature.Resources.RingParameters;

public class SigningEngine {

	public SystemParameters systemParams;

	public SigningEngine(SystemParameters systemParameters) {
		this.systemParams = systemParameters;
	}

	public SignatureParameters ringSigning(PrivateKeyParameters privateKeyParams, String signingData,
			ArrayList<Element> ringPublicKeysList) throws NoSuchAlgorithmException {
		HashMap<String, Element> RList = new HashMap<String, Element>(RingParameters.RING_SIZE);
		HashMap<String, Element> HList = new HashMap<String, Element>(RingParameters.RING_SIZE);

		Element delta = systemParams.getPairing().getZr().newRandomElement();
		Element a = systemParams.getPairing().getZr().newRandomElement();
		delta = a.duplicate();

		Element g = systemParams.getGenerator().duplicate();
		Element R_s = g.duplicate();
		R_s.powZn(a);

		for (int i = 1; i < RingParameters.RING_SIZE; i++) {

			Element a_i = systemParams.getPairing().getZr().newRandomElement();
			delta.add(a_i);

			Element R_i = systemParams.getPairing().getG1().newRandomElement();
			R_i = g.duplicate();
			R_i.powZn(a_i);
			RList.put(SignatureParameters.RLIST_PREFIX + i, R_i);

			Element H_i = systemParams.getPairing().getZr().newRandomElement();
			PreProcessingEngine.elementFromString(H_i, signingData, new String(R_i.toBytes()));
			HList.put(SignatureParameters.HLIST_PREFIX + i, H_i);

			Element y = systemParams.getPairing().getG1().newRandomElement();
			y = ringPublicKeysList.get(i - 1).duplicate();
			Element h = systemParams.getPairing().getZr().newRandomElement();
			h = H_i.duplicate();
			h.negate();
			y.powZn(h);
			R_s.mul(y);
		}

		RList.put(SignatureParameters.RLIST_PREFIX + RingParameters.RING_SIZE, R_s);

		Element H_s = systemParams.getPairing().getZr().newElement();
		PreProcessingEngine.elementFromString(H_s, signingData, new String(R_s.toBytes()));

		HList.put(SignatureParameters.HLIST_PREFIX + RingParameters.RING_SIZE, H_s);

		Element x_s = systemParams.getPairing().getZr().newRandomElement();
		x_s = privateKeyParams.getPrivate().duplicate();
		x_s.mul(H_s);
		delta.add(x_s);

		SignatureParameters signatureParameters = new SignatureParameters(signingData, RList, HList, delta);
		return signatureParameters;
	}
}
