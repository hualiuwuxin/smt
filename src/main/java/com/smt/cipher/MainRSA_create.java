package com.smt.cipher;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.smt.cipher.unsymmetry.RSAUtils;

public class MainRSA_create {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		Map<String,String> map  = RSAUtils.createKeys(1024);
		
		
		String publicKey = map.get("publicKey");
		String privateKey = map.get("privateKey");
		
		System.out.println( publicKey );
		System.out.println( privateKey );
	}

}
