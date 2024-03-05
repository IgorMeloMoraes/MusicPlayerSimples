package com.example.musicplayer;

import java.io.Serializable;

public class AudioModel implements Serializable {

    String path;
    String titulo;
    String duracao;

    public AudioModel(String path, String titulo, String duracao) {
        this.path = path;
        this.titulo = titulo;
        this.duracao = duracao;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }
}
