package block;

import java.io.FileOutputStream;

import crypto.Encrypt;
import event.Event;
import transaction.Transaction;
import wallet.WalletData;

public class Block 
{
	/*
	 * Block Header
	 */
	private String block_id = null;
	private String version = Encrypt.BLOCK_VERSION;
	private String prev_block_id = null;

	private long timestamp = 0L;

	/*
	 * Block Body
	 */
	private Transaction body = null;
	
	/*
	 * Block Constructor
	 */
	public Block(Transaction tx){
		this.timestamp = System.currentTimeMillis();
		this.body = tx;
		this.block_id = Encrypt.get_RIPEMD160(Encrypt.get_SHA256(tx.toString()).getBytes());
	}
	
	public Block(String path){}
	
	/*
	 * Block Functions
	 */
	
	public String getPath()
	{
		return "block/abdcdef.block";
	}
	
	public boolean saveblock()
	{
		if(body != null)
		{
			try {
				FileOutputStream out = new FileOutputStream("block/" + block_id +".block");
				out.write(block_id.getBytes());
				out.write(version.getBytes());
				out.write(prev_block_id.getBytes());
				out.write(body.getbytes());
				return true;
			}catch(Exception e) {
				return false;
			}
		}
		else return false;
	}
}
