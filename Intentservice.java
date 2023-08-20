package co.in.drh.Tokens;
import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import java.io.*;
import java.util.*;
import java.text.*;
import android.widget.*;
import android.util.*;
import android.preference.*;
import android.view.*;
import android.graphics.*;

public class Intentservice extends IntentService
{
	static final int APTM=1,ALL=2,TOKEN=0,NAME=1,NUMBER=2;
	static final String aptm="aptm.txt",all="all.txt",lf="\n",sp="  ",s="#",TAG="TOKENS",
	OPTIONS="OPTIONS",SpOptions="sp_options",nul="null",Holiday="Holiday",SOURCE="SOURCE";
	final static int OPTIONS_DATE=0,HOLIDAY=0,OPTIONS_TAG=1,OPTIONS_OP_START_TIME=2,OPTIONS_OP_CLOSE_TIME=3,OPTIONS_TOKENS=4;
	//Tokens=4,DateOfEvent=5, OPTIONS_START=1,OPTIONS_CLOSE=2;
	int DATE_NUMBER,OP_START_MINUTE,OP_CLOSE_MINUTE,MINUTE,TOKEN_START_MINUTE=0,TOKEN_CLOSE_MINUTE=23 * 60 + 59, TOKENS,PRESENT_TOKEN_NUMBER;
	//String dateNumber,opStartTime,opCloseTime,tokensStartTime,tokensCloseTime;	
	boolean blnHoliday,blnTokensOver;
	File path,localPath;
	int[] simIds=new int[3];

	private String MESSAGE_TAG;

	Context context=getBaseContext();
	// String address,mes;
	long tms;
	SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy hh:mm:ss:SSS aa");
	final int STRING=0,INTEGER=1,LONG=2,BOOLEAN=3,FLOAT=4;
	public  Intentservice()
	{super("Intentservice");}
	Tokens tkns=new Tokens();	
	void getSims(){
		SubscriptionManager sm=(SubscriptionManager)getSystemService(context.TELEPHONY_SUBSCRIPTION_SERVICE);
		if(sm!=null){
			int numberOfSims=sm.getActiveSubscriptionInfoCountMax();
			if(numberOfSims>1){
				List list=sm.getActiveSubscriptionInfoList();
			//	SubscriptionInfo sinfo1=(SubscriptionInfo) list.get(0);
				simIds[0]=((SubscriptionInfo)(list.get(0))).getSubscriptionId(); 
				simIds[1]=((SubscriptionInfo)(list.get(1))).getSubscriptionId();
				}
		}
	}
	@Override
	protected void onHandleIntent(Intent p1)
	{
		//tkns.t("<<<<<<<<<<<<<<<<<<<<<<  in Intentservice onHandleIntent()  >>>>>>>>>>>>>>>>>>>");
		Toast.makeText(getBaseContext(), "<<<<<<<<<<<<<<<<<<<<<<  in Intentservice onHandleIntent()  >>>>>>>>>>>>>>>>>>>",
					   Toast.LENGTH_LONG).show();
		t("Toast.makeText(getBaseContext(),<<<<<<<<<<<<<<<<<<<<<<  in Intentservice onHandleIntent()  >>>>>>>>>>>>>>>>>>>," +
		  "Toast.LENGTH_LONG).show();");

		//  Log.i("Intentservice", "in Intentservice onHandleIntent()  " + p1);
		l("In Intentservice onHandleIntent()  " + p1);
		if (p1.getStringExtra("Source").equals("receiver"))
		{
			getSms(p1.getExtras());}
		if (p1.getStringExtra("Source").equals("manual"))
		{
			dealWithSms(p1.getStringExtra("Name"), p1.getStringExtra("Address"));
		}
	}

	private void dealWithSms(String mes, String address)
	{
		l("in dealWithSms() " + mes + "   " + address);
		fileIt(ALL,
			   sdf.format(System.currentTimeMillis()) + "    "
			   + address + "  " + mes + "  " +
			   sdf.format(tms) + "  " + "\n");
		if (mes.contains("{{{"))
		{
			l("mes.contains()  {{{");
			setIt(mes);}

		if (address.toLowerCase().contains("union bank of india") ||
			mes.contains("Your SB A/c") & mes.contains("03832") ||
			mes.contains("0013") ||
			mes.toUpperCase().contains("OTP")) corpSend();
		if (mes.toLowerCase().contains("apm") ||
			mes.toLowerCase().contains("atm") ||
			mes.toLowerCase().contains("app") ||
			mes.toLowerCase().contains("token") ||
			mes.toLowerCase().contains("apt"))
			sms(address, mes);
	}

