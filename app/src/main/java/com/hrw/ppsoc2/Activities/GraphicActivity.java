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
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.hrw.ppsoc2.Fragments.BarChartFragment;
import com.hrw.ppsoc2.Fragments.LineChartFragment;
import com.hrw.ppsoc2.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


public class GraphicActivity extends ActionBarActivity implements LineChartFragment.OnFragmentInteractionListener, BarChartFragment.OnFragmentInteractionListener{

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

    private InputStream inputStream;
    private Handler handler;
    private HandlerThread handlerThread;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private ProgressDialog progressDialog;

    private String TAG = "GraphicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);

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

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);

        }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment;
            switch (position){
                case 0:
                    fragment = LineChartFragment.newInstance("","");
                    return fragment;
                case 1:
                    fragment = BarChartFragment.newInstance("","");
                    return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
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
                if (device.getName().equals("spp")) {
                    bluetoothDevice = device;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            openBT();
                        }
                    });
                    break;
                }
            }
        }
    }

    private void openBT() {
//        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard
        // SerialPortService
        // ID
        UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
        try {
            Log.w(TAG, "Trying to connect with standard method");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            if (!bluetoothSocket.isConnected()) {
                bluetoothSocket.connect();
                Log.w(TAG, "Device connected with standard method");
                inputStream = bluetoothSocket.getInputStream();
                progressDialog.cancel();
            }
        } catch (IOException e) {
            Log.e(TAG,e.toString());
        }
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

}
