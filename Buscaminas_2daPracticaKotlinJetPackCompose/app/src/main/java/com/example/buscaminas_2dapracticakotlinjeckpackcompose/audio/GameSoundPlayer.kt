package com.example.buscaminas_2dapracticakotlinjeckpackcompose.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.buscaminas_2dapracticakotlinjeckpackcompose.R

// Esta clase se encarga de gestionar todos los efectos de sonido del juego
// Uso SoundPool porque es más rápido que MediaPlayer para sonidos cortos
class GameSoundPlayer(context: Context) {

    // Configuro los atributos de audio indicando que es sonido de juego
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    // Creo el SoundPool con un máximo de 5 sonidos simultáneos
    // Esto evita saturar el audio si se disparan varios sonidos seguidos
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(audioAttributes)
        .build()

    // Cargo todos los sonidos en memoria al crear la clase
    // Esto hace que luego se reproduzcan de forma inmediata sin retrasos

    // Sonido al abrir una casilla normal (con número)
    private val revealSoundId = soundPool.load(context, R.raw.relevacion, 1)

    // Sonido cuando se abre una zona vacía (expansión)
    private val expansionSoundId = soundPool.load(context, R.raw.expansion, 1)

    // Sonido al poner o quitar una bandera
    private val flagSoundId = soundPool.load(context, R.raw.bandera, 1)

    // Sonido al pulsar una mina (impacto inicial)
    private val bombSoundId = soundPool.load(context, R.raw.boom, 1)

    // Sonido al ganar la partida
    private val winSoundId = soundPool.load(context, R.raw.win, 1)

    // Sonido al perder la partida (feedback final)
    private val loseSoundId = soundPool.load(context, R.raw.loseslow, 1)

    // Reproduzco el sonido de abrir una casilla normal
    fun playReveal() {
        soundPool.play(revealSoundId, 1f, 1f, 1, 0, 1f)
    }

    // Reproduzco el sonido de expansión
    // Este se usa cuando la casilla es vacía y se abren varias en cascada
    fun playExpansion() {
        soundPool.play(expansionSoundId, 1f, 1f, 1, 0, 1f)
    }

    // Reproduzco el sonido de bandera
    // Se dispara tanto al poner como al quitar
    fun playFlag() {
        soundPool.play(flagSoundId, 1f, 1f, 1, 0, 1f)
    }

    // Reproduzco el sonido de bomba
    // Este es el impacto inmediato al perder
    fun playBomb() {
        soundPool.play(bombSoundId, 1f, 1f, 1, 0, 1f)
    }

    // Reproduzco el sonido de victoria
    fun playWin() {
        soundPool.play(winSoundId, 1f, 1f, 1, 0, 1f)
    }

    // Reproduzco el sonido de derrota
    // Este se usa como feedback final después del impacto de la mina
    fun playLose() {
        soundPool.play(loseSoundId, 1f, 1f, 1, 0, 1f)
    }

    // Libero los recursos cuando ya no se vayan a usar
    // Esto es importante para evitar fugas de memoria
    fun release() {
        soundPool.release()
    }
}