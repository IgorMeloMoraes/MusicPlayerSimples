package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerMusicasActivity extends AppCompatActivity {

    TextView txtTituloAudioPlayer,tempoCorrente, tempoTotal;
    RelativeLayout controles;
    SeekBar seekBar;
    ImageView iconCentral, iconProximo, iconAnterior, iconPausarPlayer;

    ArrayList<AudioModel> listaMusicas;
    AudioModel audioCorrente;
    MediaPlayer mediaPlayer = Player.getInstance();

    // Variavel para a rotação do icone central
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_musicas);

        txtTituloAudioPlayer = findViewById(R.id.txt_titulo_audio_player);
        tempoCorrente = findViewById(R.id.tempo_corrente);
        tempoTotal = findViewById(R.id.tempo_total);
        controles = findViewById(R.id.controles);
        seekBar = findViewById(R.id.seek_bar);
        iconCentral = findViewById(R.id.icon_central);
        iconAnterior = findViewById(R.id.proximo);
        iconProximo = findViewById(R.id.anterior);
        iconPausarPlayer = findViewById(R.id.pausar_play);


        // Informaos o texto da musica selecionada e retornmos se é verdadeiro
        txtTituloAudioPlayer.setSelected(true);

        // Retornamos a lista de musica vindo do adpter
        listaMusicas = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        // Retornamos as configurações de musica
        configuracaoPlayer();

        // Configuração de Player na UI para trocar os icones e o movimento da seekbar
        PlayerMusicasActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Verifica se o player é diferente de nulo
                if(mediaPlayer!=null){
                    // add um progresso para a seek bar de acorco com o tempo de duração do player
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    // altera o tempo corrente seguindo segundo a segundo
                    tempoCorrente.setText(conversor(mediaPlayer.getCurrentPosition()+""));

                    // Verifica se está ativo a musica, em esta der player
                    if(mediaPlayer.isPlaying()){
                        // Caso sim, altera os icones e tras movindo para o icon central
                        iconPausarPlayer.setImageResource(R.drawable.ic_pausar);
                        iconCentral.setRotation(x++);
                    }else{
                        // Se a musica estiver para, volktar o icon central para a posiçaõ 0 e o icone de play
                        iconPausarPlayer.setImageResource(R.drawable.ic_play);
                        iconCentral.setRotation(0);
                    }

                }
                // Estabele um tempo de dalay
                new Handler().postDelayed(this,100);
            }
        });

        // Configuração da Seek bar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            // Caso o audio esteja em progresso, pegar o progreso do tamanho da musica
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void configuracaoPlayer(){

        // Pega o index atual da musica na lista, para quando passamor a musica ele seguir o index corretamente
        audioCorrente = listaMusicas.get(Player.currentIndex);

        // Setamos o titulo da musica buscando no nosso AudioModel que passamos para acessar os arquivos de musica externo e
        // extrai informações
        txtTituloAudioPlayer.setText(audioCorrente.getTitulo());

        // Acessa o tempo total da musica com a informação do audio e transformamos esse texto em tempo MM:SS
        tempoTotal.setText(conversor(audioCorrente.getDuracao()));

        // Ações de click com cada botão
        iconPausarPlayer.setOnClickListener(v-> pararPlay());
        iconProximo.setOnClickListener(v-> proximaMusica());
        iconAnterior.setOnClickListener(v-> anteriorMusica());

        // Musica Rodando
        playMusic();

    }


    // Configuraçoes de Player na Musica
    private void playMusic(){

        // Aqui retormos a variavel de musica player para acessar os recursos
        mediaPlayer.reset();
        // Configura como o audio se comportanda quando player
        try {
            // Pega os dados da musica atraves do caminho externo
            mediaPlayer.setDataSource(audioCorrente.getPath());
            // faz a preparação do audio
            mediaPlayer.prepare();
            // Inicia o Audio
            mediaPlayer.start();
            // Seta o progresso inicial para 0
            seekBar.setProgress(0);
            // Seta o tempo de duração maxima do audio
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void proximaMusica(){

        // Verifica se proima musica está na lista e nao é menor que -1
        if(Player.currentIndex == listaMusicas.size()-1)
            return;
        // Chama a proxima musuca
        Player.currentIndex +=1;
        mediaPlayer.reset();
        configuracaoPlayer();
    }

    // Configurações para voltar a musuca
    private void anteriorMusica(){
        // Verifica se a musuca anterior é igual a 0
        if(Player.currentIndex == 0)
            return;
        // Retorna no index -1, voltando para a anterior da lista
        Player.currentIndex -= 1;
        mediaPlayer.reset();
        configuracaoPlayer();
    }

    // Configurações de Parar e Player
    private void pararPlay(){
        // Verifica se a musica esta tocando, caso sim ele para, caso nao ele inicia
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    // Coversor de Minutos e Segundos para colocar no seek do tempo de musica
    public static String conversor(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}