	private void getSms(Bundle bundle)
	{
		l(" in getSms()  bundle = " + bundle);
		SmsMessage[] msgs = null;
		String address=null,mes=null;
		if (bundle != null)
		{ 

			Object[] pdus = (Object[]) bundle.get("pdus");
			l("pdus   " + pdus + "  length " + pdus.length);
			msgs = new SmsMessage[pdus.length];            
			msgs[0] = SmsMessage.createFromPdu((byte[])pdus[0]);
			address = msgs[0].getOriginatingAddress();
			tms = msgs[0].getTimestampMillis();
			l("***********+++++++*********  "+msgs[0].getServiceCenterAddress());
			mes = "";
			//	ts = String.valueOf(tms);
			for (int i=0; i < msgs.length; i++)
			{
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				l("messageBody  -->" + msgs[i].getMessageBody());
				mes = mes + msgs[i].getMessageBody();
				l("Total Message  " + mes + "  Address  " + address + "  tms  " + tms);}
		}
		
		dealWithSms(mes, address);
	}

	private String getName(String ms)
	{
		String name;
		if (ms.toLowerCase().contains("hellow"))
		{}
		if (ms.toLowerCase().contains("hello"))
		{}
		if (ms.toLowerCase().contains("hai"))
		{}
		if (ms.toLowerCase().contains("madam"))
		{}
		if (ms.toLowerCase().startsWith("apm"))
		{}
		if (ms.toLowerCase().startsWith("atm"))
		{}
		if (ms.toLowerCase().startsWith("app"))
		{}
		if (ms.toLowerCase().startsWith("apt"))
		{}
		name = ms.substring(3);
		l("in getName()  " + name);
		return name;
	}

	private void sms(String adress, String msg)
	{
		l("in sms()");
		if (dateChanged()){initialige();}
		String name=getName(msg);
		String adres=stripCountryCode(adress);
		if ((boolean) getPref(getString(R.string.today_blnHoliday), BOOLEAN))
		{
			if (verifyNumber(adres, "HD"))
			{
				send_sms(adress, "Hello " + name + "\n" + getPrefValue(getString(R.string.today_message_tag)));
				saveNumber(adres, "HD");
				return;}
			l("verifyNumber -- already one message sent  "+false+sp+sp+ "  returning");
			return;
		}
		/*	 ***** this to check later about persistancy of TOKEN_START_MINUTE
		 if (TOKEN_START_MINUTE >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 +
		 Calendar.getInstance().get(Calendar.MINUTE))  */
		if ((int)getPref(getString(R.string.today_token_start_time), INTEGER) >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 +
			Calendar.getInstance().get(Calendar.MINUTE))

		{
			if (verifyNumber(adres, "EB"))
			{
				//EB EARLY_BIRD
				l("EB");
				send_sms(adress, "Hello  " + name + "  TOKENS ARE ISSUED FROM 6:00 AM.TO 3:00 PM PLEASE TRY LATER");
				saveNumber(adres, "EB");
				return;
			}
			l("verifyNumber -- already one message sent  "+false+sp+sp+ "  returning");
			return;
		}
		if ((int)getPref(getString(R.string.today_token_stop_time), INTEGER) < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 +
			Calendar.getInstance().get(Calendar.MINUTE))
		{
			if (verifyNumber(adres, "LL"))
			{
				//LL LATE_LATHEEF
				l("LL");
				send_sms(adress, "TOKENS ARE ISSUED TILL 3:00 PM.PLEASE TRY TOMMOROW");
				saveNumber(adres, "LL");
				return;
			}
			l("verifyNumber -- already one message sent "+false+sp+sp+ "  returning");
			return;
		}
		if ((boolean) getPref(getString(R.string.today_tokens_over), BOOLEAN))
		//(int)getPref("PRESENT_TOKEN_NUMBER", INTEGER) > (int)getPref("TOKENS", INTEGER))
		{
			if (verifyNumber(adres, "TO"))
			{
				// TO TOKENS_OVER
				l("TO");
				send_sms(adress, "SORRY TODAY ALL APPOINTMENTS ARE OVER PLEASE TRY TOMMORO.PLEASE DO NOT COME TODAY");
				saveNumber(adres, "TO");
				return;
			}
			l("verifyNumber -- already one message sent "+false+sp+sp+ "  returning");
			return;
		}
		if (verifyNumber(adres, "NI"))
		{
			//NI  NUMBER ISSUED
			l("NI");
			PRESENT_TOKEN_NUMBER = getPref(getString(R.string.present_token_number), INTEGER);
			send_sms(adress, "HAI " + name + "  YOUR TOKEN NUMBER IS " +
					 (PRESENT_TOKEN_NUMBER)+sp + getPrefValue(getString(R.string.today_message_tag)) +
					 "\nSumaarugaa Mee Samayam " + aproximateTime(PRESENT_TOKEN_NUMBER));
			fileIt(APTM, PRESENT_TOKEN_NUMBER + "    " + name + "    " + adres);
			savePref(getString(R.string.present_token_number), INTEGER, PRESENT_TOKEN_NUMBER + 1);
			if (PRESENT_TOKEN_NUMBER + 1 > (int)getPref(getString(R.string.today_tokens), INTEGER))
			{
				savePref(getString(R.string.today_tokens_over), BOOLEAN, true);
			}
		}
		l("verifyNumber -- already one message sent"+sp+sp+ "  returning");
		return;
	}
	/*
	 approximate Time=
	 opstartTime+tokenNumber*duration
	 example 660+(30*10)=660+300=900=3pm
	 */

