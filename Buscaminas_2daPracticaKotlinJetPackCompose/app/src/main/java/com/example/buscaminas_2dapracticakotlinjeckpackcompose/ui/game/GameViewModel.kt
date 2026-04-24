package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// ViewModel del juego.
// Aquí irá la lógica de la partida y el estado reactivo.
class GameViewModel : ViewModel(){

    // Estado interno mutable, solo lo toca el ViewModel.
    private val _uiState = MutableStateFlow(GameUiState())

    // Estado público de solo lectura para que la UI lo observe.
    val uiState: StateFlow<GameUiState> = _uiState

    // Job que uso para controlar el cronómetro
    private var timerJob: Job? = null

    init {
        // Al crear el ViewModel, dejamos el juego listo para empezar.
        startNewGame()
    }

    // Punto único de entrada de eventos desde la UI
    // La UI no llama directamente a funciones internas
    fun onEvent(event: GameEvent) {
        when (event) {
            // Si el usuario pulsa una celda, llamamos a la función que maneja esa lógica de abrir
            // casillas, perder o ganar.
            is GameEvent.CellPressed -> onCellPressed(event.row, event.col)
            // Si el usuario hace pulsación larga, llamamos a la función que maneja esa lógica de poner
            // o quitar bandera.
            is GameEvent.CellLongPressed -> onCellLongPressed(event.row, event.col)
            // Si el usuario pulsa el botón de reiniciar, empezamos una partida nueva.
            GameEvent.RestartPressed -> startNewGame()
            // Si la app pierde el foco, pausamos la partida, esto es importante para que el cronómetro
            // no siga corriendo mientras el usuario no está jugando
            GameEvent.AppPaused -> pauseGame()
            // Si el usuario pulsa el botón de pausa manual, pausamos la partida, esto es importante
            // para que el cronómetro no siga corriendo mientras el usuario no está jugando
            GameEvent.PausePressed -> pauseGame()
            // Si el usuario vuelve a la app después de una pausa, reanudamos la partida
            // Esto es importante para que el cronómetro vuelva a funcionar y el usuario pueda seguir jugando
            GameEvent.ResumePressed -> resumeGame()
            // Si la UI le avisa que ya ha reproducido el efecto de sonido pendiente, lo limpiamos
            // del estado para no reproducirlo otra vez
            GameEvent.SoundEffectConsumed -> clearPendingSoundEffect()
        }
    }

    // Arranca el cronómetro de la partida
    private fun startTimer() {

        // Si ya hay un cronómetro corriendo, lo paro antes de crear otro
        stopTimer()

        timerJob = viewModelScope.launch {

            // Mientras esta coroutine siga viva, voy sumando segundos
            while (isActive) {
                delay(1000)

                val current = _uiState.value

                // Solo sumo tiempo si la partida sigue en curso
                if (current.status == GameStatus.PLAYING) {
                    _uiState.value = current.copy(
                        elapsedSeconds = current.elapsedSeconds + 1
                    )
                }
            }
        }
    }

