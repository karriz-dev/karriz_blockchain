package consensus;

public class Block 
{
	/*
	 * Block Header
	 */
	private String block_id = null;
	private String version = null;
	private String prev_block_id = null;

	private long timestamp = 0L;
	private MerkleTree root = null;

	private String level = null;
	private String nonce = null;

	/*
	 * Block Body
	 */
	private MerkleTree body = null;
	
	/*
	 * Block Constructor
	 */
	public Block(){}
	public Block(String path){}
	
	/*
	 * Block Functions
	 */
	public boolean saveblock()
	{
		try {
			return true;
		}catch(Exception e) {
			return false;
		}
	}
}
