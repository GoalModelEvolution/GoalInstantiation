package cn.edu.fudan.se.mobigoal.initial;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import cn.edu.fudan.se.R;
import cn.edu.fudan.se.mobigoal.utils.accessjersey.JerseyClient;

public class InstantiationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instantiation);

        Spinner goalmodel_spinner = (Spinner)findViewById(R.id.goalmodel_spinner);
        final ArrayAdapter<CharSequence> adapter_goalmodel = ArrayAdapter.createFromResource(this, R.array.string_goalmodel, android.R.layout.simple_spinner_item);
        adapter_goalmodel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalmodel_spinner.setAdapter(adapter_goalmodel);

        Spinner template_spinner = (Spinner)findViewById(R.id.template_spinner);
        final ArrayAdapter<CharSequence> adapter_template = ArrayAdapter.createFromResource(this, R.array.string_template, android.R.layout.simple_spinner_item);
        adapter_template.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        template_spinner.setAdapter(adapter_template);

        if( !checkNetworkAvailable() ){ // if the network doesn't work, then disable related button.
            goalmodel_spinner.setEnabled(false);
            template_spinner.setEnabled(false);
        }else{
            String goalModelList = JerseyClient.getGoalModelList();
            String[] arr = goalModelList.split(",");
            for(String s : arr) {
                adapter_goalmodel.add(s);
            }
            template_spinner.setEnabled(false);
        }

        goalmodel_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String goalModel = .toString();
                Toast.makeText(InstantiationActivity.this, adapter_goalmodel.getItem(position), Toast.LENGTH_LONG);
            }
        });

    }

    List<String> getGoalModelList(){
        return null;
    }

    public boolean checkNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable())
            return false;
        else return true;
    }

}
