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
						
						Transaction tx = new RecvTransaction(datas);
						if(TransactionQueue.get_instance().add_transaction(tx)) {
							System.out.println("Tx_id : 12cdbk1jh2bkdjbk12r67h4ww45hb");
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
