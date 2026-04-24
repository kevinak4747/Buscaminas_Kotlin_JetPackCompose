package com.example.buscaminas_2dapracticakotlinjeckpackcompose.audio

import android.content.Context
import android.media.MediaPlayer
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.R

// Esta clase se encarga de crear, iniciar, pausar y liberar la música de fondo del juego
class BackgroundMusicPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    // Preparo el reproductor solo una vez para no crearlo repetidamente
    fun prepare() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.baselenta)
            mediaPlayer?.isLooping = true
        }
    }

    // Inicio la música si el reproductor ya está preparado y no está sonando
    fun start() {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    // Pauso la música si está sonando
    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    // Libero el reproductor cuando ya no se vaya a usar más
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Reinicio la música desde el principio
    fun restart() {
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()
    }
}