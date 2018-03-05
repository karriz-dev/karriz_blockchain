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
	private String address = null;
	private String version = null;
	
	private boolean handshake_flag = false;
	
	private Socket node_socket = null;
	
	public Node(String a)
	{
		this.address = a;
		
		try {
			if(!address.equals(InetAddress.getLocalHost().getHostAddress()))
			{
				System.out.println("[NODE STATUS] HANDSHAKING :  NODE(" + address +")���� �ڵ����ũ ��û�� ���½��ϴ�.");
				
				node_socket = new Socket(a,20185);

				handshake_flag = true;
				System.out.println("[NODE STATUS] HANDSHAKING :  NODE(" + address +")�� �ڵ����ũ�� �����߽��ϴ�.");
				this.start();
			}
		}catch(Exception e) {
			System.out.println("[ERROR] : " + e.getCause() + "(" + e.getMessage() +")");
			System.out.println("[NODE STATUS] HANDSHAKING :  NODE(" + address +")�ڵ彦��ũ ��û�� �����߽��ϴ�.");
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
				System.out.println("[NODE STATUS] STATUS : NODE(" + address +")�� ������ ���������ϴ�. (���� ���۰����� ��� : 0��)");
				break;
			}
		}
	}
	
	public synchronized boolean send_transaction(Transaction tx)
	{
		try {
			OutputStream out = node_socket.getOutputStream();
			out.write(tx.getbytes());
			out.flush();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public String get_address()
	{
		return address;
	}
}
