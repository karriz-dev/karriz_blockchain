package event;

public class ExitEvent extends Event
{
	public ExitEvent()
	{
		header = Event.EXIT_EVENT;
	}
	
	@Override
	public byte[] getbytes() {
		byte[] buffer = new byte[4];
		System.arraycopy(intToByteArray(header), 0, buffer, 0, 4);
		return buffer;
	}
}
