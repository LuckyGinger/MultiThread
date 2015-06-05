package com.matthewary.multithread;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.WrapperListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String fileName = "numbers.txt";
    private List<String> fileLines = new ArrayList<>();
    private ListView listView = null;
    private Handler handler = new Handler();
    private ProgressBar progressBar = null;
    private File file;
    private static Context context;
    private int prog = 0;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        MainActivity.context = MainActivity.this.getApplicationContext();
        listView = (ListView) findViewById(R.id.listView);
        file = new File(this.getFilesDir(), fileName);
        if (file.exists())
            file.delete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void create(View view) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // Returns true if file was made.
                    if (file.createNewFile()) {
                        FileWriter fileInput = new FileWriter(file);
                        for (int i = 1; i <= 10; ++i) {
                            fileInput.write(String.valueOf(i) + "\n");
                            final int j = i;
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(10 * j);
                                }
                            });
                            Thread.sleep(250);
                        }
                        progressBar.setProgress(0);
                        fileInput.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void load(View view) {
        new Thread (new Runnable() {
            public void run() {
                try {
                    FileReader fileOutput = new FileReader(file);

                    if (fileOutput.ready()) {
                        BufferedReader br = new BufferedReader(fileOutput);
                        String line;
                        while ((line = br.readLine()) != null) {
                            fileLines.add(line);
                            if (prog < 100) {
                                prog += 10;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(prog);
                                    }
                                });
                            }

                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        progressBar.setProgress(0);

                        br.close();
                        arrayAdapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_list_item_1, fileLines);
                        listView = (ListView) findViewById(R.id.listView);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                listView.setAdapter(arrayAdapter);
                            }
                        });


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void clear(View view) {
        file.delete();
        listView.setAdapter(null);
        fileLines.clear();
    }
}
