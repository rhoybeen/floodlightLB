
public class Sender implements Runnable {

	private Connectionhandler ch;
	private long sleepTime;
	private ServerInfo si;
	private long lastRT = 0;

	public Sender(Connectionhandler ch, ServerInfo si, long sleepTime){

		this.ch = ch;
		this.sleepTime = sleepTime;
		this.si = si;


	}

	@Override
	public void run() {
		

		while(true){
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
				
				lastRT = si.getResponseTime();
				int connections = si.getConnection();
				
				if(connections == 0)
					lastRT = 0;
				
				String info = String.valueOf(lastRT)+" "+connections+" "+si.getCpuInfo();
				System.out.println("Sending: "+ info);
				ch.sendInfo(info);
				
			
		}

		

	}

}
