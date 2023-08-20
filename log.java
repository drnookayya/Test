package co.in.drh.Tokens;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import android.util.*;
import java.util.*;

public class log extends Activity
{
	String filePath;
	File file;
	SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy hh:mm:ss:SSS aa");
	SimpleDateFormat sdfs=new SimpleDateFormat("MM-dd");
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		String line=" \n";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		Log.d("TOKENS","in log.logSave onCreate()");
		TextView tv=findViewById(R.id.logTextView1);
		tv.setText(line);
		File path = new File( Environment.getExternalStorageDirectory()+File.separator+"Tokens"+File.separator);
		if(!path.exists())path.mkdir();
		file=new File(path,"Log.txt");
		String filePath = Environment.getExternalStorageDirectory() + "/TOKENS/Log.txt";
		//    String filePath1 = Environment.getExternalStorageDirectory() + "/logcatOrT.txt";
        try
        {
            Runtime.getRuntime().exec(new String[]{"logcat", "-f", filePath, "TOKENS", "Tokens:V"});
			//        Runtime.getRuntime().exec(new String[]{"logcat",  filePath1});
			new Tokens().l("after exec");
        }
        catch (IOException e)
        {new Tokens().l("in log.java saving log exception");}
		
		//filePath =  + "/Tokens/Logs.txt";
		
		try
        {
	//if(!file.exists())file.createNewFile();
	int lefts=0;
	String todayFilter=sdfs.format( Calendar.getInstance().getTimeInMillis());
	new Tokens(). l("todayFilter "+todayFilter);
		BufferedReader br=new BufferedReader(new FileReader(file));  //fn));
		while (line != null)
		{
		line = br.readLine();
		if (line != null){
		if(!line.startsWith(todayFilter)){lefts++; continue;}
		if(line.toLowerCase().contains("androidruntime"))
		tv.append(line + "\n");
		if(line.contains("TOKENS"))
				tv.append(line + "\n");
	}
		}
		tv.append("\n"+"Lines Filtered  "+lefts);
		br.close();
		}
        catch (Exception e){}
		
	}
	public void LogSave(String s){
		try
		{
		//	Log.d("TOKENS","in log.logSave");
			File path = new File( Environment.getExternalStorageDirectory()+File.separator+"Tokens"+File.separator);
			if(!path.exists())path.mkdir();
			File file=new File(path,"LocalLogs.txt");
			if(!file.exists()){new Tokens().t("Logs file Not Created");
				new Tokens().l("Logs file Not Created");}
			FileWriter fr=new FileWriter(file,true);
			fr.append(s+"\n");
			fr.flush();
			fr.close();
		}
		catch (Exception e)
		{}
	}
	@Override
	protected void onResume()
	{
		//filePath = Environment.getExternalStorageDirectory() + "/logcatOr.txt";
		//    String filePath1 = Environment.getExternalStorageDirectory() + "/logcatOrT.txt";
	/*	String filePath = Environment.getExternalStorageDirectory() + "/TOKENS/Log.txt";
        try
        {
            Runtime.getRuntime().exec(new String[]{"logcat", "-f", filePath, "TOKENS", "TOKENS:V"});
			//        Runtime.getRuntime().exec(new String[]{"logcat",  filePath1});
        }
        catch (IOException e)
        {}
	*/
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0,2,2,"Show All");
		menu.add(0,1,1,"Show Current Log");
		menu.add(0,3,3,"Clear Log");
		menu.add(0,4,4,"Save Log");
		menu.add(0,5,5,"Exit");
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Toast.makeText(getBaseContext(),"In Show Log Menu Item Selected",Toast.LENGTH_SHORT).show();
		if(item.getItemId()==5){finish();}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
