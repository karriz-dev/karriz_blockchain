package transaction;

public abstract class Transaction 
{
	public static final int SEND_COIN = 0x10000001;
	public static final int RECV_COIN = 0x10000002;
	
	protected long t_id = 0L;
	protected int header = 0;
	protected int bodylength = 0;
	protected byte[] body = null;
	protected float amount = 0.0f;
	
	protected byte[] buffer = null;
	
	public Transaction() {
		t_id = System.currentTimeMillis();
		buffer = new byte[1024];
	}
	
	public abstract byte[] getbytes();
	public int get_header() {return header;}
}




// 경욱이형, 