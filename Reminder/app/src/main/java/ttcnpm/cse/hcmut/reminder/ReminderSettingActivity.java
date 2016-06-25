
package ttcnpm.cse.hcmut.reminder;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ReminderSettingActivity extends Activity {

    private ImageView btnBack;

    private ToggleButton tgSound, tgVibrate, tgLED;
    private Button btnSoundPicker;
    private String soundPath;
    private RadioButton rbDay, rbAll;
    private RadioGroup display;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        res = this.getResources();

        btnBack = (ImageView) findViewById(R.id.btnBackSetting);
        tgSound = (ToggleButton) findViewById(R.id.tgSound);
        tgVibrate = (ToggleButton) findViewById(R.id.tgVibrate);
        tgLED = (ToggleButton) findViewById(R.id.tgLed);
        btnSoundPicker = (Button) findViewById(R.id.btnRing);
        rbDay = (RadioButton) findViewById(R.id.rbDay);
        rbAll = (RadioButton) findViewById(R.id.rbAll);
        display = (RadioGroup) findViewById(R.id.display);

        tgSound.setChecked(MainActivity.getSharedPreferences(Constant.SOUNDKEY).equals("1"));
        tgVibrate.setChecked(MainActivity.getSharedPreferences(Constant.VIBRATEKEY).equals("1"));
        tgLED.setChecked(MainActivity.getSharedPreferences(Constant.LEDKEY).equals("1"));
        soundPath = MainActivity.getSharedPreferences(Constant.SOUNDPATHKEY);

        if (MainActivity.getSharedPreferences(Constant.SHOWALL) != null) {
            if (MainActivity.getSharedPreferences(Constant.SHOWALL).equals("1")) {
                rbDay.setChecked(true);
                rbAll.setChecked(false);
            }
            else{
                rbDay.setChecked(false);
                rbAll.setChecked(true);
            }
        }
        else{
            rbDay.setChecked(true);
            rbAll.setChecked(false);
        }

        if (soundPath.isEmpty()) {
            soundPath = "content://settings/system/ringtone";
        }
        btnSoundPicker.setText(RingtoneManager.getRingtone(this, Uri.parse(soundPath)).getTitle(this));


        tgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.saveSharedPreferences(Constant.SOUNDKEY, tgSound.isChecked() ? "1" : "0");
            }
        });

        tgVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.saveSharedPreferences(Constant.VIBRATEKEY, tgVibrate.isChecked() ? "1" : "0");
            }
        });

        tgLED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.saveSharedPreferences(Constant.LEDKEY, tgLED.isChecked() ? "1" : "0");
            }
        });

        btnSoundPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.ToneSelectTitle);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(soundPath));
                startActivityForResult(intent, 5);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rbDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReminderSettingActivity.this, res.getString(R.string.toast_display_day), Toast.LENGTH_SHORT).show();
                MainActivity.saveSharedPreferences(Constant.SHOWALL, rbDay.isChecked()? "1":"0");
            }
        });

        rbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReminderSettingActivity.this, res.getString(R.string.toast_display_all), Toast.LENGTH_SHORT).show();
                MainActivity.saveSharedPreferences(Constant.SHOWALL, rbAll.isChecked()? "0":"1");
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {
                soundPath = uri.toString();

                btnSoundPicker.setText(RingtoneManager.getRingtone(this, Uri.parse(soundPath)).getTitle(this));

                MainActivity.saveSharedPreferences(Constant.SOUNDPATHKEY, soundPath);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
