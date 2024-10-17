package com.example.simplify1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplify1.ui.theme.CommonBackground
import com.example.simplify1.ui.theme.Simplify1Theme
import com.example.simplify1.ui.theme.TextOrange
import com.google.android.gms.maps.GoogleMap

class DroneStatusActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Simplify1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    layoutDroneStatus()
                }
            }
        }
    }
}

@Composable
fun layoutDroneStatus(){
    val context = LocalContext.current

    // Retrieve the first name from SharedPreferences
    val firstName = SharedPreferencesManager.getFirstName(context)

    Column(
        modifier = Modifier
            .background(CommonBackground)
            .fillMaxSize()
    ) {
        Row( // row for center logo
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.reliefchain_white),
                contentDescription = "center logo for S",
                modifier = Modifier
                    .size(100.dp)
            )
        }

        Spacer(modifier = Modifier.size(46.dp))

        Box( // box for texts
            modifier = Modifier
                .padding(start = 29.dp)
        ) {
            Column {

                Text(
                    text = "WELCOME $firstName !",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = "See Your",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color(0xffffffff)
                )

                Row {
                    Text(
                        text = "DRONE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = TextOrange
                    )

                    Text(
                        text = " HERE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color.White
                    )

                }


                Text(
                    text = "To make this experience easier for you, let\nRELEIFCHAIN have access to the following.",
                    fontSize = 12.sp,
                    color = Color(0xffffffff)
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row( //row for google button

        ) {
            Image(
                painter = painterResource(id = R.drawable.drone_location_static),
                contentDescription = "static image for drone location",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(BorderStroke(8.dp, TextOrange))
            )
        }
    }
}