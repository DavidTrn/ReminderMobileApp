package ttcnpm.cse.hcmut.reminder;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ReminderManager {

	private Context mContext; 
	private AlarmManager mAlarmManager;
	
	public ReminderManager(Context context) {
		mContext = context; 
		mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
	
	public void setReminder(Long taskId, String title, Calendar when) {

        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId);
		Log.d("SET", taskId + "");
		i.putExtra(RemindersDbAdapter.KEY_TITLE, title);

		//PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, (int)(long)taskId, i, PendingIntent.FLAG_ONE_SHOT);







		//mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
        
        //mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), 10000, pi);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
		} else {
			mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
		}
		
	}

	public void cancelReminder(Long taskId, String title){
		Intent i = new Intent(mContext, OnAlarmReceiver.class);
		i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId);
		i.putExtra(RemindersDbAdapter.KEY_TITLE, title);


		PendingIntent pi = PendingIntent.getBroadcast(mContext, (int)(long) taskId, i, PendingIntent.FLAG_ONE_SHOT);
		//PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
		mAlarmManager.cancel(pi);


	}

//	public void cancelReminder(Long taskId){
//		Intent i = new Intent(mContext, OnAlarmReceiver.class);
//		i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId);

//
//		PendingIntent pi = PendingIntent.getBroadcast(mContext, (int)(long)taskId, i, PendingIntent.FLAG_ONE_SHOT);
//		//PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
//		mAlarmManager.cancel(pi);
//	}










}
