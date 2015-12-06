import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
public class ServerInfo  {



	private String MyIp;
	private HashMap<String, Long> messageBuffer;
	long responseTime;

	private int nConnections;


	public ServerInfo (){

		responseTime = 0;
		nConnections = 0;

		messageBuffer = new HashMap<String, Long>();

		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements())
			{
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements())
				{
					InetAddress i = (InetAddress) ee.nextElement();
					System.out.println("interface: "+n.getDisplayName());
					if(n.getDisplayName().compareTo("eth1")==0){
						MyIp = i.getHostAddress();

					}

					//System.out.println(i.getHostAddress());
				}
			}


			System.out.println("ip:" +MyIp);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public void startTcpdump() {

		long ts = 0;
		String src = null;
		String dst = null;



		while(true){
			//System.out.println("STARTING TCPDUMP");
			String [] opts = null;
			String[] tcpdumpCmd = {"/usr/sbin/tcpdump","-l", "-nntt" ,"-i", "eth1"};
			try {
				Process p = new ProcessBuilder(tcpdumpCmd).start();

				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

				String tcpdumpOut = null;

				while ((tcpdumpOut = in.readLine()) != null ) {
					//	System.out.println(tcpdumpOut);

					opts = tcpdumpOut.split(" ");
					if((opts.length > 4) && (opts[1].compareTo("IP")==0)){
						try{

							ts = System.currentTimeMillis();
							//	System.out.println("IP: "+opts[1]);
							//System.out.println("SRC: "+opts[2]);
							src = opts[2];
							//	System.out.println("DST: "+opts[4]);
							dst = opts[4].substring(0, opts[4].length()-1);
							//	System.out.printinln("PROTO: "+opts[5]);
							//	System.out.println("ID: "+opts[9]);
							//id =Integer.parseInt(opts[9].substring(0, opts[9].length()-1));
							String flag = opts[6];
							computeResponseTime(ts, src, dst, flag);
						}catch(Exception e){

						}
					}



				}


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void computeResponseTime(long ts, String src, String dst, String flag){


		int dstPos = dst.lastIndexOf('.');
		int srcPos = src.lastIndexOf('.');

		String dstIp = dst.substring(0,dstPos);
		String srcIp = src.substring(0,srcPos);



		if(flag.contains("P")){

			if( (dstIp.compareTo(MyIp) == 0) && (messageBuffer.get(srcIp) == null) ){
				messageBuffer.put(srcIp, ts);


			}else if(srcIp.compareTo(MyIp) == 0){

				this.responseTime = (long) (ts - messageBuffer.get(dstIp)) ;
				messageBuffer.remove(dstIp);
			}

		}


	}




	public long getResponseTime(){

		return this.responseTime;
	}

	public void setResponseTime(long value){
		this.responseTime = value;
	}

	public int getConnection(){

		String s;
		int connections = 0;


		String[] cmd = {
				"/bin/sh",
				"-c",
				"netstat -an | grep "+MyIp+" | grep -c ESTABLISHED"
		};

		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			while ((s = br.readLine()) != null){

				connections = Integer.parseInt(s);

			}



			p.waitFor();
			System.out.println ("exit: " + p.exitValue());
			p.destroy();

			//  System.out.println("CPU usage: "+cpuUsage.doubleValue());
		} catch (Exception e) {
			e.printStackTrace();
		}


		return connections;
	}

	public double getCpuInfo(){


		String s;
		double cpuUsage = 0;


		Process p;
		try {
			p = Runtime.getRuntime().exec("mpstat 2 5");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			while ((s = br.readLine()) != null){

				if(s.contains("Average")){

					String [] aux = s.split(" ");


					String value = null;

					for(int i = 0 ; i<aux.length; i++ ){

						try{
							if(Character.isDigit(aux[i].charAt(0))){
								value = aux[i];
								i = aux.length;

							}
						}catch (Exception e){
							continue;
						}

					}


					String doubleValue = value.replace(',', '.');

					cpuUsage = Double.parseDouble(doubleValue);


				}

			}

			p.waitFor();
			p.destroy();

			//  System.out.println("CPU usage: "+cpuUsage.doubleValue());
		} catch (Exception e) {
			e.printStackTrace();
		}


		return cpuUsage;
	}

}
