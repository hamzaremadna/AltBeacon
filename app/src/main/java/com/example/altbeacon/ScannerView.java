package com.example.altbeacon;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class ScannerView  extends Fragment implements BeaconConsumer {

    private final String TAG = "ScannerView";

    private ArrayList<Beacon> listItems = null;
    private MainListAdapter adapter = null;
    private ListView listView = null;
    private BeaconManager beaconManager;
    private BeaconParser beaconParser;
    private BeaconConsumer consumer;
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

        beaconManager = BeaconManager.getInstanceForApplication(this.getContext());

        beaconManager.getBeaconParsers().add(beaconParser);
        consumer = this;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scanner_tab, container, false);

        final FloatingActionButton toggleButton = (FloatingActionButton) v.findViewById(R.id.scannerToggle);
        final FloatingActionButton toggleButton2 = (FloatingActionButton) v.findViewById(R.id.stop);
        toggleButton2.hide();

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getContext(),"Your Bluetooth must be Enabled",Toast.LENGTH_SHORT).show();
                } else {
                if(isLocationEnabled()){
                    toggleButton2.show();
                    toggleButton.hide();
                    Toast.makeText(getContext(),"Scanner Start",Toast.LENGTH_SHORT).show();
                    beaconManager.bind(consumer);
          }else{
                    Toast.makeText(getContext(),"Your Location must be Enabled",Toast.LENGTH_SHORT).show();
                }}

            }


        });

        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton.show();
                toggleButton2.hide();
                Toast.makeText(getContext(),"Scanner Stop",Toast.LENGTH_SHORT).show();
                beaconManager.unbind(consumer);
            }
        });

        listView = (ListView) v.findViewById(R.id.list);
        listItems = new ArrayList<Beacon>();
        adapter = new MainListAdapter(getActivity(),listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Beacon itemValue = (Beacon) listView.getItemAtPosition(position);
            }

        });

        return v;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    adapter.copyBeacons(beacons);
                    listView.setAdapter(adapter);
                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getId1()+" meters away.");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }
    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
}
