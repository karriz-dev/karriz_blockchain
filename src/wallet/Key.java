package wallet;

public class Key {
	public String privatekey;
	public String publickey;
	public String message;
	public float amount = 0.0f;
	
	public Key(String privatekey, String publickey,String msg, float amount)
	{
		this.privatekey = privatekey;
		this.publickey = publickey;
		this.message = msg;
		this.amount = amount;
	}
	
	public Key(byte[] datas) 
	{
		String input = new String(datas);
		String[] data = input.split("/");
		
		privatekey = data[0];
		publickey = data[1];
		message = data[2];
		amount = Float.parseFloat(data[3]);
	}
	
	@Override
	public String toString()
	{
		return privatekey + "/" + publickey + "/" + message +"/" + amount +"//";
	}
	
	public byte[] get_bytes() 
	{
		return toString().getBytes();
	}
}
