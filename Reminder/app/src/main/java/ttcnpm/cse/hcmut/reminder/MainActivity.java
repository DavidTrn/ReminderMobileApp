package ttcnpm.cse.hcmut.reminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    public static Context context;

    private ListView lvDrawer;
    private ImageView  ShowCalendar, preDate, nextDate, floating_btn;
    private TextView CalendarBox, tvEmpty;
    private ArrayList<Function> ds = null;
    private MyAdapter adapter = null;
    private DrawerLayout mDrawerLayout;
    private LinearLayout leftDrawer;

    private int year;
    private int month;
    private int day;

    Resources res;

    static final int DATE_DIALOG_ID = 998;
    static final int TIME_DIALOG_ID = 999;

    private int hour;
    private int minute;

    public final Date dt = new Date();

    private RemindersDbAdapter mDbHelper;
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_SETTING=2;
    private final Calendar c = Calendar.getInstance();

    private NotiDateAdapter reminders = null;

    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        res = this.getResources();

        dt.setTime(System.currentTimeMillis());

        Toast.makeText(getApplicationContext(), res.getString(R.string.first_guide), Toast.LENGTH_LONG).show();

        ShowCalendar =(ImageView) findViewById(R.id.imgCalendar);
        lvDrawer = (ListView) findViewById(R.id.lv_drawer);
        lv = (ListView) findViewById(R.id.listView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        preDate = (ImageView) findViewById(R.id.imgPre);
        nextDate = (ImageView) findViewById(R.id.imgNext);
        CalendarBox = (TextView) findViewById(R.id.tvMainDate);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
        floating_btn = (ImageView) findViewById(R.id.floating_btn);

        mDbHelper = new RemindersDbAdapter(this);
        mDbHelper.open();

        if (MainActivity.getSharedPreferences(this, Constant.SHOWALL) != null)
            MainActivity.saveSharedPreferences(this, Constant.SHOWALL, MainActivity.getSharedPreferences(this, Constant.SHOWALL));
        else{
            MainActivity.saveSharedPreferences(this, Constant.SHOWALL,"1");
        }

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        if (MainActivity.getSharedPreferences(this, Constant.SHOWALL).equals("1")){
            try {
                CalendarBox.setVisibility(View.VISIBLE);
                fillData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            CalendarBox.setVisibility(View.INVISIBLE);
            fillAllData();
        }


        ds = new ArrayList<>();
        Function create = new Function();
        Function about = new Function();
        Function setting = new Function();

        create.opt = 0;
        create.nameOfOption = res.getString(R.string.create);
        ds.add(create);

        about.opt = 1;
        about.nameOfOption = res.getString(R.string.about);
        ds.add(about);

        setting.opt = 2;
        setting.nameOfOption = res.getString(R.string.setting);
        ds.add(setting);

        adapter = new MyAdapter(this, R.layout.drawer_list_item, ds);
        lvDrawer.setAdapter(adapter);

        showdateCalendar();

        lvDrawer.setOnItemClickListener(new DrawerItemClickListener());

        // Show calendar when tap calender image
        ShowCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MainActivity.getSharedPreferences(Constant.SHOWALL).equals("1")) {
                    showDialog(DATE_DIALOG_ID);
                }
                else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(res.getString(R.string.title_warning))
                            .setMessage(res.getString(R.string.warning_mode_all))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    closeContextMenu();
                                }
                            }).show();
                }

            }

        });

        preDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.getSharedPreferences(Constant.SHOWALL).equals("1")) {
                    c.add(Calendar.HOUR, -24);

                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    // set current date into textview
                    Log.d("DDDD", day + " " + month + " " + year + " " + dt.getDate() + " " +dt.getMonth() + " " + dt.getYear());
                    if (day == dt.getDate() && month == dt.getMonth() && year == (dt.getYear()+1900)){
                        CalendarBox.setText(res.getString(R.string.today));
                    }
                    else {
                        CalendarBox.setText(new StringBuilder()
                                // Month is 0 based, just add 1
                                .append(MONTH[month]).append(" ").append(day));
                    }
                    try {
                        CalendarBox.setVisibility(View.VISIBLE);
                        fillData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(res.getString(R.string.title_warning))
                            .setMessage(res.getString(R.string.warning_mode_all))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    closeContextMenu();
                                }
                            }).show();
                }
            }
        });

        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.getSharedPreferences(Constant.SHOWALL).equals("1")) {
                    c.add(Calendar.HOUR, 24);
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    // set current date into textview
                    if (day == dt.getDate() && month == dt.getMonth() && year == (dt.getYear()+1900)){
                        CalendarBox.setText(res.getString(R.string.today));
                    }
                    else {
                        CalendarBox.setText(new StringBuilder()
                                // Month is 0 based, just add 1
                                .append(MONTH[month]).append(" ").append(day));
                    }

                    try {
                        CalendarBox.setVisibility(View.VISIBLE);
                        fillData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(res.getString(R.string.title_warning))
                            .setMessage(res.getString(R.string.warning_mode_all))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    closeContextMenu();
                                }
                            }).show();
                }

            }
        });

        floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReminder();
            }
        });

        registerForContextMenu(lv);

    }

    private void createReminder() {
        Intent i = new Intent(getApplicationContext(), ReminderEditActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void fillAllData(){
        final Cursor remindersCursor = mDbHelper.fetchAllReminders();
        startManagingCursor(remindersCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{RemindersDbAdapter.KEY_TITLE, RemindersDbAdapter.KEY_DATE_TIME};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.msg_tv, R.id.time_tv};

        // Now create a simple cursor adapter and set it to display
        final SimpleCursorAdapter reminders =
                new SimpleCursorAdapter(this, R.layout.row, remindersCursor, from, to);

        if (remindersCursor.getCount() == 0) tvEmpty.setVisibility(View.VISIBLE);
        else
            tvEmpty.setVisibility(View.INVISIBLE);

        lv.setAdapter(reminders);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ReminderEditActivity.class);
                i.putExtra(RemindersDbAdapter.KEY_ROWID, id);
                startActivityForResult(i, ACTIVITY_EDIT);
            }
        });

