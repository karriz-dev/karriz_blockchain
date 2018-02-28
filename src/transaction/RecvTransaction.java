package transaction;

public class RecvTransaction extends Transaction 
{
	private String address = null;
	private String message = null;
	private float amount = 0.0f;
	
	public RecvTransaction(byte[] body)
	{
		header = Transaction.RECV_COIN;
	}
	
	public RecvTransaction(String address, String message, float amount) 
	{	
		header = Transaction.RECV_COIN;
		
		this.address = address;
		this.message = message;
		this.amount = amount;
	}
	
	@Override
	public String toString()
	{
		return address+"/"+message+"/"+amount;
	}

	@Override
	public byte[] getbytes() 
	{
		return null;
	}
}
