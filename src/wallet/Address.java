package wallet;

import java.math.BigInteger;

import crypto.Encrypt;

public class Address 
{
	//�׻� ��Ʈ���� �ּҾտ��� 1�� �ٴ´�
	private String address = "1";
	
	//base64���� Ư������ �� i,O,l�� ������ base58 ���̺� 
	private char[] table = new char[] {'1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	public Address(String publickey) {	// �ּҸ� �����Ҷ� �߻��ϴ� ������ �޼ҵ�
		String temp_key = Encrypt.get_RIPEMD160(new BigInteger(Encrypt.get_SHA256(publickey),16).toByteArray());		
		String check_sum = Encrypt.get_SHA256(Encrypt.get_SHA256(temp_key)).substring(0,8);
		
		address = get_base58(temp_key + check_sum);
	}
	
	private String get_base58(String data){	// �ּҸ� �����ϱ� ���� base58 ��ȣȭ �޼ҵ� 
		BigInteger x = new BigInteger(data,16);
		
		StringBuilder sb = new StringBuilder();
		
	    while (x.compareTo(BigInteger.ZERO) == 1){
	        int r = x.mod(BigInteger.valueOf(58)).intValue();
	        sb.append(table[r]);
	        x = x.divide(BigInteger.valueOf(58));  
	    }
	    
	    return "1" + sb.reverse().toString();
	}
	
	public String get_address(){	// �ּҸ� �����ü��ִ� get �޼ҵ�
		return address;
	}
}
