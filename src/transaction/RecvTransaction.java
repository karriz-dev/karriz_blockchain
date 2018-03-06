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
		try {
			String body_data = address+"/"+message+"/"+amount;
			byte[] body = body_data.getBytes();
			byte[] sender = new byte[16+body.length];
			
			// t_id
			System.arraycopy(longToBytes(t_id), 0, sender, 0, 8);

			// header
			System.arraycopy(intToByteArray(header), 0, sender, 8, 4);

			// bodylength
			System.arraycopy(intToByteArray(body.length), 0, sender, 12, 4);

			// body
			System.arraycopy(body, 0, sender, 16, body.length);

			return sender;
		}catch(Exception e) {
			System.out.println(e.getCause()+"/"+e.getMessage());
			return null;
		}
	}
}
