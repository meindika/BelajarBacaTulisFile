package com.gmail.meindika80.belajarbacatulisfile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText nama, telepon;
    //TextView dataTelepon;
    public FloatingActionButton fab;

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayKontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nama = findViewById(R.id.editNama);
        telepon = findViewById(R.id.editTelp);
        //dataTelepon = findViewById(R.id.textDataKontak);
        listView = findViewById(R.id.listKontak);

        arrayKontak = new ArrayList<>();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // menyiapkan buffer --> penyimpanan sementara (cache)
                byte[] bufferNama = new byte[30];
                byte[] bufferTelepon = new byte[15];

                salinData(bufferNama, nama.getText().toString());
                salinData(bufferTelepon, telepon.getText().toString());

                // proses menulis ke file
                try{
                    // TODO 1: menulis - proses mengenali file untuk menyimpan
                    FileOutputStream dataFile = openFileOutput("telepon.dat",
                            MODE_APPEND);
                    DataOutputStream output = new DataOutputStream(dataFile);

                    // TODO 2: menulis - buffer ditulis ke dalam file
                    output.write(bufferNama);
                    output.write(bufferTelepon);
                    // TODO 3: menulis - menyelesaikan proses menulis ke file
                    dataFile.close();

                    // menampilkan konfirmasi
                    Snackbar.make(view, "Data telah disimpan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    // mengosongkan form
                    nama.setText("");
                    telepon.setText("");
                }
                catch (IOException e){
                    Toast.makeText(getBaseContext(), "Kesalahan: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                tampilkanData();
            }
        });
        tampilkanData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void salinData(byte[] buffer, String data) {
        // konten buffer dibuat 0 --> reset
        for (int i = 0; i < buffer.length; i++) buffer[i] = 0;
        // mengkonversi dan memindahkan String ke buffer
        for (int i = 0; i < data.length(); i++) buffer[i] = (byte) data.charAt(i);
    }

    public void tampilkanData() {
        try {
            // TODO 1: membaca - proses mengenali file untuk dibaca
            FileInputStream dataFile = openFileInput("telepon.dat");
            DataInputStream input = new DataInputStream(dataFile);

            // TODO 2: membaca - ukuran buffer
            byte[] bufNama = new byte[30];
            byte[] bufTelepon = new byte[15];
            String infoData = "";
            int no = 1;
            arrayKontak.clear();

            while (input.available() > 0) {
                // TODO 3: membaca - proses membaca file
                input.read(bufNama);
                input.read(bufTelepon);

                // TODO 4: membaca - proses mengubah/ mengkonversi ke string
                String dataNama = "";
                for (int i = 0; i < bufNama.length; i++)
                    dataNama = dataNama + (char) bufNama[i];
                String dataTelepon = "";
                for (int i = 0; i < bufTelepon.length; i++)
                    dataTelepon = dataTelepon + (char) bufTelepon[i];

                //infoData = infoData + no + ". " + dataNama + " - " + dataTelepon + "\n";
                infoData = no + ". " + dataNama + " - " +dataTelepon;
                arrayKontak.add(infoData);
                no++;
            }
            //dataTelepon.setText(infoData);
            // TODO 5: membaca - mengakhiri proses membaca file
            dataFile.close();
        }
        catch (IOException e) {
            Toast.makeText(getBaseContext(), "Kesalahan: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayKontak);
        listView.setAdapter(adapter);
    }
}