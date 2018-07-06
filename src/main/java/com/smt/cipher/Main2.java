package com.smt.cipher;

import java.security.NoSuchAlgorithmException;

import com.smt.cipher.symmetry.DESUtil;

public class Main2 {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(   DESUtil.encrypt("测试文本DES", "abcdabcd" ) );
		System.out.println(   DESUtil.descrypt( DESUtil.encrypt("测试文本DES", "abcdabcd" ) , "abcdabcd"));
	}

}
