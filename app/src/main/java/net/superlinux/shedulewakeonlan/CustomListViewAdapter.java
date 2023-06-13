package net.superlinux.shedulewakeonlan;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<DataModel>  {
    ArrayList<DataModel> dataSet;
    int delete_button_visibility;
    Context mContext;
    public CustomListViewAdapter(ArrayList<DataModel> data, Context context, boolean isVisible_delete_button) {
        super(context, R.layout.mac_address_listview_row_adapter, data);
        this.dataSet = data;
        this.mContext=context;
        //Button b = new Button(null);
        if (isVisible_delete_button){
            this.delete_button_visibility=Button.VISIBLE;
        }
        else {
            this.delete_button_visibility = Button.INVISIBLE;
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataModel single_dataModel = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if ( this.delete_button_visibility==Button.VISIBLE) {
            convertView = inflater.inflate(R.layout.mac_address_listview_row_adapter, parent, false);
            Button delete_button_listview_row = (Button) convertView.findViewById(R.id.delete_button_listview_row);

            delete_button_listview_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MACDatabase db = new MACDatabase(getContext());
                    db.delete_single_mac_address(single_dataModel.get_data_model_mac_address());
                    //int positionToRemove = (int)view.getTag(); //get the position of the view to delete stored in the tag
                    dataSet.remove(position);//deletes a single row from listview. This dataSet is already an array of the rows of the listView
                    notifyDataSetChanged();//refresh or reload the list
                }
            });
        }
        else {
            convertView = inflater.inflate(R.layout.mac_address_listview_row_adapter_without_delete_button, parent, false);

        }
        TextView computer_name_CustomListViewAdapter_textview=(TextView) convertView.findViewById(R.id.computer_name_textview_listview_row);
                 computer_name_CustomListViewAdapter_textview.setText(single_dataModel.get_data_model_computer_name());
        TextView mac_address_CustomListViewAdapter_textview=(TextView) convertView.findViewById(R.id.mac_address_textview_listview_row);
                mac_address_CustomListViewAdapter_textview.setText(single_dataModel.get_data_model_mac_address());
        Button send_wake_on_lan_CustomListViewAdapter_button=(Button) convertView.findViewById(R.id.send_wake_on_lan_button_listview_row);
        send_wake_on_lan_CustomListViewAdapter_button.setText(R.string.wake_up);
        send_wake_on_lan_CustomListViewAdapter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MAC ADDRESS",single_dataModel.get_data_model_mac_address());
                Network.wakeOnLan(single_dataModel.get_data_model_mac_address());
            }
        });

        convertView.setTag(single_dataModel.get_data_model_mac_address());
        return convertView;
    }



}
