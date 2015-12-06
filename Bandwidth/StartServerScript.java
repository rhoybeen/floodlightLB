import java.io.UnsupportedEncodingException;



public class StartServerScript {

	/**
	 * @param args <sleepTime(ms)> [<neighbours ips>]
	 * Example:
	 * StartServerScript 15000 192.168.9.1 192.168.9.2 192.168.9.3
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
