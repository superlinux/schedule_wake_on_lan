package net.superlinux.shedulewakeonlan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MacAddressesListEditor extends AppCompatActivity {
ListView mac_addresses_listview;
EditText computer_name_edittext,mac_address_edittext;
Button add_new_mac_address_button;
//ScrollView mac_addresses_table_scrollview;
MACDatabase database;
ArrayList<DataModel> dataSet;
CustomListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_addresses_list_editor);
        computer_name_edittext=(EditText) findViewById(R.id.computer_name_edittext);
        mac_address_edittext= (EditText) findViewById(R.id.mac_address_edittext);
        add_new_mac_address_button=(Button) findViewById(R.id.add_new_mac_address);

       mac_addresses_listview=findViewById(R.id.mac_address_list_editor_listview);
       mac_addresses_listview.addHeaderView(View.inflate(MacAddressesListEditor.this, R.layout.listview_header, null));

        database=new MACDatabase(MacAddressesListEditor.this);
       dataSet= database.get_mac_addresses_table();
        if (!dataSet.isEmpty()) {
            adapter = new CustomListViewAdapter(dataSet, getApplicationContext(),true);
            mac_addresses_listview.setAdapter(adapter);
        }

        add_new_mac_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String computer_name = computer_name_edittext.getText().toString();
                String mac_address = mac_address_edittext.getText().toString();
                if (computer_name.length()==0 || mac_address.length()==0) {
                    Toast.makeText(MacAddressesListEditor.this,R.string.please_fill_both_the_computer_name_and_the_mac_address,Toast.LENGTH_LONG).show();
                    return;
                }
                    if (!mac_address.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
                        Toast.makeText(MacAddressesListEditor.this,R.string.the_input_is_not_a_mac_address,Toast.LENGTH_LONG).show();

                        return;
                    }
                    database.add_new_mac_address(computer_name, mac_address);
                    dataSet=database.get_mac_addresses_table();
                    if (!dataSet.isEmpty()) {
                        CustomListViewAdapter adapter = new CustomListViewAdapter(dataSet, getApplicationContext(),true);
                        mac_addresses_listview.setAdapter(adapter);
                    }

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MacAddressesListEditor.this, MainActivity.class);
        startActivity(intent);

        super.onBackPressed();
        finish();
    }
}