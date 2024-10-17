package com.example.simplify1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplify1.ui.theme.CommonBackground
import com.example.simplify1.ui.theme.Simplify1Theme
import com.example.simplify1.ui.theme.TextOrange
import kotlinx.coroutines.launch

class SOSActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Simplify1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SOSLayout()
                    //ShowLocationInfo()
                }
            }
        }
    }
}

@Composable
fun SOSLayout() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val helpRequirement = remember { mutableStateOf(TextFieldValue("")) }
    val helpMessage : String = helpRequirement.value.text

    // Retrieve the first name from SharedPreferences
    val firstName = SharedPreferencesManager.getFirstName(context)

    // Use the firstName if it's available
    LaunchedEffect(firstName) {
        if (firstName != null) {
            println("User's First Name: $firstName")
            // Optionally show a Snackbar or update UI with the firstName
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Welcome, $firstName!",
                    actionLabel = "Dismiss"
                )
            }
        }
    }

    // Function to show the Snackbar when the SOS button is clicked
    fun onSOSButtonClick() {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = "SOS Activated! [$helpMessage]",
                actionLabel = "Dismiss"
            )
        }
    }
    
    fun onGeminiRouteButtonClick(){
        val intent = Intent(context, GeminiPage::class.java)
        context.startActivity(intent)
    }
    
    fun onCheckDroneStatusButtonClick(){
        val intent = Intent(context, DroneStatusActivity::class.java) //TODO change landing page
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .background(CommonBackground)
            .fillMaxSize()
    ) {
        // Center logo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.reliefchain_white),
                contentDescription = "center logo for S",
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.size(46.dp))

        // Box for texts
        Box(
            modifier = Modifier.padding(start = 29.dp)
        ) {
            Column {

                Text(
                    text = "WELCOME $firstName !",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = "Uh-Oh !",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color.White
                )

                Row {
                    Text(
                        text = "NEED ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color.White
                    )

                    Text(
                        text = "HELP?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = TextOrange
                    )
                }

                Text(
                    text = "Feel the need to send a SOS? We’ve got you covered!",
                    fontSize = 12.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    modifier = Modifier
                        .border(2.dp, TextOrange, RoundedCornerShape(12.dp)),
                    value = helpRequirement.value,
                    onValueChange = { helpRequirement.value = it },
                    label = {
                        Text(
                            text = "WHAT DISASTER ARE YOU FACING?",
                            color = Color.White
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0x60E75C26),
                        focusedContainerColor = Color(0xff522da2),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xff73ec8b),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xff73ec8b)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.size(32.dp))

        // SOS Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onSOSButtonClick() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = TextOrange),
                modifier = Modifier
                    .size(200.dp)
                    .border(8.dp, Color.White, CircleShape)
            ) {
                Text(
                    text = "SOS",
                    color = Color.White,
                    fontSize = 48.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.size(16.dp))
        
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Button(
                onClick = { onGeminiRouteButtonClick() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TextOrange)
            ) {
                Text(text = "Need Checklist?")
            }
            
            Button(
                onClick = { onCheckDroneStatusButtonClick() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TextOrange)
            ) {
                Text(text = "Drone Status")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // SnackbarHost to show the Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
