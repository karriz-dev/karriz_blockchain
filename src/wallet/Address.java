package wallet;

import java.math.BigInteger;

import crypto.Encrypt;

public class Address 
{
	//항상 비트코인 주소앞에는 1이 붙는다
	private String address = "1";
	
	//base64에서 특수문자 및 i,O,l을 제외한 base58 테이블 
	private char[] table = new char[] {'1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	public Address(String publickey) {	// 주소를 생성할때 발생하는 생성자 메소드
		String temp_key = Encrypt.get_RIPEMD160(new BigInteger(Encrypt.get_SHA256(publickey),16).toByteArray());		
		String check_sum = Encrypt.get_SHA256(Encrypt.get_SHA256(temp_key)).substring(0,8);
		
		address = get_base58(temp_key + check_sum);
	}
	
	private String get_base58(String data){	// 주소를 생성하기 위한 base58 암호화 메소드 
		BigInteger x = new BigInteger(data,16);
		
		StringBuilder sb = new StringBuilder();
		
	    while (x.compareTo(BigInteger.ZERO) == 1){
	        int r = x.mod(BigInteger.valueOf(58)).intValue();
	        sb.append(table[r]);
	        x = x.divide(BigInteger.valueOf(58));  
	    }
	    
	    return "1" + sb.reverse().toString();
	}
	
	public String get_address(){	// 주소를 가져올수있는 get 메소드
		return address;
	}
}
