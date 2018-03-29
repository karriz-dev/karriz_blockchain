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
				System.out.println("[TRANSACTION QUEUE] 트랜잭션이 " + TRANSACTION_MAX + "개가 되어 블록 생성을 진행합니다.");
				
				
				// 1. 블록 생성
				Block block = new Block(transactionList);
				
				// 2. 트랜잭션 리스트 초기화 
				transactionList.clear();
				
				// 3. 블록 전파 
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
