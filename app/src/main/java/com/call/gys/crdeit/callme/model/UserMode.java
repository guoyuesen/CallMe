package com.call.gys.crdeit.callme.model;

import com.call.gys.crdeit.callme.base.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 郭月森 on 2018/10/22.
 */

public class UserMode extends BaseModel {
    //{"company_name":"123","company_id":1,"creat_time":null,"staff_phone":"13527586651","id":1,"state":1,"staff_manage":null,"staff_name":"郭月森"}
    public static String CompanyName="";
    public static String CompanyId="";
    public static long CreatTime=0;
    public static String Phone = "";
    public static int Id = 0;
    public static int State = 0;
    public static String StaffName="";
    public static String StaffManage="";
    public static void initMode(JSONObject object){
        try {
            CompanyName = object.getString("company_name")==null?"":object.getString("company_name");
            CompanyId = object.getString("company_id")==null?"":object.getString("company_id");
            CreatTime = object.getString("creat_time").equals("null")?0:object.getLong("creat_time");
            Phone = object.getString("staff_phone")==null?"":object.getString("staff_phone");
            Id = object.getString("id").equals("null")?0:object.getInt("id");
            State = object.getString("state").equals("null")?0:object.getInt("state");
            StaffName = object.getString("staff_name")==null?"":object.getString("staff_name");
            StaffManage = object.getString("staff_manage")==null?"":object.getString("staff_manage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
