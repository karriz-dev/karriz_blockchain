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

public class NodeSyncManager extends Thread 
{
	private static NodeSyncManager instance = null;
	
	private Socket mainserver = null;
	
	private final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private NodeSyncManager() {
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
						
						NodeManager.get_instance().setNodeList(event.getlist());
					}	
				}
				Thread.sleep(10000);
			}
		}catch(Exception e) {
			System.out.println("[DEBUG] - " + e.getCause() + "(" + e.getMessage() + ")");
			JOptionPane.showMessageDialog(null, "노드 동기화에 실패하였습니다. \r\nBaSE_MainServer관리자에게 요청하세요 !!", "Error !!", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
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
	
	public static synchronized NodeSyncManager get_instance() {
		if(instance == null) {
			instance = new NodeSyncManager();
		}
		return instance;
	}
}
