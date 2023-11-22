 
import java.net.*;
import java.io.*;

public class Se1 {
	Se1() {
		try{
			ServerSocket ss = new ServerSocket(8634);
			Socket s = ss.accept();
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dut = new DataOutputStream(s.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String s1="" , s2="" ;

			while(!s1.equals("stop")) {
				s1 = dis.readUTF();
				System.out.println("Client says : "+s1);
				s2 = br.readLine();
				dut.writeUTF(s2);
				dut.flush();
			}
				s.close();
				dis.close();
				ss.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		new Se1();
	}
}