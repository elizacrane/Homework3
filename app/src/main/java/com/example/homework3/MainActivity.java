package com.example.homework3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private EditText enterTask;
    private ListView todolist;
    private ArrayList<String> tasks;
    private int i;
    private ArrayAdapter<String> arrayAdapter;
    private ToDoList adapter;
    private TextToSpeech speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        enterTask =(EditText) findViewById(R.id.editTask);
        todolist = (ListView) findViewById(R.id.viewList);
        tasks = new ArrayList<>();
        tasks.add("Study");
        tasks.add("Shop");
        tasks.add("Sleep");

        adapter = new ToDoList(this,tasks);
        todolist.setAdapter(adapter);

        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                if(status != TextToSpeech.ERROR){
                    speaker.setLanguage(Locale.US);
                    speaker.speak("Text to speech enabled.", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }


        });

        speak("list.txt");

        enterTask =(EditText) findViewById(R.id.editTask);
        todolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l){
                i = pos;
                enterTask.setText(tasks.get(pos));
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void speak(String file){

        try{
            FileInputStream inputstream = openFileInput(file);
            InputStreamReader inputreader =new InputStreamReader(inputstream);
            BufferedReader bufferedreader =new BufferedReader(inputreader);
            String line;
            while((line = bufferedreader.readLine()) != null){
                tasks.add(line);
            }
            bufferedreader.close();
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "No saved file exists.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void saveToFile(String file){
        try {
            FileOutputStream outputstream= openFileOutput(file, MODE_PRIVATE);
            OutputStreamWriter outputwriter =new OutputStreamWriter(outputstream);
            for(int i = 0; i < tasks.size(); i++){
                outputwriter.write(tasks.get(i) + '\n');
            }
            outputwriter.close();
            Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();


        } catch (IOException e){
            System.out.println("Something went wrong.");
            Toast.makeText(MainActivity.this, "Failed to save.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String text = enterTask.getText().toString();

        switch (item.getItemId()) {
            case R.id.add:
                if(text.isEmpty()){
                    Toast.makeText(MainActivity.this, "Text field empty.", Toast.LENGTH_SHORT).show();
                }else{
                    tasks.add(text);
                    adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
                enterTask.getText().clear();
                speaker.speak("'" + text + "'" + "was added.", TextToSpeech.QUEUE_ADD, null,null);

                return true;

            case R.id.delete:
                if (text.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please select an item.", Toast.LENGTH_SHORT).show();
                }else{
                    tasks.remove(i);
                }
                adapter.notifyDataSetChanged();
                enterTask.getText().clear();
                speaker.speak("'" + text + "'" + "was deleted.", TextToSpeech.QUEUE_ADD, null, null);

                return true;

            case R.id.update:
                String updatedtask = tasks.get(i);
                if (enterTask.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please add something to the text field.", Toast.LENGTH_SHORT).show();
                }else{
                    tasks.set(i, enterTask.getText().toString());
                }
                adapter.notifyDataSetChanged();
                enterTask.getText().clear();
                speaker.speak("'" + updatedtask + "'"  + " was updated to " + text, TextToSpeech.QUEUE_ADD, null, null);

                return true;

            case R.id.save:
                saveToFile("list.txt");
                speaker.speak("File saved successfully.", TextToSpeech.QUEUE_ADD, null, null);

                return true;

            case R.id.close:
                saveToFile("list.txt");
                finish();


            default:
                return super.onOptionsItemSelected(item);

        }
    }
}