package br.com.heiderlopes.democomponentesandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etTel;
    private Button btLigar;
    private Button btContatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTel = (EditText)findViewById(R.id.etTel);
        btLigar = (Button) findViewById(R.id.btLigar);
        btContatos = (Button) findViewById(R.id.btContatos);

        btContatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListaContatosActivity.class);
                startActivityForResult(i, 0);
            }
        });

        btLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligar();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                switch (resultCode) {
                    case RESULT_OK:
                        etTel.setText(data.getStringExtra("telefone"));
                        break;
                    case RESULT_CANCELED:
                        break;

                }
                break;
        }
    }

    private void ligar() {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + etTel.getText().toString()));

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(phoneIntent);
        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[] {Manifest.permission.CALL_PHONE}, 0
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                          int[] grantResults ) {

        switch (requestCode) {
            case 0:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ligar();
                } else {
                    Toast.makeText(MainActivity.this, " ;( Não foi possível completar a ligação",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
