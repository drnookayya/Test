package co.in.drh.Tokens;
import android.content.*;
import android.util.*;
import android.os.*;

public class Receiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context p1, Intent p2)
	{
		Log.i("TOKENS","in receiver onReceive()  "+p2);
		Intent intent=new Intent(p1,Intentservice.class);
		Bundle bundle=p2.getExtras();
		intent.putExtras(bundle);
		intent.putExtra("Source","receiver");
		p1.startService(intent);
	}

}