    // Detiene el cronómetro actual si existe
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    // Función interna cuando el usuario pulsa una celda, se utiliza para manejar la lógica
    // de abrir casillas, perder o ganar.
    private fun onCellPressed(row: Int, col: Int) {

        // Leemos el estado actual.
        val current = _uiState.value

        // Si la partida no está en curso, ignoramos pulsaciones.
        if (current.status != GameStatus.PLAYING) return

        // Obtenemos la celda que el usuario ha pulsado.
        val pressedCell = current.board[row][col]

        // Si la casilla ya está revelada o tiene bandera, no hago nada
        if (pressedCell.isRevealed || pressedCell.hasFlag) return

        // Si es una mina, perdemos y revelamos todo el tablero.
        if (pressedCell.isMine) {

            val revealedBoard = mutableListOf<List<CellUi>>()

            for (r in 0 until current.rows) {
                val newRow = mutableListOf<CellUi>()

                for (c in 0 until current.cols) {
                    val cell = current.board[r][c]
                    newRow.add(cell.copy(isRevealed = true))
                }

                revealedBoard.add(newRow)
            }
            // Si pulsa una mina, detengo el cronómetro
            stopTimer()

            _uiState.value = current.copy(
                status = GameStatus.LOST,
                board = revealedBoard,
                pendingSoundEffect = GameSoundEffect.BOMB
            )

            return
        }

        // Creo una copia mutable del tablero para poder modificarlo
        val mutableBoard = current.board.map { it.toMutableList() }.toMutableList()

        // Abro casillas en cascada si es necesario
        revealCells(row, col, mutableBoard)

        // Convierto el tablero de nuevo a inmutable
        // el it.toList() es para convertir cada fila mutable a inmutable, el map lo hace con todas
        // las filas, luego el toList() final convierte la lista de filas a inmutable también.
        val newBoard = mutableBoard.map { it.toList() }

        // Comprobamos si ya se han revelado todas las no-mina.
        val won = checkWin(newBoard)

        // Si ya ha ganado, detengo el cronómetro
        if (won) {
            stopTimer()
        }

        // Decido qué sonido toca según el resultado de la jugada
        val soundEffect = when {
            won -> GameSoundEffect.WIN
            pressedCell.adjacentMines == 0 -> GameSoundEffect.EXPANSION
            else -> GameSoundEffect.REVEAL
        }

        // Actualizamos el estado: si ganó, cambiamos status.
        _uiState.value = current.copy(
            status = if (won) GameStatus.WON else GameStatus.PLAYING,
            board = newBoard,
            pendingSoundEffect = soundEffect
        )

    }
    // Función para generar posiciones aleatorias de minas sin repetir pr eso las creo en un Set Para
    // asegurarme de que no se repiten , le pasamos el número de filas, columnas y minas que queremos generar
    private fun generateRandomMines(
        rows: Int,
        cols: Int,
        mineCount: Int
    ): Set<Pair<Int, Int>> {

        // Aquí voy guardando posiciones únicas de minas.
        // Uso un Set para asegurarme de que no se repiten.
        val mines = mutableSetOf<Pair<Int, Int>>()

        // Sigo generando hasta tener la cantidad de minas pedida.
        while (mines.size < mineCount) {

            // Fila aleatoria entre 0 y rows-1
            val r = Random.nextInt(rows)

            // Columna aleatoria entre 0 y cols-1
            val c = Random.nextInt(cols)

            // Pair(r, c) representa una coordenada (fila, columna).
            mines.add(Pair(r, c))
        }

        return mines
    }


    // Inicia una partida nueva
    private fun startNewGame() {
        // Si había un cronómetro anterior, lo paro antes de empezar otra partida
        stopTimer()
        // Leo el número de filas, columnas y minas del estado actual para crear el nuevo tablero proveniente del
        //gameUiState
        val rows = _uiState.value.rows
        val cols = _uiState.value.cols
        val mineCount = _uiState.value.mineCount

        // Genero posiciones aleatorias de minas sin repetir en una lista de coordenadas (fila, columna)
        val mines = generateRandomMines(rows, cols, mineCount)

        // Construyo el tablero sabiendo dónde están las minas
        val board = buildBoardWithMines(rows, cols, mines)

        // Actualizo el estado completo que verá la UI
        _uiState.value = GameUiState(
            rows = rows,
            cols = cols,
            mineCount = mineCount,
            status = GameStatus.PLAYING,
            elapsedSeconds = 0,
            showPauseOverlay = false,
            board = board
        )

        // Arranco el cronómetro de la partida nueva
        startTimer()
    }

    // Construye el tablero sabiendo dónde están las minas
    // Devuelve una lista de filas y cada fila contiene casillas
    private fun buildBoardWithMines(
        rows: Int,
        cols: Int,
        mines: Set<Pair<Int, Int>>
    ): List<List<CellUi>> {

        // Creo la estructura del tablero vacía
        val board = mutableListOf<List<CellUi>>()

        // Recorro todas las filas
        for (r in 0 until rows) {

            // Creo una fila nueva
            val rowList = mutableListOf<CellUi>()

            // Recorro todas las columnas de esa fila
            for (c in 0 until cols) {

                // Compruebo si en esta posición hay una mina
                val isMine = mines.contains(Pair(r, c))

                // Creo la casilla con toda su información
                rowList.add(
                    CellUi(
                        row = r,
                        col = c,

                        // Indico si esta casilla tiene mina
                        isMine = isMine,

                        // Si no es mina, calculo cuántas minas hay alrededor
                        // Si es mina, no hace falta calcular nada
                        adjacentMines = if (isMine) 0
                        else countAdjacentMines(r, c, mines),

                        // Al iniciar la partida ninguna casilla está revelada
                        isRevealed = false,

                        // Al iniciar la partida ninguna casilla tiene bandera
                        hasFlag = false
                    )
                )
            }

            // Añado la fila completa al tablero
            board.add(rowList)
        }

        // Devuelvo el tablero ya construido
        return board
    }

