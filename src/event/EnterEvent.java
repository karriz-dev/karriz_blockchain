package event;

public class EnterEvent extends Event 
{
	public EnterEvent()
	{
		header = Event.ENTER_EVENT;
	}
	
	@Override
	public byte[] getbytes() {
		byte[] buffer = new byte[4];
		System.arraycopy(intToByteArray(header), 0, buffer , 0, 4);
		return buffer;
	}
}
