package network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import event.Event;
import transaction.RecvTransaction;
import transaction.Transaction;

public class Node extends Thread
{
	private Socket node_socket = null;
	
	public Node(Socket sock)
	{
		try {
			node_socket = sock;
			this.start();
		}catch(Exception e) {
			System.out.println("[ERROR] : " + e.getCause() + "(" + e.getMessage() +")");
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			// READ TRANSACTIONS
			try {
				InputStream in = node_socket.getInputStream();

				while(true) 
				{				
					if(in.available() > 0)
					{
						// Read t_id (8byte)
						
						byte[] datas = new byte[8];
						int count = 0;
						while(count < 8)
						{
							int r = in.read();
							if(r != -1)
							{
								datas[count] = (byte)r;
								count++;
							}
						}
						
						long t_id = Event.bytesToLong(datas);
						
						// Read header (4byte)
						datas = new byte[4];
						count = 0;
						while(count < 4)
						{
							int r = in.read();
							if(r != -1)
							{
								datas[count] = (byte)r;
								count++;
							}
						}
						
						int header = Event.byteArrayToInt(datas);
						
						// Read bodylength (4byte)
						datas = new byte[4];
						count = 0;
						while(count < 4)
						{
							int r = in.read();
							if(r != -1)
							{
								datas[count] = (byte)r;
								count++;
							}
						}
						
						int bodylength = Event.byteArrayToInt(datas);
						
						// Read body (nbyte)
						datas = new byte[bodylength];
						count = 0;
						while(count < bodylength)
						{
							int r = in.read();
							if(r != -1)
							{
								datas[count] = (byte)r;
								count++;
							}
						}
						
						Transaction tx = new RecvTransaction(t_id,header,bodylength, datas);

						if(TransactionQueue.get_instance().add_transaction(tx)) {
							System.out.println("[NODE STATUS] TRANSACTION : 트랜잭션 전송이 완료되었습니다.");
						}
					}
					Thread.sleep(1);
				}
			}catch(Exception e) {
				System.out.println("[ERROR] : " + e.getCause() + "(" + e.getMessage() +")");
				break;
			}
		}
	}
}
