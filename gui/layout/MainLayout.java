package layout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import crypto.KeyGenerator;
import event.Event;
import network.NodeManager;
import network.NodeSyncManager;
import transaction.RecvTransaction;
import transaction.SendTransaction;
import transaction.Transaction;
import wallet.Address;
import wallet.WalletData;

public class MainLayout extends JFrame 
{
	private static MainLayout instance = null;
	private JTextField txt_address;
	
	public static String privatekey = "";
	public static String publickey = "";
	
	public static Address Address = null;
	private JTextField textField;
	
	private JLabel lblSyncAt = null;
	
	private JList<String> sendlist = null;
	
	
	private List<WalletData> datalist = null;
	private JTextField textField_1;
	
	
	private JPanel panel_1 = null;
	private JButton btnNewButton = null;
	
	private JLabel lblBase = null;
	private JSpinner spinner_1 = null;
	private JSlider slider = null;
	private JLabel label = null;
	
	private float base_coin = 0.0f;
	
	private MainLayout() {
		
		setTitle("BaSE Core");

		datalist = new ArrayList<WalletData>();
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		    	NodeSyncManager.get_instance().send_exitevent();
		    }
		});
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("assets/138292.png"));
		
		setBounds(100,100,662,488);
		
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		lblBase = new JLabel("0.0 BaSE");
		lblBase.setFont(new Font("±¼¸²", Font.PLAIN, 20));
		lblBase.setBounds(101, 7, 529, 26);
		lblBase.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblBase);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(12, 73, 622, 338);
		getContentPane().add(tabbedPane);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Send", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblAddress_1 = new JLabel("Address :");
		lblAddress_1.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		lblAddress_1.setBounds(12, 40, 110, 26);
		panel_2.add(lblAddress_1);
		
		JLabel lblAmount_1 = new JLabel("Amount :");
		lblAmount_1.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		lblAmount_1.setBounds(14, 87, 73, 31);
		panel_2.add(lblAmount_1);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		textField_1.setBounds(91, 38, 388, 31);
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				change_checkimg((textField_1.getText().length() == 34) && (textField_1.getText().charAt(0) == '1'));
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				change_checkimg((textField_1.getText().length() == 34) && (textField_1.getText().charAt(0) == '1'));
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
		});

		panel_2.add(textField_1);
		textField_1.setColumns(10);
		
		spinner_1 = new JSpinner();
		spinner_1.setEnabled(false);
		spinner_1.setModel(new SpinnerNumberModel(new Float(0), new Float(0), new Float(5000000), new Float(1)));
		spinner_1.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		spinner_1.setBounds(91, 90, 255, 28);
		spinner_1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float max = base_coin;
				float min = (float) spinner_1.getValue();
				
				if((max < min))
				{
					btnNewButton.setText("Send 0.0 sBaSE");
					spinner_1.setValue(0.0f);
					label.setText("0 %");
					JOptionPane.showMessageDialog(null, "ÃÖ´ë " + max +" BaSE¸¸ º¸³¾¼ö ÀÖ½À´Ï´Ù !!", "Error !!", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					btnNewButton.setText("Send " + spinner_1.getValue() + " BaSE");
					slider.setValue((int)((min/max)*100.0f));
					label.setText(slider.getValue() +" %");
				}
			}
		});
		
		panel_2.add(spinner_1);
		
		slider = new JSlider();
		slider.setEnabled(false);
		slider.setValue(0);
		slider.setBounds(78, 138, 383, 26);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float max = base_coin;
				float value = (max/100.0f) * (float)slider.getValue();
				
				btnNewButton.setText("Send " + value + " BaSE");
				spinner_1.setValue(value);
				label.setText(slider.getValue() + " %");
				
				repaint();
			}
		});
		panel_2.add(slider);
		
		label = new JLabel("0%");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		label.setBounds(458, 143, 73, 15);
		panel_2.add(label);
		
		btnNewButton = new JButton("Send 0.0 BaSE");
		btnNewButton.setEnabled(false);
		btnNewButton.setBounds(12, 188, 516, 37);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Transaction tx = new SendTransaction();
				NodeManager.get_instance().broadcasting_tx(tx);
				
				// send transaction
				datalist.add(new WalletData(textField_1.getText(), " ", (float) spinner_1.getValue(), "send_lock"));
				
				savekey();
				refresh_sendlist();
				refresh_base_coin();
			}
		});
		panel_2.add(btnNewButton);
		
		panel_1 = new JPanel();
		panel_1.setBounds(485, 35, 24, 26);
		panel_2.add(panel_1);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Receive", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblMessage = new JLabel("Message :");
		lblMessage.setBounds(12, 22, 77, 19);
		panel.add(lblMessage);
		lblMessage.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		
		textField = new JTextField();
		textField.setBounds(101, 19, 427, 25);
		panel.add(textField);
		textField.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		textField.setColumns(10);
		
		JLabel lblAmount = new JLabel("Amount   :");
		lblAmount.setBounds(13, 58, 76, 19);
		panel.add(lblAmount);
		lblAmount.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(101, 54, 229, 26);
		panel.add(spinner);
		spinner.setModel(new SpinnerNumberModel(new Float(0), new Float(0), new Float(5000000), new Float(1)));
		spinner.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		JLabel lblAddress = new JLabel("Address  :");
		lblAddress.setBounds(12, 93, 87, 19);
		panel.add(lblAddress);
		lblAddress.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		
		txt_address = new JTextField();
		txt_address.setBounds(101, 90, 427, 25);
		panel.add(txt_address);
		txt_address.setEditable(false);
		txt_address.setFont(new Font("±¼¸²", Font.PLAIN, 16));
		txt_address.setColumns(10);
		
		JButton btnCreateAddress = new JButton("Receive BaSE Coin");
		btnCreateAddress.setBounds(12, 134, 516, 41);
		panel.add(btnCreateAddress);
		btnCreateAddress.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		
		sendlist = new JList<String>();
		sendlist.setBounds(12, 185, 516, 136);
		sendlist.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				String item = sendlist.getSelectedValue();
				String[] items = item.split("/");
				
				if(items != null) {
					JOptionPane.showMessageDialog(null, "ÁÖ¼Ò : " + items[0] + "\r\n" + "³»¿ë : " + items[1] + "\r\n" + "¼ö·® : " + items[2] +"\r\nÁÖ¼ÒÁ¤º¸°¡ Å¬¸³º¸µå¿¡ º¹»çµÇ¾ú½À´Ï´Ù !!", "Æ®·£Àè¼Ç ÀÚ¼¼È÷ º¸±â", JOptionPane.OK_OPTION);

					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					String copyString = items[0];
					
					if(copyString != null)
					{
					     StringSelection contents = new StringSelection(copyString);
					     clipboard.setContents(contents, null);
					}
				}
			}
		});
		panel.add(sendlist);
		
		btnCreateAddress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(!textField.getText().equals("") && (float)spinner.getValue() > 0.0f)
            	{
            		privatekey = KeyGenerator.get_privatekey();
                	publickey = KeyGenerator.get_publickey(privatekey);
                	Address = new Address(publickey);
                	txt_address.setText(Address.get_address());

        			datalist.add(new WalletData(Address.get_address(),textField.getText(),(float)spinner.getValue(),"recv_lock"));
        			
                	savekey();
                	
                	refresh_sendlist();
                	refresh_base_coin();
                	
                	Transaction tx = new RecvTransaction(Address.get_address(),textField.getText(),(float)spinner.getValue());
                	NodeManager.get_instance().broadcasting_tx(tx);
            	}
            }
        });
		
		txt_address.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txt_address.selectAll();
			}
		});
		
		JList list = new JList();
		tabbedPane.addTab("Transactions", null, list, null);
		list.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		refresh_sendlist();
		refresh_base_coin();
		
		lblSyncAt = new JLabel("Sync At 2017-01-12 14:22:33");
		lblSyncAt.setForeground(Color.DARK_GRAY);
		lblSyncAt.setBounds(465, 421, 169, 15);
		lblSyncAt.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblSyncAt);
		
		JLabel lblSendLock = new JLabel("Send Lock : 0.0 BaSE");
		lblSendLock.setBounds(350, 32, 280, 15);
		lblSendLock.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblSendLock);
		
		JLabel lblNewLabel = new JLabel("Receive Lock : 0.0 BaSE");
		lblNewLabel.setBounds(375, 50, 255, 15);
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblNewLabel);
		setVisible(true);
	}
	
	private boolean refresh_sendlist()
	{
		try {
			loadkey();

			String[] datas = new String[datalist.size()];
			
			int count = 0;
			
			for(WalletData data : datalist)
			{
				datas[count] = data.toString();
				count++;
			}
			sendlist.setListData(datas);

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized static MainLayout get_instance()
	{
		if(instance == null)
			instance = new MainLayout();
		
		return instance;
	}
	
	public float get_base()
	{
		return base_coin;
	}
	
	public void setSynclbl_text(String text)
	{
		lblSyncAt.setText(text);
	}

	public void setlblBase_text(String text)
	{
		lblBase.setText(text + " Base");
	}
	
	private boolean refresh_base_coin()
	{
		try {
			loadkey();
			for(WalletData data : datalist)
			{
				if(data.getType().equals("recv_lock"))
					base_coin += data.getAmount();
				else if(data.getType().equals("send_lock"))
					base_coin -= data.getAmount();
			}
			return true;
		}catch(Exception e) {
			base_coin = 0.0f;
			lblBase.setText(base_coin + " Base");
			return false;
		}
	}
	
	private boolean savekey()
	{
		try {
			FileOutputStream out = new FileOutputStream("wallet.base");
			out.write(Event.intToByteArray(datalist.size()));
			for(WalletData data : datalist)
			{
				out.write(data.get_bytes());
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private boolean loadkey()
	{
		try {
			datalist.clear();
			
			FileInputStream in = new FileInputStream("wallet.base");
			byte[] data = new byte[4];
			int count = 0;
			
			// Æ®·£Àè¼Ç ÃÑ °¹¼ö 
			while(count < 4)
			{
				int r = in.read();
				if(r != -1) {
					data[count] = (byte)r;
					count++;
				}
			}
			
			int total_size = Event.byteArrayToInt(data);
			
			for(int index = 0;index < total_size;index++)
			{
				// Æ®·£Àè¼Ç ÃÑ °¹¼ö¸¸Å­ Read 
				
				// 1. µ¥ÀÌÅÍÀÇ ±æÀÌ
				
				data = new byte[4];
				count = 0;
				
				while(count < 4)
				{
					int r = in.read();
					if(r != -1) {
						data[count] = (byte)r;
						count++;
					}
				}
				
				int bodylength = Event.byteArrayToInt(data);
				
				// 2. ¹ÙµðÀÇ ±æÀÌ 
				
				data = new byte[bodylength];
				count = 0;
				while(count < bodylength)
				{
					int r = in.read();
					if(r != -1) {
						data[count] = (byte)r;
						count++;
					}
				}
				
				datalist.add(new WalletData(data));
			}
			
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private void change_checkimg(boolean checked)
	{
		Image img = null;
		ImageIcon imgic = null;
		
		if(checked) {
			img = Toolkit.getDefaultToolkit().getImage("assets/check.png");
			spinner_1.setEnabled(true);
			slider.setEnabled(true);
			btnNewButton.setEnabled(true);
		}else {
			img = Toolkit.getDefaultToolkit().getImage("assets/error.png");
			spinner_1.setEnabled(false);
			slider.setEnabled(false);
			btnNewButton.setEnabled(false);
		}
		
		Image newimg = img.getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH);  
		imgic = new ImageIcon(newimg);
		
		JLabel lbImage1  = new JLabel(imgic);
		panel_1.removeAll();
		panel_1.add(lbImage1);
		
		repaint();
	}
}
