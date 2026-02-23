package com.example.buscaminas_2dapracticakotlinjeckpackcompose.ui.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

// ViewModel del juego.
// Aquí irá la lógica de la partida y el estado reactivo.
class GameViewModel : ViewModel(){

    // Estado interno mutable, solo lo toca el ViewModel.
    private val _uiState = MutableStateFlow(GameUiState())

    // Estado público de solo lectura para que la UI lo observe.
    val uiState: StateFlow<GameUiState> = _uiState

    init {
        // Al crear el ViewModel, dejamos el juego listo para empezar.
        startNewGame()
    }
    // Punto único de entrada de eventos desde la UI.
    // La UI no llama directamente a funciones internas.
    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.CellPressed -> onCellPressed(event.row, event.col)
            GameEvent.RestartPressed -> startNewGame()
        }
    }

    // Función interna cuando el usuario pulsa una celda.
    private fun onCellPressed(row: Int, col: Int) {

        // Leemos el estado actual.
        val current = _uiState.value

        // Si la partida no está en curso, ignoramos pulsaciones.
        if (current.status != GameStatus.PLAYING) return

        // Obtenemos la celda que el usuario ha pulsado.
        val pressedCell = current.board[row][col]

        if (pressedCell.isRevealed) return

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

            _uiState.value = current.copy(
                status = GameStatus.LOST,
                board = revealedBoard
            )

            return
        }

        // Creamos un tablero nuevo copiando el actual.
        // Solo cambiamos la celda que se ha pulsado.
        val newBoard = mutableListOf<List<CellUi>>()

        for (r in 0 until current.rows) {
            val newRow = mutableListOf<CellUi>()

            for (c in 0 until current.cols) {
                val cell = current.board[r][c]

                // Si es la celda pulsada, la marcamos como revelada.
                if (r == row && c == col) {
                    newRow.add(cell.copy(isRevealed = true))
                } else {
                    // Las demás celdas se quedan igual.
                    newRow.add(cell)
                }
            }

            newBoard.add(newRow)
        }
        // Comprobamos si ya se han revelado todas las no-mina.
        val won = checkWin(newBoard)

        // Actualizamos el estado: si ganó, cambiamos status.
        _uiState.value = current.copy(
            status = if (won) GameStatus.WON else GameStatus.PLAYING,
            board = newBoard
        )

    }
    // Función para generar posiciones aleatorias de minas sin repetir pr eso las creo en un Set Para
    // asegurarme de que no se repiten.
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


    // Inicia una partida nueva.
    private fun startNewGame() {
        // Definimos el tamaño del tablero de filas y columnas
        val rows = 3
        val cols = 3

        // Definimos la cantidad de minas que queremos en el tablero y la generamos aleatoriamente.
        val mineCount = 2
        val mines = generateRandomMines(rows, cols, mineCount)

        // Aquí creo el tablero sabiendo dónde están las minas
        val board = buildBoardWithMines(rows, cols, mines)
        // Actualizamos el estado completo que verá la UI.
        _uiState.value = GameUiState(
            rows = rows,
            cols = cols,
            status = GameStatus.PLAYING,
            elapsedSeconds = 0,
            board = board
        )
    }

    private fun buildBoardWithMines(
        rows: Int,
        cols: Int,
        mines: Set<Pair<Int, Int>>
    ): List<List<CellUi>> {

        val board = mutableListOf<List<CellUi>>()

        for (r in 0 until rows) {
            val rowList = mutableListOf<CellUi>()

            for (c in 0 until cols) {
                val isMine = mines.contains(Pair(r, c))

                rowList.add(
                    CellUi(
                        row = r,
                        col = c,
                        isMine = isMine,
                        adjacentMines = if (isMine) 0 else countAdjacentMines(r, c, mines),
                        isRevealed = false
                    )
                )
            }

            board.add(rowList)
        }

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



}