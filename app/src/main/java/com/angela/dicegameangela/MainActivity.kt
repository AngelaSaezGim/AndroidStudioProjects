package com.angela.dicegameangela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angela.dicegameangela.ui.theme.DiceGameAngelaTheme
import com.angelasaez.diceandroidangela.model.Dice

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceGameAngelaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Content(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {

    // Saveable - para que cuando cambie configuración no se cambie
    var dicePoints by rememberSaveable { mutableIntStateOf(20) }
    var startGame by rememberSaveable { mutableStateOf(false) }

    //tiradas (la ultima (comparar) y la actual)
    var diceThrowed by rememberSaveable { mutableIntStateOf(0) }
    var nextdiceThrowed by rememberSaveable { mutableIntStateOf(0) }

    //Mensaje que se cambiará para simular lanzamiento de dado
    var messageDiceThrowing by rememberSaveable { mutableStateOf("Lanzando dado...") }
    var buttonsEnabled by rememberSaveable { mutableStateOf(true) } //para la espera del lanzamiento

    // Pop-ups para informar al jugador si pierde o gana puntos
    var showPopup by rememberSaveable { mutableStateOf(false) }
    var popupMessage by rememberSaveable { mutableStateOf("") }

    //Colores (xml)
    val colorButtons = colorResource(id = R.color.color_buttons)
    val colorButtons2 = colorResource(id = R.color.color_buttons2)

    val colorButtonsDisabled = colorResource(id = R.color.color_buttons_disabled)
    val colorTxt1 = colorResource(id = R.color.default_text) //Negro
    val colorTxt2 = colorResource(id = R.color.default_text2) //Blanco

    val gradientColors = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.gradient_start), colorResource(id = R.color.gradient_end)
        )
    )

    //OBJETO DADO
    val dice = Dice() // 6 caras por defecto

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientColors)
            .then(modifier),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        //FILA - TITULO JUEGO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            Text(
                text = "Dice Game",
                fontSize = 30.sp,
                color = colorTxt1
            )
        }
        //FIN FILA TITULO JUEGO
        //FILA DESCRIPCIÓN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .border(1.dp, Color.Black)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            Text(
                text = "Deberás predecir el numero aleatorio que va a salir. Ya sea directamente (1-6) o guiandote por el número que salió en el dado (↑ o ↓) .",
            )
        } //FIN FILA DESCRIPCIÓN

        if (startGame) {
            //COLUMNA = [JUEGO]
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                //FILA PUNTOS
                Row(
                    //organizacion horizontal
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    if (dicePoints == 0) {
                        //Uso box para poder centrarlo cuando salga GAME OVER, ya que ocupa todo el ancho de la fila
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = {
                                    startGame = false
                                    dicePoints = 20
                                    diceThrowed = 0
                                    dice.reset() // uso objeto dice para resetear dado
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.button_restartstart_background),
                                    contentColor = colorTxt2
                                ),
                                shape = CircleShape,
                                modifier = Modifier
                                    .border(1.dp, Color.White, CircleShape)
                                    .shadow(2.dp, CircleShape)
                            ) {
                                Text(
                                    text = "VOLVER A EMPEZAR",
                                    fontSize = 25.sp,
                                    modifier = Modifier.padding(5.dp),
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.End,
                    ) {
                        if (dicePoints > 0) {
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color.Black)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "$dicePoints pts",
                                    fontSize = 20.sp,
                                    color = colorResource(id = R.color.points_text)
                                )
                            }
                        }
                    }
                } //FIN FILA PUNTOS
            }
            //FILA DADO
            if (dicePoints > 0) {
                Row(
                    //organizacion horizontal
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = messageDiceThrowing,
                        fontSize = 15.sp,
                    )
                }
            }
            Row(
                //organizacion horizontal
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (dicePoints <= 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color.Black)
                            .shadow(2.dp)
                            .padding(30.dp)
                            .width(50.dp)
                            .height(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "GAME OVER",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.Center),
                            color = colorResource(id = R.color.game_over_text)
                        )
                    }
                } else {
                    // Contenedor para centrar el box de diceThrowed
                    Box(
                        modifier = Modifier
                            .border(2.dp, Color.Black)
                            .shadow(2.dp)
                            .padding(40.dp)
                            .width(120.dp)
                            .height(80.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$diceThrowed",
                            fontSize = 30.sp,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            //FILA - BOTONES NUMEROS DEL 1 AL 6
            Row(
                //organizacion horizontal
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                //He hecho bucles para las 2 filas para ahorrar codigo
                for (i in 1..4) {
                    Button(
                        onClick = {
                            dicePoints -= 3
                            buttonsEnabled = false
                            messageDiceThrowing = "Lanzando dado..."
                            Thread {
                                Thread.sleep(1000L)
                                nextdiceThrowed = dice.throwDice() //Se tira dado
                                messageDiceThrowing = "¡Salió $nextdiceThrowed!"
                                if (dicePoints <= 0) {
                                    dicePoints = 0
                                    popupMessage = "Has gastado todos los puntos, has perdido"
                                    showPopup = true
                                } else {
                                    //Se compara
                                    if (nextdiceThrowed == i) {
                                        dicePoints += 15
                                        popupMessage =
                                            "¡Has adivinado el número! Ganaste 15 puntos."
                                    } else {
                                        popupMessage = "No lo adivinaste, perdiste 3 puntos."
                                    }
                                    showPopup = true
                                }
                                buttonsEnabled = true
                                diceThrowed = nextdiceThrowed //Lo vemos
                            }.start()
                        },
                        enabled = dicePoints > 0 && buttonsEnabled, //habilitación boton (mas de 0 puntos y tiempo espera)
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorButtons,
                            contentColor = colorTxt2,
                            disabledContainerColor = colorButtonsDisabled,
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .shadow(4.dp, CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    ) {
                        Text(text = "$i", fontSize = 20.sp, modifier = Modifier.padding(2.dp))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
            //FIN FILA
            Row(
                //organizacion horizontal
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                for (i in 5..6) {
                    Button(
                        onClick = {
                            dicePoints -= 3
                            buttonsEnabled = false
                            messageDiceThrowing = "Lanzando dado..."
                            Thread {
                                Thread.sleep(1000L)
                                nextdiceThrowed = dice.throwDice() //Se tira dado
                                messageDiceThrowing = "¡Salió $nextdiceThrowed!"
                                if (dicePoints <= 0) {
                                    dicePoints = 0
                                    popupMessage = "Has gastado todos los puntos, has perdido"
                                    showPopup = true
                                } else {
                                    //Se compara
                                    if (nextdiceThrowed == i) {
                                        dicePoints += 15
                                        popupMessage =
                                            "¡Has adivinado el número! Ganaste 15 puntos."
                                    } else {
                                        popupMessage = "No lo adivinaste, perdiste 3 puntos."
                                    }
                                    showPopup = true
                                }
                                buttonsEnabled = true
                                diceThrowed = nextdiceThrowed //Lo vemos
                            }.start()
                        },
                        enabled = dicePoints > 0 && buttonsEnabled, //habilitación boton (mas de 0 puntos y tiempo espera)
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorButtons,
                            contentColor = colorTxt2,
                            disabledContainerColor = colorButtonsDisabled
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .shadow(4.dp, CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    ) {
                        Text(text = "$i", fontSize = 20.sp, modifier = Modifier.padding(2.dp))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            //FILA - BOTONES MAYOR Y MENOR
            Row(
                //organizacion horizontal
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Text(
                    text = "Será el siguiente número mayor o menor que $diceThrowed ?"
                )
            } //FIN FILA
            Row(
                //organizacion horizontal
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Button(
                    onClick = {
                        dicePoints -= 1
                        buttonsEnabled = false
                        messageDiceThrowing = "Lanzando dado..."
                        Thread {
                            Thread.sleep(1000L)
                            nextdiceThrowed = dice.throwDice() //Se tira dado
                            messageDiceThrowing = "¡Salió $nextdiceThrowed!"
                            if (dicePoints <= 0) {
                                dicePoints = 0
                                popupMessage = "Has gastado todos los puntos, has perdido"
                                showPopup = true
                            } else {
                                //Se compara
                                if (nextdiceThrowed > diceThrowed) {
                                    dicePoints += 3
                                    popupMessage = "¡Correcto! Has ganado 3 puntos."
                                } else {
                                    popupMessage = "Incorrecto, has perdido 1 punto."
                                }
                                showPopup = true
                            }
                            buttonsEnabled = true
                            diceThrowed = nextdiceThrowed //Lo vemos
                        }.start()
                    },
                    enabled = dicePoints > 0 && buttonsEnabled, //habilitación boton (mas de 0 puntos y tiempo espera)
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorButtons2,
                        contentColor = colorTxt2,
                        disabledContainerColor = colorButtonsDisabled
                    ),
                    shape = CircleShape,
                    modifier = Modifier
                        .shadow(8.dp, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                ) {
                    Text(
                        text = "↑", fontSize = 20.sp, modifier = Modifier.padding(2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    onClick = {
                        dicePoints -= 1
                        buttonsEnabled = false
                        messageDiceThrowing = "Lanzando dado..."
                        Thread {
                            Thread.sleep(1000L)
                            nextdiceThrowed = dice.throwDice() //Se tira dado
                            messageDiceThrowing = "¡Salió $nextdiceThrowed!"
                            if (dicePoints <= 0) {
                                dicePoints = 0
                                popupMessage = "Has gastado todos los puntos, has perdido"
                                showPopup = true
                            }else{
                                //Se compara
                                if (nextdiceThrowed < diceThrowed) {
                                    dicePoints += 3
                                    popupMessage = "¡Correcto! Has ganado 3 puntos."
                                } else {
                                    popupMessage = "Incorrecto, has perdido 1 punto."
                                }
                                showPopup = true
                            }
                            buttonsEnabled = true
                            diceThrowed = nextdiceThrowed //Lo vemos
                        }.start()
                    },
                    enabled = dicePoints > 0 && buttonsEnabled, //habilitación boton (mas de 0 puntos y tiempo espera)
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorButtons2,
                        contentColor = colorTxt2,
                        disabledContainerColor = colorButtonsDisabled
                    ),
                    shape = CircleShape,
                    modifier = Modifier
                        .shadow(8.dp, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                ) {
                    Text(
                        text = "↓", fontSize = 20.sp, modifier = Modifier.padding(2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Text(
                    text = "Mayor"
                )
                Spacer(modifier = Modifier.width(25.dp))
                Text(
                    text = "Menor"
                )
            }
            //FIN FILA JUEGO
        } else {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                //organizacion horizontal
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        startGame = true

                        //LANZAMIENTO INICIAL DEL DADO
                        buttonsEnabled = false
                        messageDiceThrowing = "Lanzando dado..."
                        Thread {
                            Thread.sleep(1000L)  // 1 segundo
                            diceThrowed = dice.throwDice() // método de la clase Dice
                            messageDiceThrowing = "¡Salió $diceThrowed!"
                            buttonsEnabled = true
                        }.start()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorButtons2,
                        contentColor = colorTxt2
                    ),
                    shape = CircleShape,
                    modifier = Modifier
                        .shadow(8.dp, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                ) {
                    Text(
                        text = "COMENZAR JUEGO", fontSize = 20.sp, modifier = Modifier.padding(2.dp)
                    )
                }
            }
        }
        //FILA - NOMBRE
        Row(
            //organizacion horizontal
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.Bottom

        ) {
            Text(
                text = "Ángela", fontSize = 15.sp
            )
        } //FIN FILA NOMBRE

        //Configuración pop-up
        if (showPopup) {
            AlertDialog(onDismissRequest = { showPopup = false }, // Ocultar el pop-up al cerrarlo
                        title = { Text(text = "Resultado Dado") },
                        text = { Text(text = popupMessage) },
                        confirmButton = {
                            Button(onClick = { showPopup = false }) { Text("OK") }
                        })
        }
    }
}