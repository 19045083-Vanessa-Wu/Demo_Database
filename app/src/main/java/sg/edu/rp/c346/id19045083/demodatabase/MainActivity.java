package sg.edu.rp.c346.id19045083.demodatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnInsert, btnGetTasks;
    TextView tvResults;
    ListView lv;
    ArrayAdapter adapterASC, adapterDESC;
    EditText etTask, etDate;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = findViewById(R.id.buttonInsert);
        btnGetTasks = findViewById(R.id.buttonGetTasks);
        tvResults = findViewById(R.id.textViewResults);
        lv = findViewById(R.id.listView);
        etTask = findViewById(R.id.editTextTask);
        etDate = findViewById(R.id.editTextDate);
        final boolean[] checker = {true};

        // Design for EditText expiry - date picker in edittext
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int mth = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date();
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(String.format("%d-%d-%d", year, month, dayOfMonth));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        etDate.setText(new SimpleDateFormat("dd MMM yyyy").format(date));
                    }
                }, year, mth, day);
                picker.show();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etTask.getText().toString().isEmpty() || etDate.getText().toString().isEmpty())
                {
                    if (etTask.getText().toString().isEmpty()) {
                        etTask.setError("Inputs cannot be empty!");
                    }

                    if (etDate.getText().toString().isEmpty()) {
                        etDate.setError("Inputs cannot be empty!");
                    }
                } else {

                    // Create the DBHelper object, passing in the activity's Context
                    DBHelper db = new DBHelper(MainActivity.this);

                    // Insert a task
                    String task = etTask.getText().toString();
                    db.insertTask(task.toLowerCase(), etDate.getText().toString());

                    //Clear inputs
                    etTask.getText().clear();
                    etDate.getText().clear();
                }

                //Hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
            }
        });

        btnGetTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the DBHelper object, passing in the activity's content
                DBHelper db = new DBHelper(MainActivity.this);

                // Insert a task
                ArrayList<String> data = db.getTaskContent();
                ArrayList<Task> dataASC = db.getTasks();
                ArrayList<Task> dataDESC = db.getTasksDesc();
                db.close();

                String txt = "";
                for (int i=0; i<data.size(); i++) {
                    Log.d("Database Content", i + ". " + data.get(i));
                    txt += i + ". " + data.get(i) + "\n";
                }
                tvResults.setText(txt);
                //lv.setAdapter(adapterASC);

                if (checker[0] ==true) {
                    adapterASC = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, dataASC);
                    lv.setAdapter(adapterASC);
                    checker[0] =false;
                } else if (checker[0] == false){
                    adapterDESC = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, dataDESC);
                    lv.setAdapter(adapterDESC);
                    checker[0] = true;
                }
            }
        });
    }
}