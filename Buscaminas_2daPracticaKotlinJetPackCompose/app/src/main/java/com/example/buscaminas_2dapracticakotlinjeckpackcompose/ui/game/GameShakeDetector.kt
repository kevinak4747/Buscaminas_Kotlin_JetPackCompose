package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

// Esta clase escucha el acelerómetro para detectar una sacudida fuerte del móvil
// Cuando detecta un shake, llama a la función que le pasamos desde fuera
class GameShakeDetector(
    context: Context,
    private val onShakeDetected: () -> Unit
) : SensorEventListener {

    // Obtengo el servicio de sensores del sistema para poder acceder al acelerómetro
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Guardo una referencia al acelerómetro del dispositivo
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Aquí guardo el último momento en el que detecté un shake
    // Lo uso para evitar que una sola sacudida dispare varios reinicios seguidos
    private var lastShakeTime = 0L

    // Este valor marca la fuerza mínima para considerar que hubo sacudida
    private val shakeThreshold = 15f

    // Este tiempo mínimo evita disparos repetidos demasiado seguidos
    private val shakeCooldownMillis = 1000L

    // Empiezo a escuchar cambios del acelerómetro
    fun startListening() {
        accelerometer?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    // Dejo de escuchar el acelerómetro para no gastar recursos
    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    // Este método se ejecuta cada vez que cambia un sensor
    // En nuestro caso lo usamos para leer el acelerómetro y detectar sacudidas
    override
    fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        // Solo me interesa el acelerómetro
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Calculo la fuerza total del movimiento en los tres ejes
        val acceleration = sqrt(x * x + y * y + z * z)

        val currentTime = System.currentTimeMillis()

        // Si la fuerza supera el umbral y ya pasó el tiempo de espera, detecto shake
        if (acceleration > shakeThreshold &&
            currentTime - lastShakeTime > shakeCooldownMillis
        ) {
            lastShakeTime = currentTime
            onShakeDetected()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesito hacer nada aquí para esta práctica
    }
}