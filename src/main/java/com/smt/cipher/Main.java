package com.smt.cipher;

import java.security.NoSuchAlgorithmException;

import com.smt.cipher.symmetry.AESUtil;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(  AESUtil.encrypt("测试文件AES", "abcdabcd") );
		System.out.println(  AESUtil.descrypt( AESUtil.encrypt("测试文件AES", "abcdabcd") , "abcdabcd") );
	}

}
