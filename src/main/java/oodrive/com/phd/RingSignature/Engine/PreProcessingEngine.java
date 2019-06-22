package oodrive.com.phd.RingSignature.Engine;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.unisa.dia.gas.jpbc.Element;

public class PreProcessingEngine {
	
	public static void elementFromString(Element h, String s1, String s2) throws NoSuchAlgorithmException {

		StringBuilder builder = new StringBuilder();
		builder.append(s1);
		builder.append(s2);

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] digest = md.digest(builder.toString().getBytes());
		h.setFromHash(digest, 0, digest.length);
	}
}
