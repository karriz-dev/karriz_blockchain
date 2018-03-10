package block;

import java.io.File;
import java.io.FileOutputStream;

import crypto.Encrypt;
import transaction.Transaction;

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
			String path = "block/";
			File file = new File(path);
			if(!file.exists())
			{
				file.mkdirs();
			}

			try {
				FileOutputStream out = new FileOutputStream(path + block_id.toLowerCase().substring(2) +".block");
				
				out.write(block_id.getBytes());
				
				out.write(version.getBytes());
				
				if(prev_block_id != null)
					out.write(prev_block_id.getBytes());
				
				out.write(body.getbytes());
				
				return true;
			}catch(Exception e) {
				System.out.println("BLOCK ERROR : " + e.getCause() + "(" + e.getMessage() +")");
				return false;
			}
		}
		else return false;
	}
}
