package event;

import java.util.ArrayList;
import java.util.List;

import network.Node;

public class GetListEvent extends Event
{
	private List<String> nodelist = null;
	
	public GetListEvent(int bodylength, byte[] body)
	{
		nodelist = new ArrayList<String>();
		
		// address_size, address_length, address, 

		int address_size = Event.byteArrayToInt(new byte[] {body[0],body[1],body[2],body[3]});
	
		int offset = 4;
		
		for(int address_index = 1; address_index <= address_size; address_index++)
		{
			/* Read address_length */
			int length = byteArrayToInt(new byte[] {body[offset],body[offset+1],body[offset+2],body[offset+3]});
		
			offset += 4;
			
			byte[] datas = new byte[length];
			for(int index = 0; index <length;index++)
			{
				/* Read Address */
				datas[index] = body[offset + index];
			}
			
			String address = new String(datas);
			String[] split = address.split(":");
			nodelist.add(split[0]);

			offset += length;
		}
	}

	public List<String> getlist()
	{
		return nodelist;
	}
	
	@Override
	public byte[] getbytes() {
		return null;
	}
}
