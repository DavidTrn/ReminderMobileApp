package ttcnpm.cse.hcmut.reminder;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class ReminderService extends WakeReminderIntentService {

	public NotificationManager mgr;

	public ReminderService() {
		super("ReminderService");
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	void doReminderWork(Intent intent) {
		Log.d("ReminderService", "Doing work.");
		Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);
		String content = intent.getExtras().getString(RemindersDbAdapter.KEY_TITLE);
		 
		mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		//Intent notificationIntent = new Intent(this, MainActivity.class);
		Intent notificationIntent = new Intent(this, ReminderEditActivity.class);
		notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);
		
		//PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(), 0);

		boolean isSoundOn = MainActivity.getSharedPreferences(this, Constant.SOUNDKEY).equals("1");
		boolean isVibrateOn = MainActivity.getSharedPreferences(this, Constant.VIBRATEKEY).equals("1");
		boolean isLedOn = MainActivity.getSharedPreferences(this, Constant.LEDKEY).equals("1");

		String soundPath = MainActivity.getSharedPreferences(this, Constant.SOUNDPATHKEY);
		Uri ringTone = null;
		if(!isSoundOn)
			ringTone = null;
		else if(soundPath.equals(""))
			ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		else
			ringTone = Uri.parse(soundPath);

		Notification note=new Notification.Builder(this)
								.setTicker(getString(R.string.notify_new_task_message))
								.setSmallIcon(android.R.drawable.stat_sys_warning)
								.setWhen(System.currentTimeMillis())
								.setContentTitle(getString(R.string.notify_new_task_title))
								.setContentText(content)
								.setContentIntent(pi)
								.setAutoCancel(true)
								.setSound(ringTone)
								.build();


		if(isVibrateOn)
			note.defaults |= Notification.DEFAULT_VIBRATE;
		if(isLedOn) {
			note.flags |= Notification.FLAG_AUTO_CANCEL;
			note.flags |= Notification.FLAG_SHOW_LIGHTS;
			note.ledARGB = Color.RED;
			note.ledOnMS = 1000;
			note.ledOffMS = 1000;
		}
		
		// An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value). 
		// I highly doubt this will ever happen. But is good to note. 
		int id = (int)((long)rowId);
		Log.d("NOTI", id + "");
		mgr.notify(id, note);

	}
}
