package com.adunik.adunik_connectingdevices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private BluetoothAdapter BA;
    private BTClass btc;

    private Switch toggleBT_led, toggleBT_rc;
    private Switch tilt, buttons;
    private Button btnScan_led, btnScan_rc;
    private Button btnOn, btnOff;
    private Button btnForward, btnLeft, btnRight, btnReverse;
    private SeekBar seekRed, seekGreen, seekBlue;

    private int mode = 0;
    private char status = 'u';
    private String address;

    private Set<BluetoothDevice>pairedDevices;
    ListView lv;

    public int valueRed = 0;
    public int valueGreen = 0;
    public int valueBlue = 0;

    int dir;
    private Handler mHandler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            controlRC(dir);
            mHandler.postDelayed(run, 10);
        }
    };

    private SensorManager sensorMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("LED");
        tabSpec.setContent(R.id.tabLED);
        tabSpec.setIndicator("LED");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("RC Car");
        tabSpec.setContent(R.id.tabRC);
        tabSpec.setIndicator("RC Car");
        tabHost.addTab(tabSpec);

        initViews();
        disableAll();

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = null;
        btc = new BTClass();

        if(BA.isEnabled())
        {
            toggleBT_led.setChecked(true);
            toggleBT_rc.setChecked(true);
            btnScan_led.setEnabled(true);
            btnScan_rc.setEnabled(true);
        }
        else
        {
            toggleBT_led.setChecked(false);
            toggleBT_rc.setChecked(false);
            disableAll();
        }
    }

    public void initViews() //This method initializes all the Interface Views
    {
        toggleBT_led = (Switch) findViewById(R.id.toggleBT_led);
        toggleBT_rc = (Switch) findViewById(R.id.toggleBT_rc);
        tilt = (Switch) findViewById(R.id.tgAcc);
        buttons = (Switch) findViewById(R.id.tgButtons);
        btnScan_led = (Button) findViewById(R.id.btnScan_led);
        btnScan_rc = (Button) findViewById(R.id.btnScan_rc);
        btnOn = (Button) findViewById(R.id.btnLEDOn);
        btnOff = (Button) findViewById(R.id.btnLEDOff);

        btnForward = (Button) findViewById(R.id.btnForward);
        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dir = 0;
                        if(mHandler != null)
                            return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(run,10);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(mHandler == null)
                            return true;
                        mHandler.removeCallbacks(run);
                        mHandler = null;
                        break;
                }
                return true;
            }
        });

        btnReverse = (Button) findViewById(R.id.btnReverse);
        btnReverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dir = 1;
                        if (mHandler != null)
                            return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(run, 10);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null)
                            return true;
                        mHandler.removeCallbacks(run);
                        mHandler = null;
                        break;
                }
                return true;
            }
        });

        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dir = 2;
                        if (mHandler != null)
                            return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(run, 10);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null)
                            return true;
                        mHandler.removeCallbacks(run);
                        mHandler = null;
                        break;
                }
                return true;
            }
        });

        btnRight = (Button) findViewById(R.id.btnRight);
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dir = 3;
                        if (mHandler != null)
                            return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(run, 10);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null)
                            return true;
                        mHandler.removeCallbacks(run);
                        mHandler = null;
                        break;
                }
                return true;
            }
        });

        seekRed = (SeekBar) findViewById(R.id.seekRed);
        seekRed.setMax(255);
        seekRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueRed = progress;
                sendData(valueRed + "," + valueGreen + "," + valueBlue + "\n");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekGreen = (SeekBar) findViewById(R.id.seekGreen);
        seekGreen.setMax(255);
        seekGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueGreen = progress;
                sendData(valueRed + "," + valueGreen + "," + valueBlue + "\n");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBlue = (SeekBar) findViewById(R.id.seekBlue);
        seekBlue.setMax(255);
        seekBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueBlue = progress;
                sendData(valueRed+","+valueGreen+","+valueBlue+"\n");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    public void listDevices(View v) //This method will list the paired devices
    {
        if(lv != null)
            lv.setVisibility(View.INVISIBLE);

        if(v.getId() == R.id.btnScan_led)
            lv = (ListView) findViewById(R.id.listPaired_led);

        else
            lv = (ListView) findViewById(R.id.listPaired_rc);

        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices)
        {
            list.add(bt.getName() + "\n" + bt.getAddress());
            lv.setVisibility(View.VISIBLE);
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
                String info = ((TextView) v).getText().toString();
                address = info.substring(info.length() - 17);

                int status = btc.btConnect(address);
                if (status == 0) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Connected", Toast.LENGTH_SHORT).show();
                    enableAll();
                } else if (status == 1) {
                    Toast.makeText(getApplicationContext(), "Error: Bluetooth Not Connected", Toast.LENGTH_SHORT).show();
                    disableAll();
                    btnScan_led.setEnabled(true);
                    btnScan_rc.setEnabled(true);
                }

                lv.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void onSwitchBT_LED(View v)
    {
        if(toggleBT_led.isChecked())
        {
            btEnable();
            toggleBT_rc.setChecked(true);
            btnScan_led.setEnabled(true);
            btnScan_rc.setEnabled(true);
        }
        if(!toggleBT_led.isChecked())
        {
            BA.disable();
            toggleBT_rc.setChecked(false);
            disableAll();
        }
    }

    public void onSwitchBT_RC(View v)
    {
        if(toggleBT_rc.isChecked())
        {
            btEnable();
            toggleBT_led.setChecked(true);
            btnScan_led.setEnabled(true);
            btnScan_rc.setEnabled(true);
        }
        if(!toggleBT_rc.isChecked())
        {
            controlRC(4);
            BA.disable();
            toggleBT_led.setChecked(false);
            disableAll();
        }
    }

    public void ledON(View v)
    {
        if(valueRed > 0 || valueGreen > 0 || valueBlue > 0)
        {
            sendData(valueRed+","+valueGreen+","+valueBlue+"\n");
        }
        else
        {
            sendData("255,255,255\n");
        }
        Toast.makeText(getApplicationContext(), "LED On", Toast.LENGTH_SHORT).show();
    }

    public void ledOFF(View v)
    {
        sendData("0,0,0\n");
        Toast.makeText(getApplicationContext(), "LED Off", Toast.LENGTH_SHORT).show();
    }

    public void rcMode(View v) //This method switched between Tilt Control and Button Control for the RC Car
    {
        //Tilt: mode = 0
        //Buttons: mode = 1
        if(tilt.isChecked() && mode == 0)
        {
            mode = 1;
            buttons.setChecked(false);
            disableControls();

            sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
            sensorMgr.registerListener(MainActivity.this,sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
            status = 'r';
        }
        else if(!tilt.isChecked() && mode == 1)
        {
            if(status == 'r')
            {
                controlRC(4);
                sensorMgr.unregisterListener(MainActivity.this);
                status = 'u';
            }
        }
        else if((buttons.isChecked() && mode == 0) || (buttons.isChecked() && mode == 1))
        {
            mode = 0;
            tilt.setChecked(false);
            enableControls();
            if(status == 'r')
            {
                controlRC(4);
                sensorMgr.unregisterListener(MainActivity.this);
                status = 'u';
            }
        }
        else if(!buttons.isChecked() && mode == 0)
        {
            controlRC(4);
            disableControls();
        }
    }

    public void controlRC(int d) {
        sendData(d+"\n");
    }

    public void onDestroy() //This method is called when the application is closed
    {
        super.onDestroy();
        controlRC(4);
        BA.disable();
        if(status == 'r')
            sensorMgr.unregisterListener(MainActivity.this);
    }

    private void btEnable() //This method generates a pop-up asking for permission to enable bluetooth
    {
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);
    }

    private void enableAll()
    {
        btnOn.setEnabled(true);
        btnOff.setEnabled(true);
        seekRed.setEnabled(true);
        //seekGreen.setEnabled(true);
        //seekBlue.setEnabled(true);

        tilt.setEnabled(true);
        buttons.setEnabled(true);
    }

    private void disableAll()
    {
        btnScan_led.setEnabled(false);
        btnOn.setEnabled(false);
        btnOff.setEnabled(false);
        seekRed.setEnabled(false);
        //seekGreen.setEnabled(false);
        //seekBlue.setEnabled(false);

        btnScan_rc.setEnabled(false);
        tilt.setEnabled(false);
        buttons.setEnabled(false);
        disableControls();
    }

    private void enableControls()
    {
        btnForward.setEnabled(true);
        btnReverse.setEnabled(true);
        btnLeft.setEnabled(true);
        btnRight.setEnabled(true);
    }

    private void disableControls()
    {
        btnForward.setEnabled(false);
        btnReverse.setEnabled(false);
        btnLeft.setEnabled(false);
        btnRight.setEnabled(false);
    }

    public void sendData(String msg)
    {
        btc.getData(msg);
    }

    public void onSensorChanged(SensorEvent event)
    {
        float x = event.values[0];
        float y = event.values[1];

        if(x > 0.0)
            controlRC(1);
        else if(x < 0.0)
            controlRC(0);
    }
    public void onAccuracyChanged(Sensor sensor, int acc) {}
}