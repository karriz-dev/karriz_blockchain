package crypto;

import java.util.List;

import transaction.Transaction;

public class MerkleTree {
	
	private MerkleNode root = null;
	
	public MerkleTree(List<Transaction> tx_list)
	{
		root = new MerkleNode();
	}
	
	public String getMerkleRoot()
	{
		return root.getHash();
	}
}
