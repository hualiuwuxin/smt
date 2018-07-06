package com.smt.cipher.symmetry;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * DES  工具
 * @author ZHANGYUKUN
 *
 */
public class DESUtil {
	public static final String  ENCODING = Charset.defaultCharset().name();
	
	
	/**
	 * 加密
	 * @param data 原文
	 * @param cipherStr 秘钥字符字符串(8的倍数)
	 * @return
	 */
	public static String encrypt( String content , String secretkeyStr ) {
		String rt = "";
		try {
			byte[] encryptData = getDESCipher(secretkeyStr, Cipher.ENCRYPT_MODE ).doFinal(content.getBytes( ENCODING ));
			rt = new String( Base64.getEncoder().encode(encryptData) ,  ENCODING );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}
	
	/**
	 * 解密
	 * @param data 解密
	 * @param cipherStr 秘钥字符串(8的倍数)
	 * @return
	 */
	public static String descrypt(String encrypt , String secretkeyStr) {
		String rt = "";
		try {
			byte[] conctentData = getDESCipher(secretkeyStr, Cipher.DECRYPT_MODE ).doFinal( Base64.getDecoder().decode( encrypt.getBytes( ENCODING ) ) );
			rt = new String( conctentData ,  ENCODING );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}
	
	
	/**
	 * 得到密码
	 * @param secretkeyStr
	 * @param mode
	 * @return
	 */
	public static Cipher getDESCipher( String secretkeyStr,int mode ) {
		int remainder = secretkeyStr.length()%8;
		if( remainder != 0 ) {
			throw new RuntimeException("秘钥字符串必须是8的倍数");
		}
		
		try {
			KeyGenerator keygen =KeyGenerator.getInstance("DES");
			keygen.init(56,new SecureRandom(secretkeyStr.getBytes(ENCODING)));
			SecretKey secretKey  = keygen.generateKey();
			
			Cipher  cipher = Cipher.getInstance("DES");
			cipher.init( mode ,  secretKey);
			return cipher;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
