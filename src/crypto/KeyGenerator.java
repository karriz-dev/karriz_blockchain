package crypto;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyGenerator 
{
	public static String get_privatekey(){	// private key를 생성하는 메소드
		String result = "";
		SecureRandom rand = null;
		
		try {
		    rand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
		    e.printStackTrace();
		}
		
		int randomkey_index = 0;
		
		for(randomkey_index=0;randomkey_index<64;randomkey_index++) {
			result += Integer.toHexString(rand.nextInt(16));
		}
		
		return result.toUpperCase();
	}
	
	public static String get_publickey(String privatekey){	// public key를 생성하는 메소드
		BigInteger p = new BigInteger("0FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F",16);
		BigInteger x = BigInteger.ZERO;
        BigInteger y = BigInteger.valueOf(7);
		BigInteger Gx = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798",16);
        BigInteger Gy = new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8",16);

        EllipticCurve curve256 = new EllipticCurve(p, x, y);
        CurvePoint generator256 = new CurvePoint(curve256, Gx, Gy);
        BigInteger secret = new BigInteger(privatekey, 16);
        
        CurvePoint pubkeyPoint = CurvePoint.mul(generator256, secret);
        
        String result = "04" + pubkeyPoint.getX().toString(16).toUpperCase() + pubkeyPoint.getY().toString(16).toUpperCase();
        
		return result;
	}
}
