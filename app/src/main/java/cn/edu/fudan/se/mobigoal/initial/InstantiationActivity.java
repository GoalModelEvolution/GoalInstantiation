package cn.edu.fudan.se.mobigoal.initial;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.fudan.se.R;
import cn.edu.fudan.se.mobigoal.utils.accessjersey.JerseyClient;

public class InstantiationActivity extends Activity {

    private Spinner goalmodel_spinner;
    private Spinner template_spinner;
    private ArrayAdapter<CharSequence> goalmodel_adapter;
    private ArrayAdapter<CharSequence> template_adapter;

    public final int GOAL_MODEL_LIST_RESULT = 0;
    public final int TEMPLATE_LIST_RESULT = 1;

    private final android.os.Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == GOAL_MODEL_LIST_RESULT) {

                String goalModelList = (String) msg.obj;
                String[] arr = goalModelList.split(",");
                goalmodel_adapter = new ArrayAdapter<CharSequence>(InstantiationActivity.this, android.R.layout.simple_spinner_item, arr);
                goalmodel_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                goalmodel_spinner.setAdapter(goalmodel_adapter);
            }else if(msg.what == TEMPLATE_LIST_RESULT){
                String templateList = (String) msg.obj;
                String[] arr = templateList.split(",");
                template_adapter = new ArrayAdapter<CharSequence>(InstantiationActivity.this, android.R.layout.simple_spinner_item, arr);
                template_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                template_spinner.setAdapter(template_adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instantiation);

        goalmodel_spinner = (Spinner) findViewById(R.id.goalmodel_spinner);
        template_spinner = (Spinner) findViewById(R.id.template_spinner);

        if (!checkNetworkAvailable()) { // if the network doesn't work, then disable related button.
            goalmodel_spinner.setEnabled(false);

        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String goalModelList = JerseyClient.getGoalModelList();
                    Message msg = Message.obtain();
                    msg.obj = goalModelList;
                    msg.what = GOAL_MODEL_LIST_RESULT;
                    handler.sendMessage(msg);
                }
            }).start();
        }
        template_spinner.setEnabled(false);
        goalmodel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getSelectedItem().toString();
                template_spinner.setEnabled(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String templateList = JerseyClient.getTemplateList(selectedItem);
                        Message msg = Message.obtain();
                        msg.obj = templateList;
                        msg.what = TEMPLATE_LIST_RESULT;
                        handler.sendMessage(msg);
                    }
                }).start();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public boolean checkNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable())
            return false;
        else return true;
    }

}
