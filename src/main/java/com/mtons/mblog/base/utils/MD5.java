/*
+--------------------------------------------------------------------------
|   mtons [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.base.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5摘要
 */
public class MD5 {

	/**
	 * 对字符串进行Md5加密
	 *
	 * @param input 原文
	 * @return md5后的密文
	 */
	public static String md5(String input) {
		byte[] code = null;
		try {
			code = MessageDigest.getInstance("md5").digest(input.getBytes());
		} catch (NoSuchAlgorithmException e) {
			code = input.getBytes();
		}
		BigInteger bi = new BigInteger(code);
		return bi.abs().toString(32).toUpperCase();
	}

	/**
	 * 对字符串进行Md5加密
	 *
	 * @param input 原文
	 * @param salt 随机数
	 * @return string
	 */
	public static String md5(String input, String salt) {
		if(StringUtils.isEmpty(salt)) {
			salt = "";
		}
		return md5(salt + md5(input));
	}

	/**
	 * 文件md5计算
	 *
	 * @param bytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5File(byte[] bytes)  {
		byte[] code = new byte[0];
		try {
			code = MessageDigest.getInstance("md5").digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		BigInteger bi = new BigInteger(code);
		return bi.abs().toString(32).toUpperCase();
	}
	
	
	/**
     * MD5加密生成32位小写字符串
     */
    public static String encrypt(String msg) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = msg.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte)charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int)md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
        
    }

}
