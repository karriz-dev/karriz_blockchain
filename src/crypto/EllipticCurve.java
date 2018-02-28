package crypto;

import java.math.BigInteger;

public class EllipticCurve 
{
	private BigInteger p = null;
    private BigInteger x = null;
    private BigInteger y = null;
    
    public EllipticCurve(BigInteger p, BigInteger x, BigInteger y)
    {
        this.p = p;
        this.x = x;
        this.y = y;
    }

	public BigInteger getP() {
		return p;
	}

	public BigInteger getX() {
		return x;
	}

	public BigInteger getY() {
		return y;
	}
}
