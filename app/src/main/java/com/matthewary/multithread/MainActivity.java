package com.matthewary.multithread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void create() {
        try {
            // Returns true if file was made.
            File file = new File(this.getFilesDir(), fileName);
            if (file.createNewFile()) {
                FileWriter fileInput = new FileWriter(file);
                for (int i = 1; i < 10; i++) {
                    fileInput.write(String.valueOf(i) + "\n");
                    Thread.sleep(250);
                }
                fileInput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            FileReader fileOutput = new FileReader(new File(this.getFilesDir(), fileName));
            while (true) {
                // Yes this is an infinite loop, we wait until the file is ready to be read.
                if(fileOutput.ready()) {
                    BufferedReader br = new BufferedReader(fileOutput);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        fileLines.add(line);
                        Thread.sleep(250);
                    }
                    br.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        File file = new File(this.getFilesDir(), fileName);
        file.delete();
        fileLines.clear();
    }
}
