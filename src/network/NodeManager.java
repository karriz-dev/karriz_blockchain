package network;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import layout.MainLayout;
import transaction.Transaction;

public class NodeManager extends Thread 
{
	private static NodeManager instance = null;
	
	private List<Node> recv_nodes = null;
	private List<Socket> send_nodes = null;
	
	private ServerSocket receiver = null;
	
	private NodeManager() {
		try {
			receiver = new ServerSocket(20185);
			this.start();
		}catch(Exception e) {
			
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try {
				recv_nodes.add(new Node(receiver.accept()));
				Thread.sleep(1);
			}catch(Exception e) {
			}
		}
	}
	
	public boolean broadcasting_tx(Transaction tx)
	{
		try {
			for(Socket sock : send_nodes)
			{
				OutputStream out = sock.getOutputStream();
				out.write(tx.getbytes());
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean setNodeList(List<String> list) {
		try {
			if(list.size() <= 0)
			{
				JOptionPane.showMessageDialog(null, "��� ����ȭ�� �����Ͽ����ϴ�.\r\n���ͳ� Ȯ�� �Ǵ� BaSE_MainServer�����ڿ��� ��û�ϼ��� !!", "Error !!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			else
			{
				System.out.println("�� ��� ���� : " + list.size());
				sendHandShake(list);
				MainLayout.get_instance().setSynclbl_text("Sync At " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private boolean sendHandShake(List<String> list)
	{
		int count = 0;
		
		for(String address : list)
		{
			try {
				if(!address.equals(InetAddress.getLocalHost().getHostAddress()))
				{
					System.out.println("[NODE STATUS] HANDSHAKING : Node(" + address +")���� HandShaking ��û�� ���½��ϴ�.");
					Socket clnt = new Socket(address,20185);
					System.out.println("[NODE STATUS] HANDSHAKING : Node(" + address +")���� HandShaking�� �����Ͽ����ϴ�.");
					send_nodes.add(clnt);
					count++;
				}
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println(e.getCause() +"/" + e.getMessage());
				System.out.println("[NODE STATUS] HANDSHAKING : Node(" + address +")�� HandShaking�� �����Ͽ����ϴ�.");
			}
		}
		
		if(count > 0)
			return true;
		
		return false;
	}
	
	public static synchronized NodeManager get_instance() {
		if(instance == null) {
			instance = new NodeManager();
		}
		return instance;
	}
}