//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                mDbHelper.deleteReminder(id);
//                Log.d("ID", id +"");
//                //new ReminderManager(MainActivity.this).cancelReminder(id, remindersCursor.getString(1));
//                String Title = "";
//                remindersCursor.moveToFirst();
//                while(!remindersCursor.isAfterLast()){
//                    String ident = remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_ROWID));
//
//                    Log.d("IDENT", ident);
//
//                    if(id == Integer.parseInt(ident))
//                    {
//                        Title = remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_TITLE));
//                        break;
//                    }
//                    remindersCursor.moveToNext();
//                }
//                new ReminderManager(MainActivity.this).cancelReminder(id, Title);
//
//                fillAllData();
//                return true;
//            }
//        });
    }

    private  void fillData() throws ParseException {
        String date = year + "-" + (month+1) + "-" + day;
        Log.d("TAG", date);
        final ArrayList remindersCursor = mDbHelper.getDataByDay(date);

        // Now create a simple cursor adapter and set it to display
        reminders = new NotiDateAdapter(this, R.layout.row, remindersCursor);

        lv.setAdapter(reminders);

        if (remindersCursor.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
        else
            tvEmpty.setVisibility(View.INVISIBLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ReminderEditActivity.class);
                i.putExtra(RemindersDbAdapter.KEY_ROWID, ((Container)(remindersCursor.get((int) id))).getID());
                startActivityForResult(i, ACTIVITY_EDIT);
            }
        });

