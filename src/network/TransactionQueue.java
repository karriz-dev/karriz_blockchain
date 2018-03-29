package network;

import java.util.ArrayList;
import java.util.List;

import block.Block;
import transaction.Transaction;

public class TransactionQueue extends Thread 
{
	private static final int TRANSACTION_MAX = 5;
			
	private static TransactionQueue instance = null;
	
	private List<Transaction> transactionList = null;
	
	private TransactionQueue()
	{
		transactionList = new ArrayList<Transaction>();
		this.start();
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			if(transactionList.size() >= TRANSACTION_MAX)
			{
				System.out.println("[TRANSACTION QUEUE] Ʈ������� " + TRANSACTION_MAX + "���� �Ǿ� ��� ������ �����մϴ�.");
				
				
				// 1. ��� ����
				Block block = new Block(transactionList);
				
				// 2. Ʈ����� ����Ʈ �ʱ�ȭ 
				transactionList.clear();
				
				// 3. ��� ���� 
				NodeManager.get_instance().broadcasting_block(block);
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
			transactionList.add(tx);
			return true;
		}catch(Exception e) {
			System.out.println("[TX ERROR] : " + e.getCause() +"("+e.getMessage()+")");
			return false;
		}
	}
	
	public synchronized static TransactionQueue get_instance() {
		if(instance==null)
			instance = new TransactionQueue();
		return instance;
	}
}
