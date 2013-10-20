package kr.co.rcsoft.mediatong;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		
		Log.e("MyBroadCastReceiver", "onReceive");

		MyIntentService.runIntentInService(context, intent);
        setResult(Activity.RESULT_OK, null, null);

	}
	
	

}


