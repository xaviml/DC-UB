package ub.swd.model.connection;

import java.net.*;
import java.io.*;

public class ComUtils {

    /* Objectes per escriure i llegir dades */
    private DataInputStream is;
    private DataOutputStream os;

    public ComUtils(Socket socket) throws IOException {
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }

    /* Llegir un enter de 32 bits */
    public int readInt32() throws IOException {
        byte bytes[];
        bytes = read_bytes(4);

        return bytesToInt32(bytes, "be");
    }

    /* Escriure un enter de 32 bits */
    public void writeInt32(int number) throws IOException {
        byte bytes[] = new byte[4];

        int32ToBytes(number, bytes, "be");
        os.write(bytes, 0, 4);
    }

    /* Passar d'enters a bytes */
    private int int32ToBytes(int number, byte bytes[], String endianess) {
        if ("be".equals(endianess.toLowerCase())) {
            bytes[0] = (byte) ((number >> 24) & 0xFF);
            bytes[1] = (byte) ((number >> 16) & 0xFF);
            bytes[2] = (byte) ((number >> 8) & 0xFF);
            bytes[3] = (byte) (number & 0xFF);
        } else {
            bytes[0] = (byte) (number & 0xFF);
            bytes[1] = (byte) ((number >> 8) & 0xFF);
            bytes[2] = (byte) ((number >> 16) & 0xFF);
            bytes[3] = (byte) ((number >> 24) & 0xFF);
        }
        return 4;
    }

    /* Passar de bytes a enters */
    private int bytesToInt32(byte bytes[], String endianess) {
        int number;

        if ("be".equals(endianess.toLowerCase())) {
            number = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16)
                    | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
        } else {
            number = (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8)
                    | ((bytes[2] & 0xFF) << 16) | ((bytes[3] & 0xFF) << 24);
        }
        return number;
    }

    //llegir bytes.
    private byte[] read_bytes(int numBytes) throws IOException {
        int len = 0;
        byte bStr[] = new byte[numBytes];
        do {
            len += is.read(bStr, len, numBytes - len);
        } while (len < numBytes);
        return bStr;
    }

	/* Llegir un string  mida variable size = nombre de bytes especifica la longitud*/
	public  String readStringVariable(int size) throws IOException
	{
		byte bHeader[]=new byte[size];
		char cHeader[]=new char[size];
		int numBytes=0;
		
		// Llegim els bytes que indiquen la mida de l'string
		bHeader = read_bytes(size);
		// La mida de l'string ve en format text, per tant creem un string i el parsejem
		for(int i=0;i<size;i++){
			cHeader[i]=(char)bHeader[i]; }
		numBytes=Integer.parseInt(new String(cHeader));
		
		// Llegim l'string
		byte bStr[]=new byte[numBytes];
		char cStr[]=new char[numBytes];
		bStr = read_bytes(numBytes);
		for(int i=0;i<numBytes;i++)
			cStr[i]=(char)bStr[i];
		return String.valueOf(cStr);
	}
	
	/* Escriure un string mida variable, size = nombre de bytes especifica la longitud  */
	/* String str = string a escriure.*/
	public  void writeStringVariable(int size,String str) throws IOException
	{
		
		// Creem una seqüència amb la mida
		byte bHeader[]=new byte[size];
		String strHeader;
		int numBytes=0; 
		
		// Creem la capçalera amb el nombre de bytes que codifiquen la mida
		numBytes=str.length();
		
		strHeader=String.valueOf(numBytes);
	    int len;
		if ((len=strHeader.length()) < size)
	    	for (int i =len; i< size;i++){
	    		strHeader= "0"+strHeader;}
	    System.out.println(strHeader);
		for(int i=0;i<size;i++)
			bHeader[i]=(byte)strHeader.charAt(i);
		// Enviem la capçalera
		os.write(bHeader, 0, size);
		// Enviem l'string writeBytes de DataOutputStrem no envia el byte més alt dels chars.
		os.writeBytes(str);
	}
    
    public String readString(int size) throws IOException {
        byte[] b = new byte[size];
        is.read(b);
        return new String(b);
    }
    
    public void writeString(String s) throws IOException {
        os.write(s.getBytes());
    }
    
    public void writeChar(char c) throws IOException {
        writeByte((byte) c);
    }
    
    public char readChar() throws IOException {
        return (char) readByte();
    }
    
    public void writeByte(byte b) throws IOException {
        os.write(b);
    }
    
    public byte readByte() throws IOException {
        byte b[] = new byte[1];
        is.read(b);
        return b[0];
    }
}
