package kr.co.rcsoft.mediatong;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MyIntentService extends IntentService {

	 private static PowerManager.WakeLock sWakeLock;
	    private static final Object LOCK = MyIntentService.class;
	    
	    static void runIntentInService(Context context, Intent intent) {
	        synchronized(LOCK) {
	            if (sWakeLock == null) {
	                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	                sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "my_wakelock");
	            }
	        }
	        sWakeLock.acquire();
	        intent.setClassName(context, MyIntentService.class.getName());
	        context.startService(intent);
	    }
	
	
	public MyIntentService() {
		super("MyIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {
            String action = intent.getAction();
            if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
                handleRegistration(intent);
            } else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
                handleMessage(intent);
            }
        } finally {
            synchronized(LOCK) {
                sWakeLock.release();
            }
        }
    }


	private void handleMessage(Intent intent) {
		// TODO Auto-generated method stub
		 // server sent 2 key-value pairs, score and time
	    String score = intent.getStringExtra("score");
	    String time = intent.getStringExtra("time");
	    // generates a system notification to display the score and time
	}


	private void handleRegistration(Intent intent) {
		// TODO Auto-generated method stub
		 String registrationId = intent.getStringExtra("AIzaSyB5LWvjDgYMuSIFLX1jMGVJWGSaIODyS5E");
		    String error = intent.getStringExtra("error");
		    String unregistered = intent.getStringExtra("unregistered");     
		    
		    Log.i("Id", "registrationId = " + registrationId);
		    
		    // registration succeeded
		    if (registrationId != null) {
		        // store registration ID on shared preferences
		        // notify 3rd-party server about the registered ID
		    }
		        
		    // unregistration succeeded
		    if (unregistered != null) {
		        // get old registration ID from shared preferences
		        // notify 3rd-party server about the unregistered ID
		    } 
		        
		    // last operation (registration or unregistration) returned an error;
		    if (error != null) {
		        if ("SERVICE_NOT_AVAILABLE".equals(error)) {
		           // optionally retry using exponential back-off
		           // (see Advanced Topics)
		        } else {
		            // Unrecoverable error, log it
		            Log.i("TAG", "Received error: " + error);
		        }
		    }
	}
}


