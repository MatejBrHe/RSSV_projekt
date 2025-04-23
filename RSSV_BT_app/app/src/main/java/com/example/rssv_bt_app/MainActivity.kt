package com.example.rssv_bt_app

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.rssv_bt_app.ui.theme.RSSV_BT_appTheme
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

var redVal : Int = 0
var greenVal : Int = 0
var blueVal : Int = 0

val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
lateinit var outputStream: OutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RSSV_BT_appTheme {
                MainScreen()
            }
        }
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_LONG).show()
            return
        }
        else {
            if (!bluetoothAdapter.isEnabled) {
                //Need some work
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 2)
            }

            val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
            var addr: String = ""
            for (device in pairedDevices) {
                Log.d("Bluetooth", "Paired Device: ${device.name} - ${device.address}")
                addr = device.address.toString()
            }

            if(addr != "") {
                val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(addr)
                val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

                Thread {
                    try {
                        val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                        socket.connect()

                        outputStream = socket.outputStream
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            }
        }
    }
}

fun encodeMessage(): String {
    var message="";
    if(redVal<10){ message += "00$redVal" }
    else if(redVal<100) { message += "0$redVal" }
    else { message += "$redVal" }

    if(greenVal<10){ message += "00$greenVal" }
    else if(greenVal<100) { message += "0$greenVal" }
    else { message += "$greenVal" }

    if(blueVal<10){ message += "00$blueVal" }
    else if(blueVal<100) { message += "0$blueVal" }
    else { message += "$blueVal" }

    return message
}

@Composable
fun MainScreen() {
    var sliderRedPosition by remember { mutableFloatStateOf(0f) }
    var sliderGreenPosition by remember { mutableFloatStateOf(0f) }
    var sliderBluePosition by remember { mutableFloatStateOf(0f) }

    Column(modifier = Modifier.padding(20.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //Red slider
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "0",
                modifier = Modifier.padding( horizontal = 10.dp, vertical = 15.dp),
                style = TextStyle(fontSize = 20.sp)
            )
            Slider(
                modifier = Modifier.size(height = 50.dp, width = 250.dp),
                value = sliderRedPosition,
                onValueChange = { sliderRedPosition = it
                                    redVal = (sliderRedPosition*255).toInt() },
                colors = SliderColors(
                    thumbColor = Color.Red,
                    activeTrackColor = Color.Red,
                    activeTickColor = Color.Red,
                    inactiveTrackColor = Color.Red,
                    inactiveTickColor = Color.Red,
                    disabledThumbColor = Color.Gray,
                    disabledActiveTrackColor = Color.Gray,
                    disabledActiveTickColor = Color.Gray,
                    disabledInactiveTrackColor = Color.Gray,
                    disabledInactiveTickColor = Color.Gray,
                )
            )
            Text(text = "255",
                modifier = Modifier.padding( horizontal = 10.dp, vertical = 15.dp),
                style = TextStyle(fontSize = 20.sp)
            )
        }

        //Green slider
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "0",
                modifier = Modifier.padding( horizontal = 10.dp, vertical = 15.dp),
                style = TextStyle(fontSize = 20.sp)
            )
            Slider(
                modifier = Modifier.size(height = 50.dp, width = 250.dp),
                value = sliderGreenPosition,
                onValueChange = { sliderGreenPosition = it
                                    greenVal = (sliderGreenPosition*255).toInt() },
                colors = SliderColors(
                    thumbColor = Color.Green,
                    activeTrackColor = Color.Green,
                    activeTickColor = Color.Green,
                    inactiveTrackColor = Color.Green,
                    inactiveTickColor = Color.Green,
                    disabledThumbColor = Color.Gray,
                    disabledActiveTrackColor = Color.Gray,
                    disabledActiveTickColor = Color.Gray,
                    disabledInactiveTrackColor = Color.Gray,
                    disabledInactiveTickColor = Color.Gray,
                )
            )
            Text(text = "255",
                modifier = Modifier.padding( horizontal = 10.dp, vertical = 15.dp),
                style = TextStyle(fontSize = 20.sp)
            )
        }

        //Blue slider
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "0",
                modifier = Modifier.padding( horizontal = 10.dp, vertical = 15.dp),
                style = TextStyle(fontSize = 20.sp)
            )
            Slider(
                modifier = Modifier.size(height = 50.dp, width = 250.dp),
                value = sliderBluePosition,
                onValueChange = { sliderBluePosition = it
                                    blueVal = (sliderBluePosition*255).toInt() },
                colors = SliderColors(
                    thumbColor = Color.Blue,
                    activeTrackColor = Color.Blue,
                    activeTickColor = Color.Blue,
                    inactiveTrackColor = Color.Blue,
                    inactiveTickColor = Color.Blue,
                    disabledThumbColor = Color.Gray,
                    disabledActiveTrackColor = Color.Gray,
                    disabledActiveTickColor = Color.Gray,
                    disabledInactiveTrackColor = Color.Gray,
                    disabledInactiveTickColor = Color.Gray,
                )
            )
            Text(text = "255",
                modifier = Modifier.padding( horizontal = 10.dp, vertical = 15.dp),
                style = TextStyle(fontSize = 20.sp)
            )
        }

        Text(text = "$redVal, $greenVal, $blueVal")

        Button(modifier = Modifier.padding(vertical = 5.dp),
            colors = ButtonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray
            ),
            onClick = {
                var message = encodeMessage()
                Log.v("Message", message)
                Thread {
                    try {
                        outputStream.write(message.toByteArray())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.start()
            }) {
            Text("Change")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    RSSV_BT_appTheme {
        MainScreen()
    }
}

