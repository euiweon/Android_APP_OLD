package com.etoos.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class ConnReceiver extends BroadcastReceiver {
	String action;
	EtoosBaseActivity activity;

	public ConnReceiver() {
		super();
	}

	public ConnReceiver(EtoosBaseActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		action = intent.getAction();



		if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
		{
			//네트워크 변화가 생길경우에 처리할 코드 삽입
			NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			NetworkInfo.DetailedState state = info.getDetailedState();
			if (state == NetworkInfo.DetailedState.CONNECTED) 
			{
				//Toast.makeText(context, "연결되었습니다.", Toast.LENGTH_LONG).show();
				activity.dataConnect = true;
				
				activity.DataConnect();
			} 
			else if (state == NetworkInfo.DetailedState.DISCONNECTED)
			{
				//Toast.makeText(context, "연결이 끊어졌습니다.", Toast.LENGTH_LONG).show();
				activity.dataConnect = false;
				activity.DataDisconnect();
			}
		}
	}
}