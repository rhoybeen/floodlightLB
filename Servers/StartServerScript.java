import java.io.UnsupportedEncodingException;



public class StartServerScript {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) {
		
		long sleepTime = 5000;
	
		ServerInfo si = new ServerInfo();
		Connectionhandler ch = new Connectionhandler();
		Thread t = new Thread(new Sender(ch, si, sleepTime));
		t.start();
		si.startTcpdump();
		
		
		
		
		
	}

}
