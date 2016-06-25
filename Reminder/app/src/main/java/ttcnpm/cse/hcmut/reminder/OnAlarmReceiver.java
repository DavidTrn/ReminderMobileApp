package ttcnpm.cse.hcmut.reminder;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class OnAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = ComponentInfo.class.getCanonicalName(); 

	@Override	
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received wake up from alarm manager.");

		long rowid = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);
		String rowcontent = intent.getExtras().getString(RemindersDbAdapter.KEY_TITLE);
		
		WakeReminderIntentService.acquireStaticLock(context);
		
		Intent i = new Intent(context, ReminderService.class); 
		i.putExtra(RemindersDbAdapter.KEY_ROWID, rowid);
		i.putExtra(RemindersDbAdapter.KEY_TITLE, rowcontent);
		context.startService(i);
		 
	}
}
