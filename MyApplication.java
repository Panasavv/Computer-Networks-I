package myApplication;

import ithakimodem.*;
import java.io.*;
import java.lang.System.*;
import javax.comm.*;
import java.util.*;
import java.lang.Object.*;


public class MyApplication {
	public static void main(String[] args) throws IOException {
		(new MyApplication()).rx();
	}

public void rx() throws IOException{
	int readValue;
	Modem modem;
	modem=new Modem();
	modem.setSpeed(80000);
	modem.setTimeout(2000);
	modem.open("ithaki");
	
	try {
while (true) {
			readValue=modem.read();
			if (readValue==-1) break;
			System.out.print((char)readValue);
		}
} catch (Exception error) {
	}

	
	
					ByteArrayOutputStream array=new ByteArrayOutputStream();

		String gpsCode="P4544R=";
		String rParameter="1000099";
		String carriage="\r";

	
		String fcode =gpsCode.concat(rParameter).concat(carriage);
		modem.write(fcode.getBytes());

		while ((readValue=modem.read()) >=0){
		array.write( (byte)readValue);
		}
		String gpsPackets=array.toString();
		array.close(); 
		BufferedReader reader = new BufferedReader(new StringReader(gpsPackets));

		String tParameters[]=new String[9];
		int j=0;
		for (int i=0; i<100; i++) {
			String readline=reader.readLine();
		if (i==1 | i==10| i==20| i==30 | i==40 | i==50 | i==60 | i==70| i==90){
			tParameters[j]=readline;
			j++;
			}
		}
		
			String tStrings[]=new String[9];
			for (int i=0; i<9; i++){
				char[] charArrayGPS= tParameters[i].toCharArray();
				
			   char[] c6={charArrayGPS[36],charArrayGPS [37]};
				  int c6Int=Integer.valueOf(new String (c6));
				  int secL=(int) (c6Int*((float)0.6));
				   char sec1l = Integer.toString(secL).charAt(0);
				   char sec2l=Integer.toString(secL).charAt(1);
			   
			 
			   char[] c3={ charArrayGPS[23],charArrayGPS[24]};
				  int c3Int=Integer.valueOf(new String (c3));
				  int secW=(int) (c3Int*((float)0.6));
				   char sec1w = Integer.toString(secW).charAt(0);
				   char sec2w=Integer.toString(secW).charAt(1);

			   char[] result={ charArrayGPS[31],charArrayGPS[32], charArrayGPS[33],charArrayGPS[34],sec1l,sec2l,charArrayGPS[18],charArrayGPS[19],charArrayGPS[20],charArrayGPS[21],sec1w,sec2w};
			   tStrings[i]=new String(result);
			  }
			   //combine everything in fRequest which will produce our GPS image
			String  fRequest= "P4544T=" + tStrings[0] + "T=" + tStrings[1] + "T="+tStrings[2]+"T="+tStrings[3]+"T="+tStrings[4]+"T="+tStrings[5]+"T="+tStrings[6]+"T="+tStrings[7]+"T="+tStrings[8]+"\r";
			 
			  modem.write(fRequest.getBytes());
				   array=new ByteArrayOutputStream();
					readValue=modem.read();
					while (readValue >= 0 ){
						array.write((byte)readValue);
						readValue=modem.read();
					}
					byte[] byteImage=array.toByteArray();
					FileOutputStream imageGps=new FileOutputStream("imageGps.jpeg");
					for (int i=0;i<byteImage.length;i++){
						imageGps.write(byteImage[i]);
					}
					
					
					imageGps.close();   
			modem.close();
			array.close();

	
}
}