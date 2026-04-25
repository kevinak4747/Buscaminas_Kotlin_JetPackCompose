package com.example.buscaminas_2dapracticakotlinjeckpackcompose.vibration

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

// Esta clase se encarga solo de hacer vibrar el móvil durante la partida
class GameVibrationPlayer(
    private val context: Context
) {
    fun vibrateOnLose() {
        // Obtengo el servicio de vibración según la versión de Android
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        // Compruebo que el dispositivo pueda vibrar antes de ejecutar la vibración
        if (!vibrator.hasVibrator()) {
            return
        }

        // Uso una vibración corta para dar feedback al perder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }
}