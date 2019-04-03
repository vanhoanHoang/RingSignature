package oodrive.com.phd.RingSignature;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import it.unisa.dia.gas.jpbc.Element;
import oodrive.com.phd.RingSignature.Parameters.PrivateKeyParameters;
import oodrive.com.phd.RingSignature.Parameters.PublicKeyParameters;
import oodrive.com.phd.RingSignature.Parameters.SignatureParameters;
import oodrive.com.phd.RingSignature.Parameters.SystemParameters;
import oodrive.com.phd.RingSignature.Resources.RingParameters;
import oodrive.com.phd.RingSignature.Resources.SigningData;
import oodrive.com.phd.RingSignature.SystemParameterGenerators.SystemParameterGenerator;

public class MainEngine {

	public static SystemParameters systemParams;

	public static void systemInitializing(PrivateKeyParameters privateKeyParams, PublicKeyParameters publicKeyParams,
			ArrayList<Element> ringPublicKeysList) {
		SystemParameterGenerator systemParameterGenerator = new SystemParameterGenerator();
		systemParams = new SystemParameters(systemParameterGenerator);

		Element g = systemParams.getGenerator().duplicate();
		privateKeyParams.setPrivateKey(systemParams.getPairing().getZr().newRandomElement());

		Element publicKey = systemParams.getPairing().getG1().newRandomElement();
		publicKey = g.duplicate();
		publicKey.powZn(privateKeyParams.getPrivate());
		publicKeyParams.setPublicKey(publicKey);

		for (int i = 0; i < RingParameters.RING_SIZE - 1; i++) {

			Element rawPublicKey = systemParams.getPairing().getG1().newRandomElement();
			rawPublicKey = g.duplicate();
			Element exponent = systemParams.getPairing().getZr().newRandomElement();
			rawPublicKey.powZn(exponent);

			ringPublicKeysList.add(rawPublicKey);
		}
		ringPublicKeysList.add(publicKeyParams.getPublicKey());

	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		PrivateKeyParameters privateKeyParams = new PrivateKeyParameters();
		PublicKeyParameters publicKeyParams = new PublicKeyParameters();
		ArrayList<Element> ringPublicKeysList = new ArrayList<Element>();

		systemInitializing(privateKeyParams, publicKeyParams, ringPublicKeysList);

		System.out.println("\n Signing message: " + SigningData.data + "...");
		SigningEngine signingEngine = new SigningEngine(systemParams);

		Element e = systemParams.getPairing().getG1().newRandomElement();

		SignatureParameters signatureParameters = signingEngine.ringSigning(privateKeyParams, SigningData.data,
				ringPublicKeysList);
		System.out.println("\n Verifying the signature...");
		VerifyingEngine verifyingEngine = new VerifyingEngine(systemParams);
		if (verifyingEngine.ringVerifing(signatureParameters, ringPublicKeysList)) {
			System.out.println("\n The scheme is well implemented");
		} else {
			System.out.println("\n The scheme is not well implemened");
		}
	}

}
