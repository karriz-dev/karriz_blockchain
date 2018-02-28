package wallet;

import event.Event;

public class WalletData 
{
	private String address;
	private String message;
	private String amount;
	private String type;
	
	public WalletData(String address, String message, float amount, String type)
	{
		this.address = address;
		this.message = message;
		this.amount = String.valueOf(amount);
		this.type = type;
	}
	
	public WalletData(byte[] data)
	{
		String data_str = new String(data);
		String[] datas = data_str.split("/");
		address = datas[0];
		message = datas[1];
		amount = datas[2];
		type = datas[3];
	}
	
	public String getAddress() {
		return address;
	}

	public String getMessage() {
		return message;
	}

	public float getAmount() {
		return Float.parseFloat(amount);
	}
	
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return address+"/"+message+"/"+amount+"/"+type;
	}
	
	public byte[] get_bytes()
	{
		String data_str = address+"/"+message+"/"+amount+"/"+type;
		byte[] data_byte = data_str.getBytes();
		
		int offset = 0;
		int length = data_byte.length;
		
		byte[] datas = new byte[4+length];
		
		System.arraycopy(Event.intToByteArray(length), 0, datas, offset, 4);

		offset += 4;
		
		System.arraycopy(data_byte, 0, datas, offset, length);
		
		offset += length;
		
		return datas;		
	}
}
