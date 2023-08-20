//package co.in.drh.Tokens;

//public class Tokens{}
package co.in.drh.Tokens;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import android.net.*;
import android.provider.*;
import android.*;
import android.preference.*;
import java.text.*;

public class Tokens extends Activity
{

	
//	static final int APTM=1,ALL=2,TOKEN=0,NAME=1,NUMBER=2;
//	static final String aptm="aptm.txt",all="all.txt",lf="\n",sp="  ",s="#",TAG="TOKENS",
//	OPTIONS="OPTIONS",SpOptions="sp_options",nul="null",Holiday="Holiday";
	final static int OPTIONS_DATE=0,HOLIDAY=0,OPTIONS_TAG=1,OPTIONS_OP_START_TIME=2,OPTIONS_OP_CLOSE_TIME=3,OPTIONS_TOKENS=4;
	//Tokens=4,DateOfEvent=5, OPTIONS_START=1,OPTIONS_CLOSE=2;
	int DATE_NUMBER,OP_START_MINUTE,OP_CLOSE_MINUTE,MINUTE,TOKEN_START_MINUTE=0,TOKEN_CLOSE_MINUTE=23 * 60 + 59, TOKENS,PRESENT_TOKEN_NUMBER;
	//String dateNumber,opStartTime,opCloseTime,tokensStartTime,tokensCloseTime;	
	boolean blnHoliday,blnTokensOver;
	File path,localPath;
	//final static String today_date_number="today_date_number";
	private String MESSAGE_TAG;

	Context context=getBaseContext();
	// String address,mes;
	long tms;
	SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy hh:mm:ss:SSS aa");
	final int STRING=0,INTEGER=1,LONG=2,BOOLEAN=3,FLOAT=4;
	
	//=================
	TextView tv;
	static final int APTM=1,ALL=2,TOKEN=0,NAME=1,NUMBER=2;
	static final String aptm="aptm",all="all",lf="\n",sp="  ",s="#",TAG="TOKENS",OPTIONS="OPTIONS",SpOptions="sp_options",nul="null";
	String[] names=new String[]{"AGAMAYYA","AGAMREDDY","ALEX","ANAND","ANIL","ANJAYYA","ANJIREDDY",
		"ANTHAIAH","ANWAR KHAN","APPALARAJU","APPARAO","AROGYAYYA","ASHOK","ASHOK GOUD","ASHOK","KUMAR","BABU"};
	public static final int SEND_SMS=1,READ_SMS=2,RECEIVE_SMS=3,
  	WRITE_EXTERNAL_STORAGE=4,READ_EXTERNAL_STORAGE=5,VIBRATE=6,
	READ_CALL_LOG=7,READ_PHONE_STATE=8,READ_PHONE_NUMBERS=9,ANSWER_PHONE_CALLS=10,
	MODIFY_PHONE_STATE=11,CALL_PHONE=12,KILL_BACKGROUND_PROCESSES=13,
	WAKE_LOCK=14,MANAGE_EXTERNAL_STORAGE=15;

