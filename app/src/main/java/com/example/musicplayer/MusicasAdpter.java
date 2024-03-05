package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicasAdpter extends RecyclerView.Adapter<MusicasAdpter.MyViewHolder> {

    Context context;
    ArrayList<AudioModel> listaMusicas;

    public MusicasAdpter(Context context, ArrayList<AudioModel> listaMusicas) {
        this.context = context;
        this.listaMusicas = listaMusicas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_musicas,parent,false);
        return new MusicasAdpter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // Pega a posiçção da lista de musica e lista de acordo com o titulo
        AudioModel dadosMusica = listaMusicas.get(position);
        holder.txtTituloMuscias.setText(dadosMusica.getTitulo());

        // Verifica a posição do index e defini uma cor para informar qual a musica está tocando no player
        if(Player.currentIndex == position){
            holder.txtTituloMuscias.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.txtTituloMuscias.setTextColor(Color.parseColor("#000000"));
        }

        // Ação de click - quando clicar no audio selecionado ele ir para a pagina pagina de audio play
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a activity
                Player.getInstance().reset();
                Player.currentIndex = position;

                // Ir para a proxima tela - Passo a minha lista de musica e indico com a flag que é  uma nova tela
                Intent intent = new Intent(context, PlayerMusicasActivity.class);
                intent.putExtra("LIST",listaMusicas);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaMusicas.size();
    }

    // Acessa e recura os dados da lista do recycler layout que criamos
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTituloMuscias;
        ImageView imgIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTituloMuscias = itemView.findViewById(R.id.txt_musica_lista);
            imgIcon = itemView.findViewById(R.id.icon_view);
        }
    }
}
