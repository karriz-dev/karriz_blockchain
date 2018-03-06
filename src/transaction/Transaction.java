package transaction;

import java.nio.ByteBuffer;

public abstract class Transaction 
{
	public static final int SEND_COIN = 0x10000001;
	public static final int RECV_COIN = 0x10000002;
	
	protected long t_id = 0L;
	protected int header = 0;
	protected int bodylength = 0;
	protected byte[] body = null;

	protected byte[] buffer = null;
	
	public Transaction() {
		t_id = System.currentTimeMillis();
		buffer = new byte[1024];
	}
	
	public Transaction(long t, int h, int bl, byte[] b) {
		t_id = t;
		header = h;
		bodylength = bl;
		buffer = b;
	}
	
	public abstract byte[] getbytes();
	public int get_header() {return header;}
	
	protected byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte)(value >> 24);
		byteArray[1] = (byte)(value >> 16);
		byteArray[2] = (byte)(value >> 8);
		byteArray[3] = (byte)(value);
		return byteArray;
	}
	
	protected int byteArrayToInt(byte bytes[]) {
		return ((((int)bytes[0] & 0xff) << 24) |
				(((int)bytes[1] & 0xff) << 16) |
				(((int)bytes[2] & 0xff) << 8) |
				(((int)bytes[3] & 0xff)));
	} 
	
	protected byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}

	protected long bytesToLong(byte[] bytes) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.put(bytes);
	    buffer.flip();//need flip 
	    return buffer.getLong();
	}
}




// 경욱이형, 