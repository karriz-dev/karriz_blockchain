package crypto;

import java.math.BigInteger;

public class CurvePoint 
{
	public static CurvePoint INFINITY = new CurvePoint(null, new BigInteger("0",16),new BigInteger("0",16));
    private EllipticCurve Curve;
    private BigInteger X;
    private BigInteger Y;

    public CurvePoint(EllipticCurve curve, BigInteger x, BigInteger y)
    {
        this.Curve = curve;
        this.X = x;
        this.Y = y;
    }
    
    public CurvePoint Double()
    {
        if (this == INFINITY)
            return INFINITY;

        BigInteger p = this.Curve.getP();
        BigInteger x = this.Curve.getX();
        
        BigInteger l = X.multiply(BigInteger.valueOf(3)).multiply(X.add(x)); 
        l.multiply(inverse_mod(Y.multiply(BigInteger.TWO),p).mod(p));
        
        BigInteger x3 = (l.pow(2).subtract(X.multiply(BigInteger.TWO))).mod(p);
        BigInteger y3 = ((l.multiply(X.subtract(x3))).subtract(Y)).mod(p);
        
        return new CurvePoint(this.Curve, x3, y3);
    }
    
    public static CurvePoint plus(CurvePoint left, CurvePoint right)
    {
        if (right == INFINITY)
            return left;
        if (left == INFINITY)
            return right;
        if (left.X == right.X)
        {
            if (((left.getY().add(right.getY())).mod(left.Curve.getP())).compareTo(BigInteger.ZERO) == 0)
                return INFINITY;
            else
                return left.Double();
        }

        BigInteger p = left.Curve.getP();
        BigInteger l = ((right.getY().subtract(left.getY())).multiply((inverse_mod(right.getX().subtract(left.getX()), p)).mod(p)));
        BigInteger x3 = ((l.multiply(l)).subtract(left.getX()).subtract(right.getX())).mod(p);
        BigInteger y3 = (l.multiply((left.getX().subtract(x3))).subtract(left.getY())).mod(p);
        
        return new CurvePoint(left.Curve, x3, y3);
    }
    public static CurvePoint mul(CurvePoint left, BigInteger right)
    {
    	BigInteger e = right;
        if (e == BigInteger.ZERO || left == INFINITY)
            return INFINITY;
        
        BigInteger e3 = e.multiply(BigInteger.valueOf(3));
        
        CurvePoint negativeLeft = new CurvePoint(left.Curve, left.X, left.Y.multiply(BigInteger.valueOf(-1)));
        
        BigInteger count_index = LeftmostBit(e3).divide(BigInteger.valueOf(2));
        
        CurvePoint result = left;
        
        while (count_index.compareTo(BigInteger.ONE) == 1)
        {
            result = result.Double();
            
            if ((e3.and(count_index)).compareTo(BigInteger.ZERO) != 0 && ((e.and(count_index)).compareTo(BigInteger.ZERO)) == 0)
                result = plus(result, left);
            
            if ((e3.and(count_index)).compareTo(BigInteger.ZERO) == 0 && (e.and(count_index).compareTo(BigInteger.ZERO)) != 0)
                result = plus(result, negativeLeft);
            
            count_index = count_index.divide(BigInteger.TWO);
        }
        
        return result;
    }
    private static BigInteger LeftmostBit(BigInteger x)
    {
        BigInteger result = BigInteger.ONE;
        while (result.compareTo(x) <= 0)
            result = result.multiply(BigInteger.TWO);
        return result.divide(BigInteger.TWO);
    }
    private static BigInteger inverse_mod(BigInteger a, BigInteger m)
    {
        while (a.compareTo(BigInteger.ZERO) == -1) a = a.add(m);
        
        if (a.compareTo(BigInteger.ZERO) < 0 || m.compareTo(a) <= 0)
            a = a.mod(m);
        
        BigInteger c = a;
        BigInteger d = m;

        BigInteger uc = BigInteger.ONE;
        BigInteger vc = BigInteger.ZERO;
        BigInteger ud = BigInteger.ZERO;
        BigInteger vd = BigInteger.ONE;

        while (c.compareTo(BigInteger.ZERO) != 0)
        {
            BigInteger remainder = d.mod(c);

            BigInteger q = d.divide(c);
            
            d = c;
            c = remainder;

            BigInteger uct = uc;
            BigInteger vct = vc;
            BigInteger udt = ud;
            BigInteger vdt = vd;
            
            uc = udt.subtract(q.multiply(uct));
            vc = vdt.subtract(q.multiply(vct));
            ud = uct;
            vd = vct;
        }
        if (ud.compareTo(BigInteger.ZERO) == 1) return ud;
        else return ud.add(m);
    }
    
    public BigInteger getX() {return X;}
    public BigInteger getY() {return Y;}
    
    @Override
    public String toString()
    {
        if (this == INFINITY)
            return "infinity";
        return "(" + this.X + "," + this.Y + ")";
    }
}
