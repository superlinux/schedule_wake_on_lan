package net.superlinux.shedulewakeonlan;

public class DataModel {
    String data_model_computer_name,data_model_mac_address;

    DataModel(String p_computer_name, String p_mac_address){
        data_model_computer_name=p_computer_name;
        data_model_mac_address=p_mac_address;
    }

    public String get_data_model_computer_name() {
        return data_model_computer_name;
    }

    public String get_data_model_mac_address() {
        return data_model_mac_address;
    }
}
