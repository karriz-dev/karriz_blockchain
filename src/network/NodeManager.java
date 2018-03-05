package network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import event.EnterEvent;
import event.Event;
import event.ExitEvent;
import event.GetListEvent;
import layout.MainLayout;
import transaction.Transaction;

public class NodeManager extends Thread 
{
	private static NodeManager instance = null;
	
	private List<Node> nodelist = null;
	
	private NodeManager() {
		this.start();
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			if(nodelist.size() > 0)
			{
				for(Node node : nodelist)
				{
					if(!node.isAlive())
					{
						nodelist.remove(node);
					}
				}
			}
			try {
				Thread.sleep(1);
			}catch(Exception e) {
				
			}
		}
	}
	
	public boolean broadcasting_tx(Transaction tx)
	{
		try {
			for(Node node : nodelist)
			{
				node.send_transaction(tx);
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean send_handshak(String target_address)
	{
		try {
			Socket target_node = new Socket(target_address,20182);
			OutputStream out = target_node.getOutputStream();
			out.write(1);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean setNodeList(List<Node> list) {
		try {
			nodelist = list;
			System.out.println("�� ��� ���� : " + nodelist.size());

			if(nodelist.size() <= 0)
			{
				JOptionPane.showMessageDialog(null, "��� ����ȭ�� �����Ͽ����ϴ�.\r\n���ͳ� Ȯ�� �Ǵ� BaSE_MainServer�����ڿ��� ��û�ϼ��� !!", "Error !!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			else
			{
				MainLayout.get_instance().setSynclbl_text("Sync At " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public static synchronized NodeManager get_instance() {
		if(instance == null) {
			instance = new NodeManager();
		}
		return instance;
	}
}
