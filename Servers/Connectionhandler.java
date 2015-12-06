import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Connectionhandler {
	
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
	
	
	public Connectionhandler(){
		
		try {
			 clientSocket = new DatagramSocket();

			 IPAddress = InetAddress.getByName("192.168.9.19"); 

			 
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	
	 
	
	
	public void sendInfo(String info) {
		
		
		try {
			
			
			byte[] sendData = new byte[1024];
			 
			
			sendData = info.getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		      clientSocket.send(sendPacket);
			
			 
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	

}