//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                new ReminderManager(MainActivity.this).cancelReminder(((Container) (remindersCursor.get((int) id))).getID(), ((Container) (remindersCursor.get((int) id))).getTitle());
//
//                mDbHelper.deleteReminder(((Container) (remindersCursor.get((int) id))).getID());
//
//                try {
//                    fillData();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
////                new ReminderManager(MainActivity.this).cancelReminder(((Container) (remindersCursor.get((int) id))).getID());
//                new ReminderManager(MainActivity.this).cancelReminder(((Container) (remindersCursor.get((int) id))).getID(), ((Container) (remindersCursor.get((int) id))).getTitle());
//
//                return true;
//            }
//        });
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void showdateCalendar() {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        // set current date into textview
        if (day == dt.getDate() && month == dt.getMonth() && year == (dt.getYear()+1900)){
            CalendarBox.setText(res.getString(R.string.today));
        }
        else {
            CalendarBox.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(MONTH[month]).append(" ").append(day));
        }


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            // set current date into textview
            if (day == dt.getDate() && month == dt.getMonth() && year == (dt.getYear()+1900)){
                CalendarBox.setText(res.getString(R.string.today));
            }
            else {
                CalendarBox.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(MONTH[month]).append(" ").append(day));
            }

            // set selected date into textview
            c.set(year, month, day);

            try {
                CalendarBox.setVisibility(View.VISIBLE);
                fillData();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;


        }
    };

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if (position == 0){
            createReminder();
        }
        else if (position == 1){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Reminder")
                    .setMessage("* Version: 1.0\n* Author: Tran Quoc Dai - Mach Chi Da - Tran Quang Duy - Le Cong Doan - Nguyen Le Duy - Ho Chi Minh University of Technology\n* This application is developed by team with nonprofit purposes")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            closeContextMenu();
                        }
                    }).show();
        }
        else {
            goSetting();
        }
        mDrawerLayout.closeDrawer(leftDrawer);
    }

    public void goSetting(){
        Intent i = new Intent(this, ReminderSettingActivity.class);
        startActivityForResult(i, ACTIVITY_SETTING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (MainActivity.getSharedPreferences(Constant.SHOWALL).equals("1")){
            try {
                CalendarBox.setVisibility(View.VISIBLE);
                fillData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            CalendarBox.setVisibility(View.INVISIBLE);
            fillAllData();
        }
    }

	public static void saveSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editer = sharedPreferences.edit();
        editer.putString(key, value);
        editer.commit();
    }

	public static void saveSharedPreferences(Context c, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(c);
        SharedPreferences.Editor editer = sharedPreferences.edit();
        editer.putString(key, value);
        editer.commit();
    }

    public static String getSharedPreferences(String key) {
        String suid = null;
        SharedPreferences sharedPreferences = null;
        if (context != null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            suid = sharedPreferences.getString(key, "");
        }
        else{
            suid = "1";
        }

        return suid;
    }

	public static String getSharedPreferences(Context c, String key) {
        String suid = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        suid = sharedPreferences.getString(key, "");

        return suid;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.list_menu_item_longpress, menu);
        menu.setHeaderTitle(res.getString(R.string.action_menu));

    }

    public void deleteAll(){
        final Cursor remindersCursor = mDbHelper.fetchAllReminders();
        startManagingCursor(remindersCursor);

        remindersCursor.moveToFirst();
        while(!remindersCursor.isAfterLast()){
            String ident = remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_ROWID));

            String Title = remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_TITLE));

            new ReminderManager(MainActivity.this).cancelReminder(Long.parseLong(ident), Title);

            remindersCursor.moveToNext();
        }

        mDbHelper.DeleteAll();

        fillAllData();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                if (MainActivity.getSharedPreferences(this, Constant.SHOWALL).equals("1")) {
                    String date = year + "-" + (month + 1) + "-" + day;
                    final ArrayList remindersCursor;
                    try {
                        remindersCursor = mDbHelper.getDataByDay(date);

                        mDbHelper.deleteReminder(((Container) (remindersCursor.get((int) info.id))).getID());

                        new ReminderManager(MainActivity.this).cancelReminder(((Container) (remindersCursor.get((int) info.id))).getID(), ((Container) (remindersCursor.get((int) info.id))).getTitle());

                        try {
                            fillData();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (MainActivity.getSharedPreferences(Constant.SHOWALL).equals("0")){
                    final Cursor remindersCursor = mDbHelper.fetchAllReminders();
                    startManagingCursor(remindersCursor);
                    mDbHelper.deleteReminder(info.id);
                    String Title = "";
                    remindersCursor.moveToFirst();
                    while(!remindersCursor.isAfterLast()){
                        String ident = remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_ROWID));

                        Log.d("IDENT", ident);

                        if(info.id == Integer.parseInt(ident))
                        {
                            Title = remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_TITLE));
                            break;
                        }
                        remindersCursor.moveToNext();
                    }
                    new ReminderManager(MainActivity.this).cancelReminder(info.id, Title);

                    fillAllData();
                    return true;
                }

            case R.id.menu_delete_all:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(res.getString(R.string.confirm_title))
                        .setMessage(res.getString(R.string.confirm_body))
                        .setPositiveButton(res.getString(R.string.btn_confirm_yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteAll();
                                closeContextMenu();
                            }
                        })
                        .setNegativeButton(res.getString(R.string.osbtn_confirm_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeContextMenu();
                            }
                        }).show();
                return true;


        }
        return super.onContextItemSelected(item);

    }

}
