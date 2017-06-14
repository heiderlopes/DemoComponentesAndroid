package br.com.heiderlopes.democomponentesandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.heiderlopes.democomponentesandroid.adapter.ContatosAdapter;
import br.com.heiderlopes.democomponentesandroid.models.Contato;

public class ListaContatosActivity extends AppCompatActivity {

    private List<Contato> contatos;
    private ListView lvContatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);


        lvContatos = (ListView) findViewById(R.id.lvContatos);

        if(ContextCompat.checkSelfPermission(ListaContatosActivity.this,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            carregaContatos();
        } else {
            ActivityCompat.requestPermissions(
                    ListaContatosActivity.this,
                    new String[] {Manifest.permission.READ_CONTACTS}, 0
            );
        }
    }

    private void carregaContatos() {
        Cursor phones = getContentResolver()
                .query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, null);

        if(phones.getCount() > 0) {
            contatos = new ArrayList<>();
            phones.moveToFirst();
            do {
                String nome = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String telefone = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                Contato contato = new Contato();
                contato.setNome(nome);
                contato.setTelefone(telefone);
                contatos.add(contato);
            } while (phones.moveToNext());

            lvContatos.setAdapter(new ContatosAdapter(this, contatos));

            lvContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent();
                    i.putExtra("telefone", contatos.get(position).getTelefone());
                    setResult(RESULT_OK, i);
                    finish();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults ) {

        switch (requestCode) {
            case 0:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    carregaContatos();
                } else {
                    Toast.makeText(ListaContatosActivity.this, " ;( Não foi possível completar a ligação",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
