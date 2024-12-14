package com.angelasaez.diceandroidangela.model

//Constructor primario que recibe el número de caras que tiene el dado.
class Dice(faces: Int) {

    var faces: Int = faces
        private set

    var quantityOfThrows: Int = 0
        private set;

    val times: IntArray = IntArray(faces) { 0 }
    val numbers: MutableList<Int> = mutableListOf()

    //Constructor secundario que no recibe ningun parámetro y asigna a faces valor 6
    constructor() : this(6)

    /**
     * Función que simula el lanzamiento del dado actual y suma los lanzamientos
     * @return resultado aleatorio entre 1 y el número de caras del dado
     */
    fun throwDice(): Int {
        quantityOfThrows++
        val value = (1..faces).random()
        times[value - 1]++
        numbers.add(value) //se añade valor al array del historial de numeros que han salido
        return value // se devuelve numero aleatorio entre 1 y el numero de faces (parámetro)
    }

    /**
    \* Función estática que simula el lanzamiento de un dado con un número específico de caras
    \* @param faces número de caras del dado
    \* @return número aleatorio entre 1 y el número de caras
     */
    companion object {
        fun throwDiceNFaces(faces: Int): Int {
            return (1..faces).random()
        }
    }

    /*
    *
    * Funcion que muestra la info del dado en ese momento
    * (las caras, las tiradas, y el historial de tiradas hasta el momento)
    */
    fun showRecord() {
        var infoRecord = ""
        if (quantityOfThrows>0) {
            infoRecord += "Historial de tiradas\n   "
            for (number in numbers) {
                infoRecord += "${number} "
            }
        }
        println("""|Dado de $faces caras
                   |Se ha tirado $quantityOfThrows veces
                   |$infoRecord
                """.trimMargin())
    }


    /**
     * Función que resetea TODO (las tiradas y el historial de numeros que se han sacado)
     * @return numero de caras del dado
     */

    fun reset() {
        quantityOfThrows = 0
        numbers.clear()
    }

    /**
     * Función que nos da la información del objeto dado
     * @return String con número de caras, cantidad de lanzamientos, y cantidad de veces que la cara ha salido.
     */
    override fun toString(): String {
        var quantityOfEachFace = ""
        for ((index, value) in times.withIndex()) {
            quantityOfEachFace += "   La cara ${index + 1} ha salido $value veces\n"
        }
        return """|Dado de $faces caras
                  |Se ha tirado $quantityOfThrows veces
                  |Información de cada cara:
                  |$quantityOfEachFace
                  """.trimMargin()
    }
}