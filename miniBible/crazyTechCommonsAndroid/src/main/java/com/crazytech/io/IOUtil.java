package com.crazytech.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

import com.crazytech.exception.LogException;

public class IOUtil {
	public static void writeFile(String line, String outFile, String charSet) throws IOException{
		String prevText;
		prevText = readFile(outFile, charSet);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),charSet));
		out.write(prevText+line);
		out.newLine();
		out.close();
	}
	
	public static String readFile(String fileToRead, String charSet) throws IOException {
		File file = new File(fileToRead);
		String text = "";
		if (file.exists()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileToRead), charSet));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				
				while (line != null) {
					sb.append(line);
					sb.append('\n');
					line = br.readLine();
				}
				text = sb.toString();
			} finally {
				br.close();
			}
			
		}
	    return text;
	}
	
	public static String readFileWithIS(InputStream is, String charSet) throws IOException {
		String text = "";
		if (is!=null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
                    is, charSet));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				
				while (line != null) {
					sb.append(line);
					sb.append('\n');
					line = br.readLine();
				}
				text = sb.toString();
			} finally {
				br.close();
			}
			
		}
	    return text;
	}
	
	/*
	 * Read file from res/raw...
	 */
	public static String readRawResource(Context context, Integer fileId) throws IOException {
	    String fileStr = null;

	        InputStream raw = context.getResources().openRawResource(fileId);
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        int size = 0;
	        // Read the entire resource into a local byte buffer.
	        byte[] buffer = new byte[1024];
	        while ((size = raw.read(buffer, 0, 1024)) >= 0) {
	        	outputStream.write(buffer, 0, size);
	        }
	        raw.close();
	        
	        fileStr = outputStream.toString();
	        
	        Log.v("CrazyTech:RawResStr", fileStr);
	        

	    return fileStr;

	}
	
	public static void overwriteFile(String line, String outFile, String charSet){
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),charSet));
			out.write(line);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogException.log(e);
		}
	}
}
