package co.in.drh.Tokens;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.telephony.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.content.ClipboardManager;

import android.media.*;
import java.util.prefs.*;

public class PhoneStateReceiver extends BroadcastReceiver
{
	Context context;
	int sid;
	String icn="000000000000000000",state="SSSSSSSSSSSSSSSSS";
	boolean ringing=false,offHook=false,ideal=true;
	String[] sim=new String[]{" Nun "," Jio "," AirTel "};
	private static final String TAG="TOKENS",
	SOURCE="SOURCE",PHONE_CALL="PHONE_CALL",NAME="NAME",PHONE_NUMBER="PHONE_NUMBER";
	String name,number;
	@Override
	public void onReceive(Context p1, Intent intent)
	{
		context=p1;
		icn=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		t("phoneStateReceiver on receive {{{{{{+++===+++}}}}}} "+intent.getStringExtra(TelephonyManager.EXTRA_STATE));
		l("phoneStateReceiver on receive {{{{{{+++===+++}}}}}} "+intent.getStringExtra(TelephonyManager.EXTRA_STATE));
		try{
			Bundle bdl=intent.getExtras();
			if(bdl!=null){
				Log.e(TAG,"bundle not null  "+bdl);
				Toast.makeText(context,bdl.toString(),900).show();
				Set<String> set= bdl.keySet();
				for(String k:bdl.keySet()){
					Log.i(TAG,"key  "+k+"   value     "+bdl.get(k));
				}
				Log.i(TAG,"bdl size  "+set.size());
				//	String[] keys=new String[]( set.toArray());
				new save().keep("Phone Is Ringing  "+bdl.toString());
				sid=bdl.getInt("simId",-1);
				l("Sim Id  "+sid);
		}
			state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			l("state  "+state);
			if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
			//	icn=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		//		icn=icn;//+sim[sid+1];
				l("Ringing Incoming Number "+icn);
				ringing=true;offHook=ideal=false;
				Toast.makeText(context, "Phone Is Ringing ======= "+icn, Toast.LENGTH_LONG).show();
					new save().keep("Phone Is Ringing  "+icn);
			}

			if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
			//	icn=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			//	icn=icn+sim[sid+1];
				offHook=true;ideal=offHook=false;
				l("OffHook");
				Toast.makeText(context, "Call Recieved  ======= "+icn, Toast.LENGTH_LONG).show();
					new save().keep("Call Received  "+icn);
				startSendSms(p1);
			}

			if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
					Toast.makeText(context, "Phone Is Idle=======", Toast.LENGTH_LONG).show();
					l("Ideal");
					ideal=true;ringing=offHook=false;
					new save().keep("Phone Is Idel  ");

			}
		}
		catch(Exception e){e.printStackTrace();}
		
		
		
		}

	private void startSendSms(Context p1)
	{
		l("Outside Analysis  state, number  " + state + "  icn  " + icn);
		SharedPreferences pref=context.getSharedPreferences("phoneNumber", context.MODE_PRIVATE);
		pref.edit().putString("icn", icn).commit();

		ClipboardManager clipboard = (ClipboardManager)context. getSystemService(Context.CLIPBOARD_SERVICE); 
		ClipData clip = ClipData.newPlainText("text", icn);
		clipboard.setPrimaryClip(clip);
		//if (offHook)
		{
			//if(ringing){
			t("=======  in phoneReceiver  if(offhuck) starting sendSms Activity");
			l("=======  in phoneReceiver  if(offhuck) starting sendSms Activity");
			AudioManager am=(AudioManager)context.getSystemService(context.AUDIO_SERVICE);
			am.setMode(AudioManager.MODE_IN_CALL);
			am.setSpeakerphoneOn(true);
			Intent uiIntent=new Intent(context, sendSms.class);
			uiIntent.putExtra(SOURCE, PHONE_CALL);
			uiIntent.putExtra(NAME, "APM");
			uiIntent.putExtra(PHONE_NUMBER, icn);
			uiIntent.setFlags(
			Intent.FLAG_ACTIVITY_NEW_TASK|
			Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
			|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			p1.startActivity(uiIntent);
		}
	}
	public void l(Object o)
	{Log.i(TAG,"in PhoneStateReceiver "+ o.toString());}
	public void t(Object o)
	{Toast.makeText(context, o.toString(), Toast.LENGTH_LONG).show();
		Toast tost=new Toast(context);
		tost.setGravity(Gravity.CENTER, 0, 0);
		TextView ttv = new TextView(context);
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