	public static final String[] permissions=new String[]
	{
		"Blank",
		android.Manifest.permission.SEND_SMS,
		android.Manifest.permission.READ_SMS,
		android.Manifest.permission.RECEIVE_SMS,
		android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
		android.Manifest.permission.READ_EXTERNAL_STORAGE,
		android.Manifest.permission.VIBRATE,
		android.Manifest.permission.READ_CALL_LOG,
		android.Manifest.permission.READ_PHONE_STATE,
		android.Manifest.permission.READ_PHONE_NUMBERS,
		android.Manifest.permission.MODIFY_PHONE_STATE,
		android.Manifest.permission.CALL_PHONE,
		android.Manifest.permission.KILL_BACKGROUND_PROCESSES,
		android.Manifest.permission.WAKE_LOCK,
		android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
		android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
		android.Manifest.permission.RECORD_AUDIO
	};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		tv=findViewById(R.id.mainTextView1);
		l("In Tokens");
		l(getIntent());
		SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(this);
		l(sharedPref);
		Map<String,?> optionKeys= sharedPref.getAll();
		l("optionKeys  "+optionKeys);
		String[] pr=optionKeys.toString().split(",");
		for(String s:pr){tv.append(lf+s);}
		tv.append(lf+"======================="+lf);
		sharedPref=null;
		sharedPref=getSharedPreferences(getString(R.string.SpOptions),MODE_PRIVATE);
		optionKeys=null;
		optionKeys= sharedPref.getAll();
		pr=optionKeys.toString().split(",");
		for(String s:pr){tv.append(lf+s);}
		tv.append(lf+"======================="+lf);
	//	String path=Environment.getExternalStorageDirectory()+"Tokens";
	/*	String[] keys=new String[optionKeys.keySet().size()];
		String[] values=new String[keys.length];
		int x=0;
		for(String key:optionKeys.keySet()){
			l(x+"  "+key+"     "+sharedPref.getString(key,"OOO"));
			keys[x]=key;values[x]=sharedPref.getString(key,"OOO");
			x++;
		}
		ArrayList<String> alkey=new ArrayList<String>();
		ArrayList<String> alVal=new ArrayList<String>();

		Set set=optionKeys.entrySet();
		Iterator it=set.iterator();
		while (it.hasNext())
		{
			Map.Entry entry=(Map.Entry) it.next();
			tv.append("\n"+entry.getKey().toString()+"   "+entry.getValue().toString());
			alkey.add(entry.getKey().toString());
			alVal.add(entry.getValue().toString());

		}
		
   		for(int y=0;y<alkey.size();y++){
			keys[y]=alkey.get(y);
			values[y]=alVal.get(y);
		}
		*/
		t("In Tokens=============In Tokens=============In Tokens=============In Tokens=============In Tokens");
		permission();
	//	l("Environment.isExternalStorageManager()  "+Environment.isExternalStorageManager());
	 //   l("Environment.getExternalStorageDirectory().getAbsolutePath()   " + Environment.getExternalStorageDirectory().getAbsolutePath());
	//	File file=new File(Environment.getExternalStorageDirectory()+File.separator+"Tokens"+File.separator);
		File path=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Tokens" + File.separator);
		if (!path.exists())path.mkdir();
		savePref(getString(R.string.path),STRING,path.getAbsolutePath());
		l(("path.exists()  "+path+"  "+path.exists()));
		l("path.mkdir()  "+path.mkdir());
		l("path.getName()  "+path.getName());
		try
		{
			l("path.getAbsolutePath()  "+ path.getAbsolutePath() +
			"  path.getCanonicalPath()  "+ path.getCanonicalPath());
		}
		catch (IOException e)
		{}

