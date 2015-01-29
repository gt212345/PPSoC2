package com.hrw.ppsoc2.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hrw.ppsoc2.Fragments.BarChartFragment;
import com.hrw.ppsoc2.Fragments.LineChartFragment;
import com.hrw.ppsoc2.Fragments.PieChartFragment;
import com.hrw.ppsoc2.Interface.ConnectListener;
import com.hrw.ppsoc2.Interface.DataListener;
import com.hrw.ppsoc2.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


public class GraphicActivity extends ActionBarActivity implements LineChartFragment.OnFragmentInteractionListener,
        BarChartFragment.OnFragmentInteractionListener, PieChartFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * Call back function for drawing charts
     */
    private ConnectListener connectListenerLin;
    private ConnectListener connectListenerPie;
    private ConnectListener connectListenerBar;

    private InputStream inputStream;
    private Handler handler;
    private HandlerThread handlerThread;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private ProgressDialog progressDialog;

    private DataListener dataListenerLin;
    private DataListener dataListenerLin2;
    private DataListener dataListenerLin3;
    private DataListener dataListenerPie;
    private DataListener dataListenerBar;

    private ArrayList<Integer> weight;

    private byte[] input;

    private String TAG = "GraphicActivity";

    public void callAfterConnected(ConnectListener connectListener) {
        connectListener.doAfterConnected();
    }

    private void callAfterDataReceived(DataListener dataListener,byte[] input,ArrayList<Integer> data,int position) {
        dataListener.doAfterDataReceived(input,data,position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);

        weight = new ArrayList<>();
        weight.add(0);
        weight.add(0);
        weight.add(0);
        weight.add(0);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handlerThread = new HandlerThread("ConnectBT");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        progressDialog = ProgressDialog.show(this,"Please wait","Connecting",true);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(bluetoothSocket == null){
                    findBT();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graphic, menu);
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
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            switch (position) {
                case 0:
                    dataListenerLin = (LineChartFragment)object;
                    connectListenerLin = (LineChartFragment)object;
                    break;
                case 1:
                    dataListenerPie = (PieChartFragment)object;
                    connectListenerPie = (PieChartFragment)object;
                    break;
                case 2:
                    dataListenerBar = (BarChartFragment)object;
                    connectListenerBar = (BarChartFragment)object;
                    break;
                case 3:
                    dataListenerLin2 = (BarChartFragment)object;
                    break;
                case 4:
                    dataListenerLin3 = (BarChartFragment)object;
                    break;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            switch (position) {
                case 0:
                    dataListenerLin = null;
                    break;
                case 1:
                    dataListenerPie = null;
                    break;
                case 2:
                    dataListenerBar = null;
                    break;
                case 3:
                    dataListenerLin2 = null;
                    break;
                case 4:
                    dataListenerLin3 = null;
                    break;
            }
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position){
                case 0:
                    fragment = LineChartFragment.newInstance("","");
                    dataListenerLin = (LineChartFragment)fragment;
                    connectListenerLin = (LineChartFragment)fragment;
                    return fragment;
                case 1:
                    fragment = PieChartFragment.newInstance("","");
                    dataListenerPie = (PieChartFragment)fragment;
                    connectListenerPie = (PieChartFragment)fragment;
                    return fragment;
                case 2:
                    fragment = BarChartFragment.newInstance("","");
                    dataListenerBar = (BarChartFragment)fragment;
                    connectListenerBar = (BarChartFragment)fragment;
                    return fragment;
                case 3:
                    fragment = LineChartFragment.newInstance("","");
                    dataListenerLin2 = (LineChartFragment)fragment;
//                    connectListenerLin = (LineChartFragment)fragment;
                    return fragment;
                case 4:
                    fragment = LineChartFragment.newInstance("","");
                    dataListenerLin3 = (LineChartFragment)fragment;
//                    connectListenerLin = (LineChartFragment)fragment;
                    return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public ActionBar getActionBars(){
        ActionBar actionBar = getSupportActionBar();
        return actionBar;
    }


    private void findBT() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter
                .getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("Spp")) {
                    bluetoothDevice = device;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            openBT();
                        }
                    });
                    break;
                } else {
                    progressDialog.cancel();
                    Toast.makeText(this,"Connect attempt failed",Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "no spp match");
                }
            }
        } else {
            progressDialog.cancel();
            Toast.makeText(this,"Connect attempt failed",Toast.LENGTH_SHORT).show();
            Log.w(TAG, "no paired device");
        }
    }

    private void openBT() {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard
        // SerialPortService
        // ID
//        UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
        try {
            Log.w(TAG, "Trying to connect with standard method");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            if (!bluetoothSocket.isConnected()) {
                bluetoothSocket.connect();
                Log.w(TAG, "Device connected with standard method");
                inputStream = bluetoothSocket.getInputStream();
                progressDialog.cancel();
                Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show();
                receiveData();
            }
        } catch (IOException e) {
            progressDialog.cancel();
            Toast.makeText(this,"Connect attempt failed",Toast.LENGTH_SHORT).show();
            Log.w(TAG, e.toString());
        }
    }

    private void receiveData() throws IOException {
        Log.w(TAG,"receiveData called");
        while (true) {
            if (inputStream.available() >= 15) {
                input = new byte[15];
                inputStream.read(input);
                if (input[0] == -86/* && input[1] == -86*/) {
                    if(input[4] == 0){
                        weight.set(0,weight.get(0)+1);
                    } else if(input[4] == 1){
                        weight.set(1,weight.get(1)+1);
                    } else if(input[4] == 2){
                        weight.set(2,weight.get(2)+1);
                    } else if (input[4] == 3) {
                        weight.set(3,weight.get(3)+1);
                    }
                    if(dataListenerLin != null) {
                        callAfterDataReceived(dataListenerLin, input, weight,1);
                    }
                    if(dataListenerLin2 != null) {
                        callAfterDataReceived(dataListenerLin2, input, weight,0);
                    }
                    if(dataListenerLin3 != null) {
                        callAfterDataReceived(dataListenerLin3, input, weight,0);
                    }
                    if(dataListenerPie != null) {
                        callAfterDataReceived(dataListenerPie,input, weight,2);
                    }
                    if(dataListenerBar != null) {
                        callAfterDataReceived(dataListenerBar,input, weight,3);
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
