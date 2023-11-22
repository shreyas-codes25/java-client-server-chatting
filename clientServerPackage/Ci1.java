import java.io.*;
import java.net.*;

public class Ci1 {
	Ci1() {
		try{
			Socket s = new Socket("localhost",8634);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dut = new DataOutputStream(s.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String s1 = "" , s2 = "";

			while(!s1.equals("stop")) {
				s1 = br.readLine();
				dut.writeUTF(s1);
				dut.flush();
				s2 = dis.readUTF();
				System.out.println("Server says : "+s2);

			}
			dut.close();
			s.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		new Ci1();
	}
}