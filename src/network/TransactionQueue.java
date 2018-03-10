package network;

import java.util.LinkedList;
import java.util.Queue;

import block.Block;
import transaction.Transaction;

public class TransactionQueue extends Thread 
{
	private static TransactionQueue instance = null;
	
	private Queue<Transaction> transactionQueue = null;
	
	private TransactionQueue()
	{
		transactionQueue = new LinkedList<Transaction>();
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			if(!transactionQueue.isEmpty())
			{
				Transaction t =  transactionQueue.poll();
				Block block = null;
				
				switch(t.get_header())
				{
				case Transaction.RECV_COIN:
					System.out.println("[NDOE STATUS] : RECV TRANSACTION");
					
					// ��� ���� �� Ʈ����� ���� 
					block = new Block(t);
					
					// ��� ����
					block.saveblock();
					
					// ���� 
					
					break;
				case Transaction.SEND_COIN:
					System.out.println("[NDOE STATUS] : SEND TRANSACTION");
					
					// ��� ���� �� Ʈ����� ���� 
					block = new Block(t);
					
					// ��� ����
					block.saveblock();
					
					// ���� 
					
					break;
				}
			}
		
			try {
				Thread.sleep(1);
			}catch(Exception e) {
				
			}
		}
	}
	
	public boolean add_transaction(Transaction tx)
	{
		try {
			transactionQueue.add(tx);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public synchronized static TransactionQueue get_instance() {
		if(instance==null)
			instance = new TransactionQueue();
		return instance;
	}
}
