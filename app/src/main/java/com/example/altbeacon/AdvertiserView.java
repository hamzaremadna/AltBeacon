package com.example.altbeacon;

import androidx.fragment.app.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

public class AdvertiserView extends Fragment {

    private Beacon beacon;
    private BeaconTransmitter beaconTransmitter;
    private BeaconParser beaconParser;
private BluetoothAdapter mBluetoothAdapter;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
   private EditText id1Text;
    private EditText id2Text;
    private EditText id3Text;
    private EditText id4Text;
    private Pattern k;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       k = Pattern.compile("\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
         beaconTransmitter = new BeaconTransmitter(this.getContext(), beaconParser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.advertise_tab, container, false);

        id1Text = (EditText) v.findViewById(R.id.id1box);
        id2Text = (EditText) v.findViewById(R.id.id2box);
        id3Text = (EditText) v.findViewById(R.id.id3box);
        id4Text = (EditText) v.findViewById(R.id.id4box);
        textView1 = (TextView) v.findViewById(R.id.textView3);
        textView2 = (TextView) v.findViewById(R.id.textView4);
        textView3 = (TextView) v.findViewById(R.id.textView5);
        textView4 = (TextView) v.findViewById(R.id.textView6);
        id1Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!k.matcher(id1Text.getText().toString()).matches()){
                textView4.setText("Hex characters and lenght of 32 (36 with hyphens)");}
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(k.matcher(id1Text.getText().toString()).matches()){
                    textView4.setText("");}
            }
        });
         id2Text.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                     textView1.setText("Between 0 and 65 535");

             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });
        id3Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textView2.setText("Between 0 and 65 535");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        id4Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textView3.setText("Between -128 and 127");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final Button button  = (Button) v.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String a = String.valueOf(UUID.randomUUID());
                id1Text.setText(a);

            }
        });
        final FloatingActionButton toggleButton2 = (FloatingActionButton) v.findViewById(R.id.advertToggle2);
        toggleButton2.hide();
        final FloatingActionButton toggleButton = (FloatingActionButton) v.findViewById(R.id.advertToggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getContext(),"You have to activate your bluetooth",Toast.LENGTH_SHORT).show();
                } else {
                    if(!k.matcher(id1Text.getText().toString()).matches()){
                        Toast.makeText(getContext(),"Put a valid UUID or Generate one",Toast.LENGTH_SHORT).show();
                    }else{
               if(id2Text.getText().toString().trim().length() == 0 || Integer.parseInt(id2Text.getText().toString()) < 0 || Integer.parseInt(id2Text.getText().toString()) > 65535)
               {
                   Toast.makeText(getContext(),"Put a valid Major value",Toast.LENGTH_SHORT).show();
               }else{
                    if(id3Text.getText().toString().trim().length() == 0 || Integer.parseInt(id3Text.getText().toString()) < 0 || Integer.parseInt(id3Text.getText().toString()) > 65535 )
                    {
                        Toast.makeText(getContext(),"Put a valid Minor Value",Toast.LENGTH_SHORT).show();
                    }else{
                    if(id4Text.getText().toString().trim().length() == 0 || Integer.parseInt(id4Text.getText().toString()) < -128 || Integer.parseInt(id4Text.getText().toString()) > 127)
                    {
                        Toast.makeText(getContext(),"Put a valid Tx Power Value",Toast.LENGTH_SHORT).show();
                    }else{

                    Toast.makeText(getContext(),"Advertiser Began",Toast.LENGTH_SHORT).show();
                    toggleButton2.show();
                    toggleButton.hide();
                     beacon = new Beacon.Builder()
                             .setId1(id1Text.getText().toString())
                             .setId2(id2Text.getText().toString())
                             .setId3(id3Text.getText().toString())
                             .setRssi(Integer.parseInt(id4Text.getText().toString()))
                             .setManufacturer(MainActivity.BEACON_MANUFACTURER)
                            .setTxPower(-69)
                            .setDataFields(Arrays.asList(new Long[] {0l}))
                            .build();
                    beaconTransmitter.startAdvertising(beacon);
                    textView1.setText("");
                        textView2.setText("");
                        textView3.setText("");
                        textView4.setText("");
                    id1Text.setEnabled(false);
                    id2Text.setEnabled(false);
                    id3Text.setEnabled(false);
                    id4Text.setEnabled(false);}}}}}
                     }
        });
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Advertiser Stopped",Toast.LENGTH_SHORT).show();
                beaconTransmitter.stopAdvertising();
                    toggleButton.show();
                    toggleButton2.hide();
                    id1Text.setEnabled(true);
                    id2Text.setEnabled(true);
                    id3Text.setEnabled(true);
                    id4Text.setEnabled(true);



            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        id1Text.setText("a3083e3d-bd69-4703-9da3-3924c2ab07e2");
        id2Text.setText("1");
        id3Text.setText("2");
        id4Text.setText("-69");
        textView1.setText("");
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(beaconTransmitter.isStarted()){
        Toast.makeText(getContext(),"Advertiser Stopped",Toast.LENGTH_SHORT).show();
        beaconTransmitter.stopAdvertising();}


        textView1.setText("");
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");

    }

}


