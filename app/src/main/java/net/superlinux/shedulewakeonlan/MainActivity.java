package net.superlinux.shedulewakeonlan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView network_addresses_textview;
    Button send_wake_on_lan_button,mac_address_list_editor_button;
    ListView mac_addresses_table_listview;
    ArrayList<DataModel> dataSet;
    MACDatabase database;
    int ACCESS_WIFI_STATE_PERMISSION_CODE=100;
    int ACCESS_NETWORK_STATE_PERMISSION_CODE=101;
    int INTERNET_PERMISSION_CODE=102;
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, permission+" Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,   String[] permissions,   int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == ACCESS_WIFI_STATE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "ACCESS_WIFI_STATE_PERMISSION Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "ACCESS_WIFI_STATE_PERMISSION Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        if (requestCode == ACCESS_NETWORK_STATE_PERMISSION_CODE) {
            if (grantResults.length > 0   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "ACCESS_NETWORK_STATE_PERMISSION Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "ACCESS_NETWORK_STATE_PERMISSION Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.length > 0   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "INTERNET_STATE_PERMISSION Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "INTERNET_STATE_PERMISSION Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(Manifest.permission.ACCESS_WIFI_STATE, ACCESS_WIFI_STATE_PERMISSION_CODE);
        checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, ACCESS_NETWORK_STATE_PERMISSION_CODE);
        checkPermission(Manifest.permission.INTERNET, INTERNET_PERMISSION_CODE);

      String this_device_IP=Network.getLocalIpAddress();
        String this_device_broadcastIP=Network.getBroadcast();
        String this_device_mac_address=Network.getMacAddress();
        network_addresses_textview=((TextView) (findViewById(R.id.network_addresses_textview)));
        network_addresses_textview.setText(Html.fromHtml("About This device..<br>IP="+this_device_IP+"<br>Broadcast IP="+this_device_broadcastIP+"<br>MAC Address="+Network.getMacAddress()));
        send_wake_on_lan_button=((Button) (findViewById(R.id.send_wake_on_lan_button)));
        send_wake_on_lan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // wakeOnLan("b4:2e:99:6f:ec:bd");
                if (Network.wakeOnLan("70:10:6f:ba:48:5e") ) {
                    Toast.makeText(MainActivity.this, R.string.wake_on_lan_packets_were_sent_successfully , Toast.LENGTH_SHORT).show();
                } else Toast.makeText(MainActivity.this, R.string.failed_to_send_wake_on_lan_packets, Toast.LENGTH_SHORT).show();

            }
        });
        mac_address_list_editor_button=((Button) (findViewById(R.id.go_to_mac_address_list_editor_button)));
        mac_address_list_editor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MacAddressesListEditor.class);
                startActivity(intent);
            }
        });
        mac_addresses_table_listview= findViewById(R.id.mac_addresses_table_listview);
        //Toast.makeText(this, IP, Toast.LENGTH_LONG).show();
        database = new MACDatabase(MainActivity.this);
        database.get_mac_addresses_table();
        dataSet= database.get_mac_addresses_table();
        if (!dataSet.isEmpty()) {
            CustomListViewAdapter adapter = new CustomListViewAdapter(dataSet, getApplicationContext(),false);

            mac_addresses_table_listview.setAdapter(adapter);
        }
       //View v= new View(set)
        mac_addresses_table_listview.addHeaderView(View.inflate(MainActivity.this, R.layout.listview_header_without_delete, null));
        mac_addresses_table_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               Log.e("position=",""+position);
                                 Toast.makeText(MainActivity.this,position,Toast.LENGTH_LONG).show();

            }
        });
    }
}