package event;

public abstract class Event 
{
	public static final int ENTER_EVENT = 0x20000001;
	public static final int EXIT_EVENT = 0x20000002;
	public static final int GET_LIST_EVENT = 0x20000003;
	
	public static final int HAND_SHAKE_REQ = 0x30000001;
	public static final int HAND_SHAKE_RES = 0x30000002;
	
	protected int header = 0;
	protected int bodylength = 0;
	protected byte[] body = null;
	
	public abstract byte[] getbytes();
	
	public static byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte)(value >> 24);
		byteArray[1] = (byte)(value >> 16);
		byteArray[2] = (byte)(value >> 8);
		byteArray[3] = (byte)(value);
		return byteArray;
	}
	
	public static int byteArrayToInt(byte bytes[]) {
		return ((((int)bytes[0] & 0xff) << 24) |
				(((int)bytes[1] & 0xff) << 16) |
				(((int)bytes[2] & 0xff) << 8) |
				(((int)bytes[3] & 0xff)));
	} 
}
