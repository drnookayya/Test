package co.in.drh.Tokens;
import java.io.*;
import android.os.*;
import java.util.*;


	public class save
	{
		public save(){}
		public void keep(String pn){
			
			try
			{
				FileWriter fw = new FileWriter(
					Environment.getExternalStorageDirectory() +
					"/phoneLog.txt",true);
				fw.append(pn+"     "+new Date().toGMTString()+"\n");
				fw.flush();
				fw.close();
			}
			catch (Exception e)
			{}
		}
		
}
