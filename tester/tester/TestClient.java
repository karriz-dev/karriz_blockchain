package tester;

import layout.MainLayout;
import network.NodeManager;

public class TestClient 
{
	public static void main(String[] args)
	{		
		/*
		 * Routing Node IP = 163.239.200.151
		 * Routing Node Port = 20181
		 * 
		 * Receive Port = 20185
		 * 
		 */
		
		NodeManager.get_instance();
		MainLayout.get_instance();
	}
}
