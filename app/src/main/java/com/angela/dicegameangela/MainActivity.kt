package com.angela.dicegameangela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
    val messageStr1 = stringResource(R.string.throwing_message)
    var messageDiceThrowing by rememberSaveable {mutableStateOf(messageStr1) }
    val noPointsMessage = stringResource(R.string.no_points_message)
    val winGuessMessage = stringResource(R.string.win_guess_message)
    val lostGuessMessage = stringResource(R.string.lost_guess_message)
    val winCompareMessage = stringResource(R.string.win_compare_message)
    val lostCompareMessage = stringResource(R.string.lost_compare_message)

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
        Spacer(modifier = Modifier.height(5.dp))
        //FILA - TITULO JUEGO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            Text(
                text = stringResource(R.string.tittle_game),
                fontSize =  dimensionResource(id = R.dimen.fontsize_big).value.sp,
                color = colorTxt1
            )
        }
        //FIN FILA TITULO JUEGO
        //FILA DESCRIPCIÓN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(1.dp, Color.Black)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            Text(
                text = stringResource(R.string.description_game),
                fontSize = 15.sp
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
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
                                    text = stringResource(R.string.reset_game),
                                    fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
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
                            Row(
                                modifier = Modifier
                                    .border(1.dp, Color.Black)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "$dicePoints pts",
                                    fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
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
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (dicePoints <= 0) {
                    Text(
                        fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = Color.Black,
                            )
                            .padding(50.dp)
                        ,
                        text = stringResource(R.string.game_over),
                        color = colorResource(id = R.color.game_over_text)
                    )
//                    }
                } else {
                    // Contenedor para centrar el box de diceThrowed
                    Row(
                        modifier = Modifier
                            .border(2.dp, Color.Black)
                            .shadow(2.dp)
                            .padding(60.dp)
                            .width(50.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$diceThrowed",
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text= "Aqui irán los mensajes del dado.",
                    fontSize = dimensionResource(id = R.dimen.fontsize_small).value.sp,
                )
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
                            messageDiceThrowing = messageStr1
                            Thread {
                                Thread.sleep(1000L)
                                nextdiceThrowed = dice.throwDice() //Se tira dado
                                messageDiceThrowing = ""
                                if (dicePoints <= 0) {
                                    dicePoints = 0
                                    popupMessage = noPointsMessage
                                    showPopup = true
                                } else {
                                    //Se compara
                                    if (nextdiceThrowed == i) {
                                        dicePoints += 15
                                        popupMessage = winGuessMessage
                                    } else {
                                        popupMessage = lostGuessMessage
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
                        Text(text = "$i",
                             fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
                             modifier = Modifier.padding(2.dp))
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
                            messageDiceThrowing = messageStr1
                            Thread {
                                Thread.sleep(1000L)
                                nextdiceThrowed = dice.throwDice() //Se tira dado
                                messageDiceThrowing = ""
                                if (dicePoints <= 0) {
                                    dicePoints = 0
                                    popupMessage = noPointsMessage
                                    showPopup = true
                                } else {
                                    //Se compara
                                    if (nextdiceThrowed == i) {
                                        dicePoints += 15
                                        popupMessage = winGuessMessage
                                    } else {
                                        popupMessage = lostGuessMessage
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
                        Text(text = "$i",
                             fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
                             modifier = Modifier.padding(2.dp))
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
                    text = stringResource(R.string.explication_compare, diceThrowed)
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
                        messageDiceThrowing = messageStr1
                        Thread {
                            Thread.sleep(1000L)
                            nextdiceThrowed = dice.throwDice() //Se tira dado
                            messageDiceThrowing = ""
                            if (dicePoints <= 0) {
                                dicePoints = 0
                                popupMessage = noPointsMessage
                                showPopup = true
                            } else {
                                //Se compara
                                if (nextdiceThrowed > diceThrowed) {
                                    dicePoints += 3
                                    popupMessage = winCompareMessage
                                } else {
                                    popupMessage = lostCompareMessage
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
                        text = "↑",
                        fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    onClick = {
                        dicePoints -= 1
                        buttonsEnabled = false
                        messageDiceThrowing = messageStr1
                        Thread {
                            Thread.sleep(1000L)
                            nextdiceThrowed = dice.throwDice() //Se tira dado
                            messageDiceThrowing = ""
                            if (dicePoints <= 0) {
                                dicePoints = 0
                                popupMessage = noPointsMessage
                                showPopup = true
                            }else{
                                //Se compara
                                if (nextdiceThrowed < diceThrowed) {
                                    dicePoints += 3
                                    popupMessage = winCompareMessage
                                } else {
                                    popupMessage = lostCompareMessage
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
                        text = "↓",
                        fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Text(
                    text = stringResource(R.string.major)
                )
                Spacer(modifier = Modifier.width(25.dp))
                Text(
                    text = stringResource(R.string.minor)
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
                        messageDiceThrowing = messageStr1
                        Thread {
                            Thread.sleep(1000L)  // 1 segundo
                            diceThrowed = dice.throwDice() // método de la clase Dice
                            messageDiceThrowing = ""
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
                        text = stringResource(R.string.start_game),
                        fontSize = dimensionResource(id = R.dimen.fontsize_medium).value.sp,
                        modifier = Modifier.padding(2.dp)
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
                text = "Ángela",
                fontSize = dimensionResource(id = R.dimen.fontsize_small).value.sp
            )
        } //FIN FILA NOMBRE

        //Configuración pop-up
        if (showPopup) {
            AlertDialog(onDismissRequest = { showPopup = false }, // Ocultar el pop-up al cerrarlo
                        title = { Text(text = "Resultado") },
                        text = { Text(text = popupMessage) },
                        confirmButton = {
                            Button(onClick = { showPopup = false }) { Text("OK") }
                        })
        }
    }
}
