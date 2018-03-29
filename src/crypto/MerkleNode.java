package crypto;

import transaction.Transaction;

public class MerkleNode {
	
	private MerkleNode left = null;
	private MerkleNode right = null;
	
	private Transaction tx = null;
	
	private String hash = null;
	
	public MerkleNode()
	{
		
	}
	
	public String getHash()
	{
		return hash;
	}
}
