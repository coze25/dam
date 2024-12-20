package com.example.testcarte;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testcarte.sampledata.AsyncTaskRunner;
import com.example.testcarte.sampledata.Callback;
import com.example.testcarte.sampledata.HttpManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String SIGN_KEY = "sign_key";
    public static final String VALUE_KEY = "value_key";
    FloatingActionButton fabSync;
    Button btnSterge;
    Spinner spn;
    EditText et;
    AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private static final String URL = "https://api.npoint.io/efcea1d46c36fa555d04";

    List<Carte> carti = new ArrayList<>();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        fabSync.setOnClickListener(v->{
            asyncTaskRunner.executeAsync(new HttpManager(URL), callbackHttp());

            if(!et.getText().toString().isEmpty()) {
                var editor = sharedPreferences.edit();
                editor.putInt(VALUE_KEY, Integer.parseInt(et.getText().toString()));
                editor.putString(SIGN_KEY, spn.toString());
                editor.apply();
                editor.commit();
            }
        });
    }

    private Callback<String> callbackHttp() {
        return result -> {
            if(result!=null){
                List<Carte> parsedList = ParseJson.parseJson(result);

                parsedList.forEach(carte -> {
                    if(!carti.contains(carte))
                    {
                        carti.add(carte);
                    }
                });

                Log.i("dam", carti.toString());
            }
        };
    }

    private void initComponents() {
        fabSync = findViewById(R.id.fabSync);
        btnSterge = findViewById(R.id.btnSterge);
        spn = findViewById(R.id.spinner);
        et = findViewById(R.id.editTextPagini);

        sharedPreferences = getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
        String s = sharedPreferences.getString(SIGN_KEY, "");
        int val = sharedPreferences.getInt(VALUE_KEY, 0);

        if(val!=0)
        {
            et.setText(String.valueOf(val));
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerValues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        if(!s.isBlank()) {
            for(int i = 0; i < adapter.getCount(); i++) {
                if(adapter.getItem(i).toString().equals(s)) {
                    spn.setSelection(i);
                    break;
                }
            }
        }

    }
}