	try
		{
			File nf=new File(path,"PhoneLog.txt");
			nf.createNewFile();
			File pat=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Tokens1");
			if (!pat.exists())pat.mkdir();
			File nf1=new File(pat,"PhoneLog.txt");
			nf1.createNewFile();
			
		//	file.createNewFile();
		//	if(!file.exists())file.mkdir();
		}
		catch (IOException e)
		{}
		try
		{
		FileWriter nfw = new FileWriter(Environment.getExternalStorageDirectory() + "/tokens/token.txt",true);
		nfw.append("Nookaraju"+lf);
		
		nfw.flush();nfw.close();
			}
		catch (IOException e)
		{}
	//	createFile(null);
    }
	int CREATE_FILE=99;
	private void createFile(Uri pickerInitialUri) {
		
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
	    intent.setType("*/*");
	//	intent.putExtra(Intent.EXTRA_TITLE,null);

		// Optionally, specify a URI for the directory that should be opened in
		// the system file picker when your app creates the document.
		//intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

		startActivityForResult(intent, CREATE_FILE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
								 Intent resultData) {
		if (requestCode == CREATE_FILE
			&& resultCode == Activity.RESULT_OK) {
			// The result data contains a URI for the document or directory that
			// the user selected.
			Uri uri = null;
			if (resultData != null) {
				uri = resultData.getData();
				l(uri);
				l(uri.getPath());
				// Perform operations on the document using its URI.
			}
		}
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater=getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu)
	{
		t("OptionMenu Closed");l("OptionMenu Closed");
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.settings:
				l("onOptionsItemSelected(MenuItem item)-  settings");
				startActivity(new Intent(this, prefs.class));
				break;
			case R.id.search:
				l("onOptionsItemSelected(MenuItem item)-  search");
				startActivity(new Intent(this, search.class));
				break;
			case R.id.options:
				startActivity(new Intent(this, options.class));
				break;
			case R.id.sms:startActivity(new Intent(this,sendSms.class));
				break;
			//	case R.id.init:new Intentservice(). initialige();break;
			case R.id.log: startActivity(new Intent(this,log.class));break;
			case R.id.exit:finish();break;
			case R.id.TotalReset:new Tokens().initialige();break;
			case R.id.dateReset:savePref(getString(R.string.today_date_number),INTEGER,00);
				//	case R.id.: ;break;
				//	case R.id.: ;break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void initialige()
	{
		l("in initialise() Tokens"); 
		context=this;
		DATE_NUMBER = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		l("DATE_NUMBER  "+DATE_NUMBER);
	//	savePref(getString(R.string.today_date_number), INTEGER, DATE_NUMBER);
		savePref(getString(R.string.today_date_number),INTEGER,DATE_NUMBER);
		savePref(getString(R.string.present_token_number),INTEGER,1);
		String[] wk=getResources().getStringArray(R.array.weekdays);
		int weekDayNumber=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		l("weekDayNumber  "+weekDayNumber +"  "+wk[weekDayNumber-1]);
		
		path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Tokens" + File.separator);
		if (!path.exists())path.mkdirs();
		//	new File(getFilesDir(), all).delete();
		File AppointmentsFile = new File(getFilesDir(), aptm);
		File AppointmentsFileExt=new File(path,aptm);
		l("AppointmentsFile.delete()  "+AppointmentsFile.delete());

		l("AppointmentsFileExt.delete()  "+AppointmentsFileExt.delete());
		try
		{
			l("AppointmentsFile.createNewFile()  "+AppointmentsFile.createNewFile());
			l("AppointmentsFileExt.createNewFile()  "+AppointmentsFileExt.createNewFile());
		}
		catch (Exception e)
		{l("in initialise() creating Files Exception  "+e);}
		clearPrefs("EB");clearPrefs("LL");clearPrefs("TO");clearPrefs("NI");
		int wkHoliday=Integer.parseInt((String) getPref(getString(R.string.holiday_key),STRING));
		int wkHolidayExtra=Integer.parseInt((String) getPref(getString(R.string.holiday_extra_key),STRING));
		l("going to check weekDayNumber "+getString(R.string.holiday_key)+"  "+wkHoliday+"  "+getString(R.string.holiday_extra_key)+"  "+wkHolidayExtra);
		if (weekDayNumber ==wkHoliday||
			weekDayNumber == wkHolidayExtra)
		{
			l("TODAY IS HOLIDAY");
			blnHoliday = true;
			savePref(getString(R.string.today_blnHoliday), BOOLEAN, true);
			savePref(getString(R.string.today_message_tag), STRING, "TODAY " + wk[weekDayNumber-1] + " HOLIDAY");
			return;
		}
		
		l("NOT HOLIDAY");
		savePref(getString(R.string.today_blnHoliday), BOOLEAN,false);
		l("token_starting_time_key  "+PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.token_starting_time_key),"xxx"));

		savePref(getString(R.string.today_token_start_time), INTEGER,ToMinutes( PreferenceManager.getDefaultSharedPreferences(context).
																			   getString(getString(R.string.token_starting_time_key), "6:00")));
		savePref(getString(R.string.today_token_stop_time), INTEGER,ToMinutes( PreferenceManager.getDefaultSharedPreferences(context).
																			  getString(getString(R.string.token_stop_time_key), "17:00")));

		String todayOptions=context.getSharedPreferences(SpOptions, MODE_PRIVATE).
			getString(String.valueOf(DATE_NUMBER), null);
		l("todayOptions  " + todayOptions);
		if (todayOptions != null)
		{
			String[] opts=todayOptions.split(s);
			for (String s:opts)l("opts  " + s);
			if (opts[HOLIDAY] != nul)
			{
				blnHoliday = true;
				MESSAGE_TAG = opts[OPTIONS_TAG];
				savePref(getString(R.string.today_blnHoliday), BOOLEAN, true);
				savePref(getString(R.string.today_message_tag), STRING, MESSAGE_TAG);

				return;}
         	OP_START_MINUTE = Integer.parseInt(opts[OPTIONS_OP_START_TIME]);
			OP_CLOSE_MINUTE = Integer.parseInt(opts[OPTIONS_OP_CLOSE_TIME]);
			TOKENS = Integer.parseInt(opts[OPTIONS_TOKENS]);
		    MESSAGE_TAG = opts[OPTIONS_TAG];
			savePref(getString(R.string.today_op_Start_time), INTEGER, OP_START_MINUTE);
			savePref(getString(R.string.today_op_close_time), INTEGER, OP_CLOSE_MINUTE);
			savePref(getString(R.string.today_tokens), INTEGER, TOKENS);
			savePref(getString(R.string.today_message_tag), STRING, MESSAGE_TAG);			
			return;}
		OP_START_MINUTE =  PreferenceManager.getDefaultSharedPreferences(context).
			getInt(getString(R.string.op_start_key), 660);
		OP_CLOSE_MINUTE = PreferenceManager.getDefaultSharedPreferences(context).
			getInt(getString(R.string.op_close_key), 16 * 60);
		TOKENS = PreferenceManager.getDefaultSharedPreferences(context).
			getInt(getString(R.string.tokens_key), 30);
		MESSAGE_TAG = (String)getPref(getString(R.string.tag_key), STRING);
		savePref(getString(R.string.today_op_Start_time), INTEGER, OP_START_MINUTE);
		savePref(getString(R.string.today_op_close_time), INTEGER, OP_CLOSE_MINUTE);
		savePref(getString(R.string.today_tokens), INTEGER, TOKENS);
		savePref(getString(R.string.today_message_tag), STRING, MESSAGE_TAG);
	}
	
	public String timeAsString(String val)
	{
		String rtnVal="00:00";
		int time=Integer.parseInt(val);
		int gantalu=time / 60;
		int nimishalu=time - (gantalu * 60);
		rtnVal = String.format("%02d", gantalu) + ":" + String.format("%02d", nimishalu);
		return rtnVal;
	}
	public String timeAsString(int time)
	{
		String rtnVal="00:00";
		int gantalu=time / 60;
		int nimishalu=time - (gantalu * 60);
		rtnVal = String.format("%02d", gantalu) + ":" + String.format("%02d", nimishalu);
		return rtnVal;
	}


	private int ToMinutes(String s){
		if(s.contains(":")){
		try{
			int h= Integer.parseInt( s.substring(0,s.indexOf(":")));
			int m =Integer.parseInt(s.substring(s.indexOf(":")+1));
			l("in ToMinutes() val h m  "+s+"  "+s+"   "+h+"   "+m+"   "+h*60+m);
			return h*60+m;
		}catch(Exception e){l("in Tominutes  Exception "+e);}
		}
		return Integer.parseInt(s);

	}


	void savePref(String key, int type, Object value)
	{
		l("in savePref(key,type,value)  "+key+"   "+type+"   "+value);

		switch (type)
		{
			case STRING:
				l("String");
				PreferenceManager.getDefaultSharedPreferences(this).edit().
					putString(key, value.toString()).commit();
				l("{{{===}}}  "+getPref(key,type));
				break;
			case INTEGER:
				l("INTEGER");
				PreferenceManager.getDefaultSharedPreferences(this).edit().
					putInt(key, (int)value).commit();
				break;
			case LONG:
				l("LONG");
				PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().
					putLong(key, (long)value).commit();
				break;
			case BOOLEAN:
				l("BOOLEAN");
				PreferenceManager.getDefaultSharedPreferences(this).edit().
					putBoolean(key, (boolean) value).commit();
				break;
		}

		l(getPref(key,type));
	}

	public Object getPref(String key, int type)
	{
		l("in getPref(key,type)  "+key+"   "+type);
		switch (type)
		{
			case STRING:
				return PreferenceManager.getDefaultSharedPreferences(this).getString(key, null);
			case INTEGER:
				return PreferenceManager.getDefaultSharedPreferences(this).getInt(key, 0);
			case LONG:
				return PreferenceManager.getDefaultSharedPreferences(this).getLong(key, 0);
			case BOOLEAN:
				return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false);

		}
		return null;
	}	

	private void clearPrefs(String PreferencesName)
	{
		l("clearPrefs(String PreferencesName)  "+PreferencesName);
		l(getSharedPreferences(PreferencesName.toUpperCase(), MODE_PRIVATE));
		getSharedPreferences(PreferencesName.toUpperCase(), MODE_PRIVATE).edit().clear().apply();
	}
	
	public void oclMain(View v)
	{
		t("oclMain");
		if (v.getId() == R.id.mainTextViewSMS)
		{
			l("in Tokens mainTextViewSMS clicked ");
		//	setContentView(R.layout.send_sms);
			Intent intent=new Intent(this, sendSms.class);
			Bundle bundle=new Bundle();
			bundle.putString("sms", "apm");
			intent.putExtras(bundle);
			startActivity(intent);

			l("ocl()  SMS Button Pressed");	}
		if (v.getId() == R.id.mainTextViewSearch)
		{
			startActivity(new Intent(this, search.class));
			l("ocl()  Search Button Pressed");	}

	}

	public void ocl(View v)
	{
		t("ocl");

	}
	private void permission()
	{
		//l("================ in permission()");
		for (int x=1;x < permissions.length;x++)
		{
			if (ContextCompat.checkSelfPermission(getBaseContext(), permissions[x]) 	//android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
				== PackageManager.PERMISSION_DENIED)
			{requestPermissions(new String[]{permissions[x]}, x);}			
			else
			{//l(permissions[x].substring(permissions[x].indexOf("."))+" Permission Granted");
				//t("{{{{{{{{{{{{{}}}}}}}}}}}}}  "+permissions[x].substring(permissions[x].indexOf(".")));
			}
		}
	if(ContextCompat.checkSelfPermission(getBaseContext(),android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
		== PackageManager.PERMISSION_GRANTED){
		 t("android.Manifest.permission.MANAGE_EXTERNAL_STORAGE GRANTED");
		// l("========android.Manifest.permission.MANAGE_EXTERNAL_STORAGE GRANTED");
		}
	}



	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissionsResult, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	//	l("onRequestPermissionsResult("+requestCode+" permissionsResult  "+permissionsResult.length+"   grantResults  "+grantResults.length  );
		int x=0;
		if (permissionsResult.length > 0)
		{//	l("onRequestPermissionsResult("+x+"  "+requestCode+" permissionsResult  "+permissionsResult[x]+"   grantResults  "+grantResults[x]+")"  );
			if (requestCode == x)
			{
				if (permissions[x] == permissions[x])
				{
					if (grantResults[x] == PackageManager.PERMISSION_GRANTED)
					{
						//	l("in onRequestPermissionResult  "+permissions[x]);
						//	t(permissions[x]);
						startActivity(new Intent(Intent.ACTION_MANAGE_PACKAGE_STORAGE));
					}
				}
			}
		}
		
		}
	
	public void l(Object o)
	{Log.i(TAG, o.toString());
	new log().LogSave(Calendar.getInstance().getTime().toLocaleString()+o.toString());}
	public void t(Object o)
	{//Toast.makeText(getBaseContext(), o.toString(), Toast.LENGTH_LONG).show();
		Toast tost=new Toast(this);
		tost.setGravity(Gravity.CENTER, 0, 0);
		TextView ttv = new TextView(this);
		ttv.setBackgroundColor(Color.WHITE);
		ttv.setTextSize(22f);
		ttv.setBackgroundResource(R.drawable.buttonshape1);
		ttv.setPadding(10, 10, 10, 10);
		ttv.setTextColor(Color.BLUE);
		ttv.setGravity(Gravity.CENTER);
		ttv.setText(o.toString());
		tost.setView(ttv);
		tost.setDuration(Toast.LENGTH_LONG);
		tost.show();

	}
}

