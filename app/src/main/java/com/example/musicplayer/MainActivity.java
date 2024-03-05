package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtNoMusica;
    RecyclerView recyclerView;

    // Lista de musicas
    ArrayList<AudioModel> listaMusicas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle = findViewById(R.id.txt_title_musicas);
        txtNoMusica = findViewById(R.id.txt_nenhuma_musica);
        recyclerView = findViewById(R.id.recycler_view);



        // Checamos a permissão - caso ele retorne falso, solicitamos ao usuario
        if(!checarPermissao()){
            solicitarPermissao();
            return;
        }

        // Cria uma array com os detalhes que queremos mostrar de cada audio capturado do externo - como titulo, data, e tempo
        // de duração
        String[] projecao = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        // Selecionamos arquivos de musica que sejam diferentes de 0
        String selecionar = MediaStore.Audio.Media.IS_MUSIC +" != 0";

        // Navegar na lista de musicas encontradas no externo do celular
        // Acessa a coleção da pasta de Media e definimos o tipo de Media como Audio
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projecao,selecionar,null,null);
        while(cursor.moveToNext()){

            // Aqui acessamos o caminho dos arquivos das misicas
            AudioModel dadosMusicas = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            // Se o caminho do arquivo for encontrado - listamos as musicas e add os dados dela
            if(new File(dadosMusicas.getPath()).exists())
                listaMusicas.add(dadosMusicas);
        }

        // Verificação - Se existe arquivos de musica - caso nao tenha retorar a msg Nenhuma musica encotrada
        // Se o tamanho da lista for igual a 0 - mostra a msg
        if(listaMusicas.size()==0){
            txtNoMusica.setVisibility(View.VISIBLE);
            // Caso Contrario - retorne a lista de musicas gerada no recycler
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicasAdpter(getApplicationContext(), listaMusicas));
        }

    }

    // Checar Permissão de Acesso do usuario ao Conteudo externo - Que colocamos no Manifest
    boolean checarPermissao(){
        // Checamos a permissão de leitrua com o self permission e para qual pagina no caso solicitamos na pagina principal
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        // Se Ok, retornamos como verdadeiro e passamos para a tela, caso nao, fazer a solicitação novamente e acessar
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    // Solicitar Permissão de Acesso
    void solicitarPermissao(){
        // Solicitamos permissão de acesso para o usuario, caso ele dê Ok, avancamos, caso nao, nao liberamos o acesso
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"É NECESSÁRIA PERMISSÃO DE LEITURA, PERMITA NAS CONFIGURAÇÕES", Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
    }

    // Verifica se a lista da musica esta Ok, e da a permissão de deuxar a musica tocando e voltar para a lista sem para o audio
    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new MusicasAdpter(getApplicationContext(),listaMusicas));
        }
    }
}