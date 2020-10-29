package log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.Scanner;

public class Log {
	private String PIN;
	
	private File file, custLog;
	private static Scanner fin;
	private static FileWriter fout;
	private static FileWriter logout;
	
	private static DateTimeFormatter format;
	private static float lockTime=15.0f;
	
	public Log()
	{
		try {
			file=new File("AccessLog.txt");
			fin=new Scanner(file);
			fout=new FileWriter(file,true);
			
			custLog=new File("CustomerLog.txt");
			logout=new FileWriter(custLog, true);
			
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		format=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
		PIN=fin.nextLine();
		//lockTime=2;
	}
	public void writeDone()
	{
		try {
			fout.close();
			logout.close();
			fin.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public boolean checkPIN(String userInput)
	{
		if(userInput.compareTo(PIN)==0)
			return true;
		return false;
	}
	public boolean isLocked(LocalDateTime currentTime)
	{
		String lastAccess="";
		if(fin.hasNextLine())
		{
			do {
				lastAccess=fin.nextLine();
			}while(fin.hasNextLine());
			int i=lastAccess.lastIndexOf(' ');
			String t=lastAccess.substring(0,i), status=lastAccess.substring(i+1); 
			LocalDateTime prevTime=LocalDateTime.parse(t,format);
			Duration timeBetweenAccess=Duration.between(prevTime, currentTime);
			if(status.compareTo("Locked")==0)
				return (timeBetweenAccess.getSeconds() <= lockTime);
		}
		return false;
	}
	public void writeToLog(String ticket)
	{
		try {
			logout.write(ticket);
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	public void writeToLog(LocalDateTime time,boolean flag,boolean isLocked)
	{
		String fileContent;
		if(!isLocked)	
			fileContent=time.format(format)+" "+flag+"\n";
		else
			fileContent=time.format(format)+" "+"Locked"+"\n";
		try {
			fout.write(fileContent);
		} 
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
}