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
					// 1. RECV 명령을 받았을 경우에는 transaction을 mempool에 저장 
					
					// 2. 일정이상 transaction이 생성되면 block 생성 진행 
						// 2-1. transaction을 가지고 머클트리 생성 진행 
						// 2-2. 만들어진 머클트리를 기반으로 하여 블록헤더 생성 
						// 2-3. 블록생성이 완료되면 전파한 뒤 합의 과정 진행 
						// 2-4. 합의 완료시 블록체인에 연결
					System.out.println("RECV");
					break;
				case Transaction.SEND_COIN:
					// 2. SEND 명령을 받았을 경우에는 transaction 명부를 비교하며 address를 찾고 해당 adderss가 존재 한다면 UTXO를 생성하여 파일을 생성해 전파한다
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
