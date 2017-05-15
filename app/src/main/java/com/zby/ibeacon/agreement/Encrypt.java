package com.zby.ibeacon.agreement;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import android.util.Log;



public class Encrypt {
	
	private final static String TAG  = "tag";

	/**
	 * ����
	 * @param message
	 * @param length
	 * @return 
	 */
	public static byte[] ProcessCommand(byte[] message, int length) {
		if (length <= 2) {
			return null;
		}
		byte[] buffer = new byte[message[0]-1];
		if(buffer.length>20) return null;
		System.arraycopy(message, 1, buffer, 0, buffer.length);
		return buffer;

	}

	
	/**
	 * ����
	 * @param message
	 * @param len
	 * @return
	 */
	public static byte[] sendMessage(byte[] message, int len) {
		//����1+����β��1
		byte[] sendmsg2 = new byte[len+1];
		//����
		sendmsg2[0] = (byte) (len+1);
	
	
		//������д���ַ�
		System.arraycopy(message, 0, sendmsg2, 1, len);
		
//		byte checkSum ;
//		if(len>2) {  //����λ = У��λ������^����^��������������ֽڣ��������������ֽڲ�0��
//			checkSum = (byte) (sendmsg2[0] ^ message[0] ^ message[len-2] ^ message[len-1]);
//		} else if(len == 2) {
//			checkSum = (byte) (sendmsg2[0] ^ message[0] ^ message[1] ^ (byte)0x00);
//		} else {
//			checkSum = (byte) (sendmsg2[0] ^ message[0] ^ (byte)0x00 ^ (byte)0x00);
//		}
//		sendmsg2[sendmsg2.length-1] = checkSum;
		return sendmsg2;
	}

	/**
	 * @param b �ֽ�
	 * @return  �ֽ�תint
	 */
	private static int byteToUnsigned(byte b) {
		int iout = 0;
		if (b >= 0) {
			iout = b;
		} else {
			iout = b + 256;
		}
		return iout;
	}
	
	
//	public static void main(String[] args) {
////		byte[] buffer;
////		buffer= CmdDataPackage.validateDate();
////		test(buffer);
////		
////		 buffer= CmdDataPackage.getReadBuffer(5);
////		test(buffer);
////			
////		buffer= CmdDataPackage.getDeviceNameControl("asdf��������");
////			test(buffer);
////			byte[] nameBuffer= new byte[buffer.length-2];
////			System.arraycopy(buffer, 2, nameBuffer, 0, nameBuffer.length);
////			String str;
////			try {
////				str = new String(nameBuffer,"utf-8");
////				System.out.println("����������  "+str);
////			} catch (UnsupportedEncodingException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
//			
//			byte[] nameBuffer = "����".getBytes();
//			byte[] buffer = new byte[20];
//			buffer[0] = (byte) 0xFF;
//			buffer[1] = (byte) 0xF0;
//			buffer[2] = (byte) 0x12;
//			buffer[3] = (byte) 0x01;
//			buffer[4] = (byte) 0x01;
//			System.arraycopy(nameBuffer, 0, buffer, 5, nameBuffer.length);
//			System.out.println(Myhex.buffer2String(buffer));
//			
//			
//			byte[] command1 = new byte[17];
//			System.arraycopy(buffer, 3, command1, 0, command1.length);
//			System.out.println("���"+Myhex.buffer2String(command1));
//			String str;
//				str = new String(nameBuffer);
//				System.out.println(str);
//				
//			
//	}
//	
//	private static void test(byte[] buffer) {
//		System.out.println("----------------");
//		System.out.println(Myhex.buffer2String(buffer));
//		byte[] addBuffer = sendMessage(buffer, buffer.length);
//		System.out.println("����"+Myhex.buffer2String(addBuffer));
//		byte[] parseBuffer = ProcessCommand(addBuffer, addBuffer.length);
//		System.out.println("����"+Myhex.buffer2String(parseBuffer));
//		
//		
//	}
}
