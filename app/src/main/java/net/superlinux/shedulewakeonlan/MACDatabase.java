package net.superlinux.shedulewakeonlan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MACDatabase {
    @SuppressLint("Range")
            SQLiteDatabase database;
            Context this_context;

    MACDatabase(Context mcontext){
        this_context=mcontext;

        //String database_table_creation_query="";
        try  {
            File database_file = new File(mcontext.getFilesDir().getPath()+File.pathSeparator+"mac_addresses.db");
            if (!database_file.exists()) {
                database_file.createNewFile();

                Toast.makeText(this_context,R.string.database_file_was_not_there_now_a_new_database_file_created,Toast.LENGTH_LONG).show();
                //Snackbar.make(this.getApplicationContext().get,R.string.database_file_was_not_there_now_a_new_database_file_created,Snackbar.LENGTH_LONG).show();
            }
            database=SQLiteDatabase.openOrCreateDatabase(database_file.getPath(),null);
          String  database_table_creation_query="create table if not exists mac_addresses_list (computer_name text, mac_address text); ";
            database.execSQL(database_table_creation_query);

            //Toast.makeText(this, Environment.getDataDirectory().toString(), Toast.LENGTH_SHORT).show();
//            FileOutputStream fOut = openFileOutput("samplefile.txt", MODE_PRIVATE);
//            fOut.close();
//            if ()
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
     @SuppressLint("Range")
     ArrayList<DataModel> get_mac_addresses_table() {
        String query_all_mac_adresses ="Select computer_name, mac_address from mac_addresses_list order by computer_name";

        ArrayList<DataModel> dataSet=new ArrayList<>();

        Cursor cursor = database.rawQuery(query_all_mac_adresses,null);

        String computer_name="",mac_address="";
        if (cursor.getCount()>0) {

            while(cursor.moveToNext()){
                computer_name=cursor.getString(cursor.getColumnIndex("computer_name"));
                mac_address=cursor.getString(cursor.getColumnIndex("mac_address"));
                dataSet.add(new DataModel(computer_name,mac_address));


            }
        }
        cursor.close();
        return dataSet;
    }
    void add_new_mac_address(String computer_name,String mac_address){
        String database_insert_new_mac_address_query="insert into mac_addresses_list (computer_name , mac_address ) values('"+computer_name+"','"+mac_address+"');";
        database.execSQL(database_insert_new_mac_address_query);
    }
    void delete_single_mac_address(String p_mac_address){
        String database_delete_single_mac_address_query="delete from mac_addresses_list where mac_address='"+p_mac_address+"'";
        database.execSQL(database_delete_single_mac_address_query);
    }

}
