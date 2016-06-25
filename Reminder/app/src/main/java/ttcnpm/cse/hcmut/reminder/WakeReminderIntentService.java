package ttcnpm.cse.hcmut.reminder;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public abstract class WakeReminderIntentService extends IntentService {
	abstract void doReminderWork(Intent intent);
	
	public static final String LOCK_NAME_STATIC="ttcnpm.cse.hcmut.reminder.Static";
	private static PowerManager.WakeLock lockStatic=null;
	
	public static void acquireStaticLock(Context context) {
		getLock(context).acquire();
	}
	
	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic==null) {
			PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
			lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}
		return(lockStatic);
	}
	
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public WakeReminderIntentService(String name) {
		super(name);
	}
	
	@Override
	final protected void onHandleIntent(Intent intent) {
		try {
			doReminderWork(intent);
		}
		finally {
			getLock(this).release();
		}
	}
}