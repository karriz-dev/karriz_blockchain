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
	
	private Socket mainserver = null;
	
	private final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private NodeManager() {
		nodelist = new ArrayList<Node>();
		this.start();
	}
	
	@Override
	public void run()
	{		
		try {
			mainserver = new Socket("163.239.200.178",20181);
			InputStream in = mainserver.getInputStream();
			OutputStream out = mainserver.getOutputStream();

			Event sendevent = new EnterEvent();
			out.write(sendevent.getbytes());
			
			while(true) 
			{				
				if(in.available() > 0)
				{
					// Header Read
					
					byte[] datas = new byte[4];
					int count = 0;
					while(count < 4)
					{
						int r = in.read();
						if(r != -1)
						{
							datas[count] = (byte)r;
							count++;
						}
					}
					
					if(Event.byteArrayToInt(datas) == Event.GET_LIST_EVENT)
					{
						// bodylength Read
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
						
						// body Read
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
						
						GetListEvent event = new GetListEvent(bodylength,datas);
						
						nodelist = event.getlist();	

						System.out.println("총 노드 갯수 : " + nodelist.size());
						
						if(nodelist.size() <= 0)
						{
							JOptionPane.showMessageDialog(null, "노드 동기화에 실패하였습니다.\r\n인터넷 확인 또는 BaSE_MainServer관리자에게 요청하세요 !!", "Error !!", JOptionPane.ERROR_MESSAGE);
							System.exit(0);
						}
						else
						{
							MainLayout.get_instance().setSynclbl_text("Sync At " + time.format(new Date(System.currentTimeMillis())));

							Thread.sleep(30000);	// node synchronize 30s Event.GET_LIST_EVENT
						}
					}	
				}
				Thread.sleep(1);
			}
		}catch(Exception e) {
			System.out.println("[DEBUG] - " + e.getCause() + "(" + e.getMessage() + ")");
			JOptionPane.showMessageDialog(null, "노드 동기화에 실패하였습니다. \r\nBaSE_MainServer관리자에게 요청하세요 !!", "Error !!", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
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
	
	public boolean send_exitevent()
	{
		try {
			Event exitevent = new ExitEvent();
			OutputStream out = mainserver.getOutputStream();
			out.write(exitevent.getbytes());
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
