package network;

import java.util.LinkedList;
import java.util.Queue;

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
			
				switch(t.get_header())
				{
				case Transaction.RECV_COIN:
					// 1. RECV ����� �޾��� ��쿡�� transaction�� mempool�� ���� 
					
					// 2. �����̻� transaction�� �����Ǹ� block ���� ���� 
						// 2-1. transaction�� ������ ��ŬƮ�� ���� ���� 
						// 2-2. ������� ��ŬƮ���� ������� �Ͽ� ������ ���� 
						// 2-3. ��ϻ����� �Ϸ�Ǹ� ������ �� ���� ���� ���� 
						// 2-4. ���� �Ϸ�� ���ü�ο� ����
					System.out.println("RECV");
					break;
				case Transaction.SEND_COIN:
					// 2. SEND ����� �޾��� ��쿡�� transaction ��θ� ���ϸ� address�� ã�� �ش� adderss�� ���� �Ѵٸ� UTXO�� �����Ͽ� ������ ������ �����Ѵ�
					System.out.println("SEND");
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
