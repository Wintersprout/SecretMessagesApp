package com.example.secretmessages;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText txtIn;
    EditText txtOut;
    EditText txtKey;
    SeekBar slider;
    Button btn;

    public String caesar(String message, int keyVal) {
        StringBuilder output = new StringBuilder();

        char key = (char)keyVal;

        for (int i = 0; i < message.length(); i++) {

            char input = message.charAt(i);

            if (input >= 'A' && input <= 'Z') {
                input += key;

                if (input > 'Z')
                    input -= 26;
                if (input < 'A')
                    input += 26;
            }
            else if (input >= 'a' && input <= 'z') {
                input += key;

                if (input > 'z')
                    input -= 26;
                if (input < 'a')
                    input += 26;
            }
            else if (input >= '0' && input <= '9') {
                input += (keyVal % 10);

                if (input > '9')
                    input -= 10;
                if (input < '0')
                    input += 10;
            }
            output.append(input);
        }

        return output.toString();
    }
    private void setSlider() {
        try {
            int keyVal = Integer.parseInt(txtKey.getText().toString());
            //slider only accepts positive values, hence the 13 adjustments.
            if (keyVal == (slider.getProgress() - 13)) {
                String message = txtIn.getText().toString();
                txtKey.setText("" + keyVal);
                txtOut.setText(caesar(message, keyVal));
            }
            else {
                slider.setProgress(keyVal + 13);
                txtKey.selectAll();
            }
        }
        catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Please enter a whole number for the encryption key.");
        }
        finally {
            txtKey.requestFocus();
            txtKey.selectAll();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtIn = (EditText)findViewById(R.id.txtIn);
        txtOut = (EditText)findViewById(R.id.txtOut);
        txtKey = (EditText)findViewById(R.id.txtKey);
        slider = (SeekBar)findViewById(R.id.seekBar2);
        btn = (Button)findViewById(R.id.button);

        Intent receiveIntent = getIntent();
        String receivedText = receiveIntent.getStringExtra(Intent.EXTRA_TEXT);
        if (receivedText != null)
            txtIn.setText(receivedText);

        btn.setOnClickListener(v -> setSlider());

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int keyVal = slider.getProgress() - 13;
                String message = txtIn.getText().toString();
                txtKey.setText("" + keyVal);
                txtOut.setText(caesar(message, keyVal));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Secret Message " +
                    DateFormat.getDateTimeInstance().format(new Date()));
            shareIntent.putExtra(Intent.EXTRA_TEXT, txtOut.getText().toString());
            try {
                startActivity(Intent.createChooser(shareIntent, "Share message..."));
                finish();
            }
            catch(android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "Error. Could not share.",
                        Toast.LENGTH_SHORT).show();
            }
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
        });
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
}