    private fun countAdjacentMines(
        row: Int,
        col: Int,
        mines: Set<Pair<Int, Int>>
    ): Int {

        var count = 0

        // Recorremos los desplazamientos -1, 0, 1 en filas y columnas.
        for (dr in -1..1) {
            for (dc in -1..1) {

                // Saltamos la propia celda (0,0).
                if (dr == 0 && dc == 0) continue

                val nr = row + dr
                val nc = col + dc

                // Si hay una mina en esa posición vecina, sumamos 1.
                if (mines.contains(Pair(nr, nc))) {
                    count++
                }
            }
        }

        return count
    }

    private fun checkWin(board: List<List<CellUi>>): Boolean {
        for (rowList in board) {
            for (cell in rowList) {
                if (!cell.isMine && !cell.isRevealed) return false
            }
        }
        return true
    }

    // Abre casillas en cascada si están vacías
    private fun revealCells(
        row: Int,
        col: Int,
        board: MutableList<MutableList<CellUi>>
    ) {

        // Compruebo que la posición está dentro del tablero
        if (row !in board.indices || col !in board[0].indices) return

        val cell = board[row][col]

        // Si ya está revelada o tiene bandera, no hago nada
        if (cell.isRevealed || cell.hasFlag) return

        // Marco la casilla como revelada
        board[row][col] = cell.copy(isRevealed = true)

        // Si tiene número, no sigo expandiendo
        if (cell.adjacentMines > 0) return

        // Si la casilla está vacía, abro también las casillas vecinas
        // y si alguna vecina también está vacía, sigo abriendo sus vecinas
        // Esto hace el efecto de expansión automática típico del Buscaminas ya que es una función recursiva que se
        // llama a sí misma para abrir las casillas vecinas de las casillas vacías.
        //for (dr in -1..1) es para recorrer las filas vecinas, desde la fila anterior (row-1) hasta la fila siguiente
        // (row+1)
        for (dr in -1..1) {
            //for (dc in -1..1) es para recorrer las columnas vecinas, desde la columna anterior (col-1) hasta
            // la columna siguiente (col+1)
            for (dc in -1..1) {
                // Saltamos la propia celda (0,0) para no entrar en un bucle infinito de llamarse a sí misma.
                if (dr == 0 && dc == 0) continue
                // Llamo recursivamente para abrir la casilla vecina.
                revealCells(row + dr, col + dc, board)
            }
        }
    }

    // Función interna cuando el usuario hace pulsación larga en una celda
    private fun onCellLongPressed(row: Int, col: Int) {

        // Leo el estado actual
        val current = _uiState.value

        // Si la partida no está en curso, no permito poner banderas
        if (current.status != GameStatus.PLAYING) return

        val cell = current.board[row][col]

        // Si la casilla ya está revelada, no se puede marcar con bandera
        if (cell.isRevealed) return

        // Creo una copia mutable del tablero para modificar solo esa casilla
        val mutableBoard = current.board.map { it.toMutableList() }.toMutableList()

        // Cambio el estado de la bandera
        mutableBoard[row][col] = cell.copy(
            hasFlag = !cell.hasFlag
        )

        // Convierto el tablero otra vez a inmutable
        val newBoard = mutableBoard.map { it.toList() }

        // Actualizo el estado con el nuevo tablero
        _uiState.value = current.copy(
            board = newBoard,
            pendingSoundEffect = GameSoundEffect.FLAG
        )
    }

    // Pausa la partida cuando la app pierde el foco
    private fun pauseGame() {
        val current = _uiState.value

        // Solo pauso si la partida sigue en curso
        if (current.status != GameStatus.PLAYING) return

        stopTimer()

        _uiState.value = current.copy(
            status = GameStatus.PAUSED,
            // Activo la capa de pausa para que el usuario pueda reanudar la partida
            showPauseOverlay = true
        )
    }

    // Reanuda la partida cuando el usuario pulsa continuar
    private fun resumeGame() {
        val current = _uiState.value

        // Solo reanudo si estaba pausada
        if (current.status != GameStatus.PAUSED) return

        _uiState.value = current.copy(
            status = GameStatus.PLAYING,
            // Oculto la capa de pausa porque el juego vuelve a estar activo
            showPauseOverlay = false
        )

        startTimer()
    }

    // Limpio el sonido pendiente después de que la UI lo haya reproducido
    private fun clearPendingSoundEffect() {
        _uiState.value = _uiState.value.copy(
            pendingSoundEffect = null
        )
    }



}