	private String aproximateTime(int tokenNumber)
	{
		int opstart=getPref(getString(R.string.today_op_Start_time), INTEGER);
		
		int eachSlot = Integer.parseInt(getPrefValue(getString(R.string.each_slot_duration_key)));
		int AproximateTime=tokenNumber * eachSlot + opstart;
		l("tokenNumber * eachSlot + opstart "+tokenNumber+sp+eachSlot+sp+opstart+sp+AproximateTime);
		l("AproximateTime   "+timeAsString(AproximateTime));
		return timeAsString(AproximateTime);
	}



	private void saveNumber(String adres, String sp)
	{
		l("in saveNumber --------------->");
		adres = stripCountryCode(adres);
		getSharedPreferences(sp.toUpperCase(), MODE_PRIVATE).edit().putString(sp.toLowerCase(),
																			  getSharedPreferences(sp.toUpperCase(), MODE_PRIVATE).getString(sp.toLowerCase(),"") + adres + s).apply();
		l(sp.toUpperCase() + "  " + getSharedPreferences(sp.toUpperCase(), MODE_PRIVATE).getString(sp.toLowerCase(), ""));
	}

	private String stripCountryCode(String adres)
	{
	l("private void stripCountryCode(String adres) "+adres);
		adres = !adres.startsWith("+91") ?adres: adres.substring(3);
	l("private void stripCountryCode(String adres) "+adres);	
	return adres;
	}
	private void send_sms(String address, String message)
	{
		l("in send_sms(address  " + address + "  message  " + message + ")");
		SmsManager smsManager  = SmsManager.getDefault();
		ArrayList<String> messageList=smsManager.divideMessage(message);
		l("messageList.size  " + messageList.size());
		smsManager.sendMultipartTextMessage(address, null, messageList, null, null);
		
	}

