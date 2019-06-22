package oodrive.com.phd.RingSignature.Engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
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
		
		
		Pairing pairing = systemParams.getPairing();
		
		long start, end; 
		Element e = pairing.getG1().newRandomElement();
		start = System.nanoTime();
		e.mul(e);
		
		end = System.nanoTime();
		System.out.println("\n HHEEHEHE"+ (end-start));
		
		
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

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		FileWriter signFile = new FileWriter("sign.txt", true);
		BufferedWriter signBw = new BufferedWriter(signFile);
		
		FileWriter verifyFile = new FileWriter("verify.txt", true);
		BufferedWriter verifyBw = new BufferedWriter(verifyFile);
		
		
		PrivateKeyParameters privateKeyParams = new PrivateKeyParameters();
		PublicKeyParameters publicKeyParams = new PublicKeyParameters();
		ArrayList<Element> ringPublicKeysList = new ArrayList<Element>();

		systemInitializing(privateKeyParams, publicKeyParams, ringPublicKeysList);

		long startTime, endTime, elapsedTime;
		
		startTime = System.currentTimeMillis();
		System.out.println("\n Signing message: " + SigningData.data + "...");
		SigningEngine signingEngine = new SigningEngine(systemParams);
		SignatureParameters signatureParameters = signingEngine.ringSigning(privateKeyParams, SigningData.data,
				ringPublicKeysList);
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		signBw.write(elapsedTime+"");
		signBw.newLine();
		signBw.close();
		
		System.out.println("\n Sign:  "+elapsedTime);
		
		startTime = System.currentTimeMillis();
		//System.out.println("\n Verifying the signature...");
		VerifyingEngine verifyingEngine = new VerifyingEngine(systemParams);
		if (verifyingEngine.ringVerifing(signatureParameters, ringPublicKeysList)) {
			//System.out.println("\n The scheme is well implemented");
		} else {
			//System.out.println("\n The scheme is not well implemened");
		}
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		verifyBw.write(elapsedTime+"");
		verifyBw.newLine();
		verifyBw.close();
		System.out.println("\n Verify:  "+elapsedTime);
	}

}