	private boolean verifyNumber(String num, String spName)
	{
		boolean catchh=false;
		try
		{
			l("------------> in verifyNumber  " + num + "   " + spName + "   " + getPref(spName, STRING) + "////");
			l("in verifyNumber  " + num + "   " + spName + "   " + getSharedPreferences(spName.toUpperCase(), MODE_PRIVATE).getString(spName.toLowerCase(), null));
			String string=getSharedPreferences(spName.toUpperCase(), MODE_PRIVATE).getString(spName.toLowerCase(), null);
			l("string  "+string);
			if(string.contains(num)){
				l("number already present");
				return false;}

		}
		catch (Exception e)
		{
			catchh = true;
			l("in verifyNumber exceptin " + e);
			//getSharedPreferences(spName.toUpperCase(), MODE_PRIVATE).edit().putString(spName, " ");
			//	savePref(spName,STRING,num);
			saveNumber(num, spName);
		}
		l("number not present  entered catch  " + catchh);
		return true;
	}
	private boolean dateChanged()
	{
		DATE_NUMBER = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		int savedDateNumber=getPref(getString(R.string.today_date_number), INTEGER);
		l("in DateChanged() DATE_NUMBER  " + DATE_NUMBER + "   savedDateNumber   " + savedDateNumber);
		if (DATE_NUMBER == savedDateNumber){return false;}
		else {savePref(getString(R.string.today_date_number), INTEGER, DATE_NUMBER);
		l("DATE_NUMBER Changed saving and returning true");
		return true;}
	}
///////

	public String getPrefValue(String key)
	{//
		return PreferenceManager.getDefaultSharedPreferences(context).
			getString(key, null);
	}


	public void initialige()
	{
		l("in initialise()"); 
		savePref(getString(R.string.present_token_number), INTEGER, 1);
		savePref(getString(R.string.today_tokens_over),BOOLEAN,false);
		savePref(getString(R.string.today_message_tag),STRING," ");
		String[] wk=getResources().getStringArray(R.array.weekdays);
		int weekDayNumber=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		l("weekDayNumber  " + weekDayNumber + "  " + wk[weekDayNumber - 1]);
		savePref(getString(R.string.today_date_number), INTEGER, DATE_NUMBER);
		savePref(getString(R.string.today_tokens_over),BOOLEAN,false);
		path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Tokens" + File.separator);
		if (!path.exists())path.mkdirs();
		l("path " + path + "  path.toString()  " + path.toString() + "  path.toPath  " + path.toPath());
		l("path.getAbsolutePath()  " + path.getAbsolutePath());
		savePref(getString(R.string.path), STRING, path);
		//	new File(getFilesDir(), all).delete();
		File AppointmentsFile = new File(getFilesDir(), aptm);
		File AppointmentsFileExt=new File(path, aptm);
		l("AppointmentsFile.delete()  " + AppointmentsFile.delete());

		l("AppointmentsFileExt.delete()  " + AppointmentsFileExt.delete());
		try
		{
			l("AppointmentsFile.createNewFile()  " + AppointmentsFile.createNewFile());
			l("AppointmentsFileExt.createNewFile()  " + AppointmentsFileExt.createNewFile());
		}
		catch (Exception e)
		{l("in initialise() creating Files Exception  " + e);}
		clearPrefs("EB");clearPrefs("LL");clearPrefs("TO");clearPrefs("NI");clearPrefs("HD");
		int wkHoliday=Integer.parseInt((String) getPref(getString(R.string.holiday_key), STRING));
		int wkHolidayExtra=Integer.parseInt((String) getPref(getString(R.string.holiday_extra_key), STRING));
		l("going to check weekDayNumber " + getString(R.string.holiday_key) + "  " + wkHoliday + "  " + getString(R.string.holiday_extra_key) + "  " + wkHolidayExtra);
		if (weekDayNumber == wkHoliday ||
			weekDayNumber == wkHolidayExtra)
		{
			l("TODAY IS HOLIDAY");
			blnHoliday = true;
			savePref(getString(R.string.today_blnHoliday), BOOLEAN, true);
			savePref(getString(R.string.today_message_tag), STRING, "TODAY " + wk[weekDayNumber - 1] + " HOLIDAY");
			return;
		}
		l("NOT HOLIDAY");
		savePref(getString(R.string.today_blnHoliday), BOOLEAN, false);
		l("token_starting_time_key  " + PreferenceManager.getDefaultSharedPreferences(context).getString(getString(R.string.token_starting_time_key), "xxx"));

		savePref(getString(R.string.today_token_start_time), INTEGER, ToMinutes(PreferenceManager.getDefaultSharedPreferences(context).
		getString(getString(R.string.token_starting_time_key), "6:00")));
		savePref(getString(R.string.today_token_stop_time), INTEGER, ToMinutes(PreferenceManager.getDefaultSharedPreferences(context).
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
         	OP_START_MINUTE = ToMinutes(opts[OPTIONS_OP_START_TIME]);
			OP_CLOSE_MINUTE = ToMinutes( opts[OPTIONS_OP_CLOSE_TIME]);
			TOKENS = ToMinutes(opts[OPTIONS_TOKENS]);
		    MESSAGE_TAG = opts[OPTIONS_TAG];
			savePref(getString(R.string.today_op_Start_time), INTEGER, OP_START_MINUTE);
			savePref(getString(R.string.today_op_close_time), INTEGER, OP_CLOSE_MINUTE);
			savePref(getString(R.string.today_tokens), INTEGER, TOKENS);
			savePref(getString(R.string.today_message_tag), STRING, MESSAGE_TAG);			
			return;}
		OP_START_MINUTE = ToMinutes( PreferenceManager.getDefaultSharedPreferences(context).
			getString(getString(R.string.op_start_key), "660"));
		l("OP_START_MINUTE = "+OP_START_MINUTE);
		
			OP_CLOSE_MINUTE = ToMinutes( PreferenceManager.getDefaultSharedPreferences(context).
			getString(getString(R.string.op_close_key),String.valueOf( 16 * 60)));
		TOKENS = ToMinutes( PreferenceManager.getDefaultSharedPreferences(context).
			getString(getString(R.string.tokens_key), "30"));
		MESSAGE_TAG = (String)getPref(getString(R.string.tag_key), STRING);
		savePref(getString(R.string.today_op_Start_time), INTEGER, OP_START_MINUTE);
		savePref(getString(R.string.today_op_close_time), INTEGER, OP_CLOSE_MINUTE);
		savePref(getString(R.string.today_tokens), INTEGER, TOKENS);
		savePref(getString(R.string.today_message_tag), STRING, MESSAGE_TAG);
	}

	private void corpSend()
	{l("in corpSend()");}
	private void setIt(String Instruction)
	{l("in setIt()");}

	private void fileIt(String FileName, String Stuff)
	{
		try
		{
			FileWriter fwt=new FileWriter(path + FileName, true);
			fwt.append(Stuff + "\n");
			//		address + "  " + mes + "  " +sdf.format(tms)+ "  " +sdf.format(System.currentTimeMillis())+"\n");
			fwt.flush();fwt.close();

		}
		catch (Exception e)
		{tkns.l("in Intentservice fileIt() Exception " + e);}		

	}

	private void fileIt(int file, String stuff)
	{
		l("in fileIt(file,stuff)  " + file + "   " + stuff);
		l("in fileIt(file,stuff)  " + all + " aptm  " + aptm);
		try
		{
			if (file == ALL)
			{
				
				FileWriter fw= new FileWriter(new File(getFilesDir(),getString(R.string.all_messages)), true);
				fw.append(stuff + lf);
				fw.flush();fw.close(); 
				fw = null;
				path = new File(getString(R.string.path));

				FileWriter fwSd= new FileWriter(new File(path,getString(R.string.all_messages)), true);
				fwSd.append(stuff + lf);
				fwSd.flush();fwSd.close();fwSd = null;

			}

			if (file == APTM)
			{
				FileWriter fw= new FileWriter(new File(getFilesDir(),getString(R.string.appointments)), true);
				fw.append(stuff + lf);
				fw.flush();fw.close(); 
				fw = null;
				FileWriter fwSd= new FileWriter(new File(getString(R.string.path),getString(R.string.appointments)), true);
				fwSd.append(stuff + lf);
				fwSd.flush();fwSd.close();fwSd = null;

			}
		}
		catch (Exception e)
		{}
	}



	void savePref(String key, int type, Object value)
	{
		l("in savePref(key,type,value)  " + key + "   " + type + "   " + value+sp+value.getClass());

		switch (type)
		{
			case STRING:
				l("String");
				PreferenceManager.getDefaultSharedPreferences(context).edit().
					putString(key, value.toString()).commit();
			//	l("{{{{{{{{{=====}}}}}}}}}  " + getPref(key, type));
				break;
			case INTEGER:
				l("INTEGER");
				PreferenceManager.getDefaultSharedPreferences(context).edit().
					putInt(key, (int)value).commit();
				break;
			case LONG:
				l("LONG");
				PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().
					putLong(key, (long)value).commit();
				break;
			case BOOLEAN:
				l("BOOLEAN");
				PreferenceManager.getDefaultSharedPreferences(context).edit().
					putBoolean(key, (boolean) value).commit();
				break;
		}

	//	l("{{{{{{{{{===savePref===}}}}}}}}}  " + getPref(key, type));
	}

	public Object getPref(String key, int type)
	{
		l("in getPref(key,type)  " + key + "   " + type);
		switch (type)
		{
			case STRING:
				return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
			case INTEGER:
				return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
			case LONG:
				return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, 0);
			case BOOLEAN:
				return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);

		}
		return null;
	}	

	private void clearPrefs(String PreferencesName)
	{
		l("clearPrefs(String PreferencesName)  " + PreferencesName);
		l(getSharedPreferences(PreferencesName.toUpperCase(), MODE_PRIVATE).getString(PreferencesName.toLowerCase(),"Nothing"));
		getSharedPreferences(PreferencesName.toUpperCase(), MODE_PRIVATE).edit().clear().apply();
		getSharedPreferences(PreferencesName.toUpperCase(), MODE_PRIVATE).edit().putString(" ", PreferencesName.toLowerCase()).apply();
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


	private int ToMinutes(String s)
	{
		if(s.contains(":")){
		try
		{
			int h= Integer.parseInt(s.substring(0, s.indexOf(":")));
			int m =Integer.parseInt(s.substring(s.indexOf(":") + 1));
			l("in ToMinutes() val h m  " + s + "  " + s + "   " + h + "   " + m + "   " + h * 60 + m);
			return h * 60 + m;
		}
		catch (Exception e)
		{l("in Tominutes  Exception " + e);}}
		return Integer.parseInt(s);

	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		context = getApplicationContext();
		t("intentservice onStart() " + intent + "   " + startId);
		l("intentservice onStart() " + intent + "   " + startId);
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate()
	{
		t("intentservice onCreate() ");
		l("intentservice onCreate() ");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		t("intentservice onStartCommand() " + intent + "  " + flags + "  " + startId);
		l("intentservice onStartCommand() " + intent + "  " + flags + "  " + startId);
		String str=PreferenceManager.getDefaultSharedPreferences(this).
			getString(getString(R.string.op_start_key), "660");
		OP_START_MINUTE=ToMinutes(str);
		l("OP_START_MINUTE = "+OP_START_MINUTE);
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{String filePath = Environment.getExternalStorageDirectory() + "/TOKENS/Log.txt";
		//    String filePath1 = Environment.getExternalStorageDirectory() + "/logcatOrT.txt";
        try
        {
            Runtime.getRuntime().exec(new String[]{"logcat", "-f", filePath, "TOKENS", "Tokens:V"});
			//        Runtime.getRuntime().exec(new String[]{"logcat",  filePath1});
        }
        catch (IOException e)
        {}


		t("intentservice onDestroy");
		l("intentservice onDestroy");
	/*	String FilePath = Environment.getExternalStorageDirectory() + "/tokens/Log.txt";
		//    String filePath1 = Environment.getExternalStorageDirectory() + "/logcatOrT.txt";
        try
        {
            Runtime.getRuntime().exec(new String[]{"logcat", "-f", FilePath, "wc", "wc:V"});
			//        Runtime.getRuntime().exec(new String[]{"logcat",  filePath1});
        }
        catch (IOException e)
        {}
	*/
		super.onDestroy();
	}



	void l(Object o)
	{Log.i(TAG, "In IntentService " + o.toString());
		new log().LogSave("+++++++>>>>  " + Calendar.getInstance().getTime().toLocaleString() + o.toString());}

	void t(Object o)
	{
		Toast tost=new Toast(this);
		tost.setGravity(Gravity.CENTER, 100, 100);
		TextView ttv = new TextView(this);
		ttv.setBackgroundColor(Color.WHITE);
		ttv.setPadding(10, 10, 10, 10);
		ttv.setTextColor(Color.BLUE);
		ttv.setGravity(Gravity.CENTER);
		ttv.setText(o.toString());
		tost.setView(ttv);
		tost.setDuration(Toast.LENGTH_LONG);
		tost.show();
	}
}
/*
	 //     corpSend();

	 if (mes.contains("{{{"))
	 {
	 //abortBroadcast();
	 new Knr().pro(mes, context);					
	 return;
	 }
	 /*
	 **03832 is Debited for Rs.13590 on 26-08-2021 23:06:34 by Debit Card Swipe Avl Bal Rs:484355.27 -

	 if (address.toLowerCase().contains("union bank of india"))
	 {
	 Toast.makeText(context, mes + "\n   " + address + "\n==============", Toast.LENGTH_LONG).show();
	 Log.i(TAG, mes + "\n   " + address + "\n==============");
	 corpSend();
	 }
	 if (mes.contains("Your SB A/c") & mes.contains("03832"))
	 {
	 Toast.makeText(context, "mes" + "\n   " + mes + "\n==============", Toast.LENGTH_LONG).show();
	 Log.i(TAG, "corpSmsReceiver  " + mes + "\n   " + "\n==============");
	 corpSend();
	 }

	 if (mes.toUpperCase().contains("OTP"))
	 {
	 Log.i(TAG, "corpSmsReceiver OTP " + mes + "\n   " + "\n==============");
	 corpSend();
	 }
	 */
	 /*
	
	String me=mes.toLowerCase().trim();
	if (me.startsWith("apm") | me.startsWith("apn") | me.startsWith("app") | me.startsWith("apt"))
	{
		//		abortBroadcast();
		//      Toast.makeText(context,"&&&&&&&&   apm    &&&&&&&&",Toast.LENGTH_SHORT).show();
		Intent broadcastIntent = new Intent();//context,timeService.class);
		broadcastIntent.setAction(INTENT_ACTION);
		broadcastIntent.putExtra("sms", new String[]{address,mes,ts});
		SharedPreferences sp=context.getSharedPreferences("SSms", context.MODE_WORLD_READABLE);
		if (sp.getBoolean("SERVICE_RUNNING", false)){
			context.sendBroadcast(broadcastIntent);
			Log.i(TAG,"SERVICE_RUNNING = TRUE");}
		else
		{
			Log.i(TAG,"SERVICE_RUNNING = FALSE");
			Intent ServiceIntent=new Intent(context, timeService.class);
			ServiceIntent.putExtra("sms", new String[]{address,mes,ts});
			Toast.makeText(context, "/////////////  From SMSReceiver timeService not running strting", Toast.LENGTH_LONG).show();
			Log.i("ssms br ", "From SMSReceiver timeService not running strting");
			context.startService(ServiceIntent);}
		abortBroadcast();}


    }

    public void allMessages(Context context, String ad, String me, long mts)
    {
        //     Toast.makeText(context," =============  in all Messages   ==============",500).show();
        try
        {
            FileWriter fwt=new FileWriter("/storage/emulated/0/Appointments/AllMessages.apm", true);
            fwt.append(
                ad + "  " + me + "  " + new LocalUtilities(context).df(mts) + "   " + Calendar.getInstance().getTime() + "\n"); 
            fwt.flush();fwt.close();
        }
        catch (Exception e)
        {}
    }
    public void corpSend()
    {
        SmsManager sm=SmsManager.getDefault();
        sm.sendTextMessage("9700224959", null, mes, null, null);
        SharedPreferences sp=tcontext.getSharedPreferences("SSms", tcontext.MODE_WORLD_READABLE);
		//    if (sp.getBoolean("SIRI", false)){sm.sendTextMessage("9985992837", null, mes, null, null);}


        try
        {
            File file=new File(Environment.getExternalStorageDirectory().toString()+ File.separator+"Appointments"+File.separator);
            if(!file.exists())file.mkdir();
            FileWriter fwt=new FileWriter(file+"Corp.txt", true);
            fwt.append(
                mes + "     " + Calendar.getInstance().getTime() + "\n"); 
            fwt.flush();fwt.close();
        }
        catch (Exception e)
        {}

*/
	

