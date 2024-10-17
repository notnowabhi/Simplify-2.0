package com.example.simplify1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplify1.ui.theme.Simplify1Theme
import com.example.simplify1.ui.theme.CommonBackground
import com.example.simplify1.ui.theme.TextOrange
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var userDao: UserDataDao
    private lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var fetchedCityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseSingleton.getDatabase(applicationContext)
        userDao = database.userDataDao()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchCityName()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            Simplify1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    layoutMain(userDao)
                }
            }
        }
    }

    fun fetchCityName() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                fetchedCityName = addresses?.get(0)?.locality ?: "Unknown"

                // Store the fetched city name in SharedPreferences
                SharedPreferencesManager.storeLocationName(applicationContext, fetchedCityName!!)

                Toast.makeText(this, "City: $fetchedCityName", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun getFetchedCityName(): String? {
        return fetchedCityName
    }

    fun addUser(firstName: String, lastName: String, phoneNumber: String, lastLocation: String, userDao: UserDataDao) {
        val newUser = Users(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            lastLocation = lastLocation,
            id = 0
        )

        CoroutineScope(Dispatchers.IO).launch {
            userDao.upsertLocation(newUser)

            // Store the first name in SharedPreferences
            SharedPreferencesManager.storeFirstName(applicationContext, firstName)
        }
    }
}

@Composable
fun layoutMain(userDao: UserDataDao) {
    val context = LocalContext.current
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val phoneNumber = remember { mutableStateOf(TextFieldValue("")) }
    val city = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CommonBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.reliefchain_white),
            contentDescription = "top reliefchain logo",
            modifier = Modifier.size(100.dp)
        )
    }

    Column(
        modifier = Modifier.offset(x = 29.dp, y = 162.dp)
    ) {
        Box {
            Column {
                Text(
                    text = "Fill your",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color.White
                )

                Row {
                    Text(
                        text = "DETAILS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = TextOrange
                    )

                    Text(
                        text = " UP",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color.White
                    )
                }

                Text(
                    text = "Just one quick task before we get you started!",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                Text(
                    text = "Please enter your Name!",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    modifier = Modifier
                        .border(2.dp, TextOrange, RoundedCornerShape(12.dp)),
                    value = name.value,
                    onValueChange = { name.value = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xff24b3d),
                        focusedContainerColor = Color(0xff522da2),
                        cursorColor = Color(0xff73ec8b),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xff73ec8b)
                    )
                )

                Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = "Please enter your Phone Number!",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    modifier = Modifier
                        .border(2.dp, TextOrange, RoundedCornerShape(12.dp)),
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xff24b3d),
                        focusedContainerColor = Color(0xff522da2),
                        cursorColor = Color(0xff73ec8b),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xff73ec8b),

                    )
                )

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        (context as MainActivity).fetchCityName()
                        city.value = (context as MainActivity).getFetchedCityName() ?: "Unknown"
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TextOrange)
                ) {
                    Text("Fetch City")
                }
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.padding(16.dp)
    ) {
        ExtendedFloatingActionButton(
            onClick = {
                val firstName = name.value.text.split(" ").getOrNull(0) ?: ""
                val lastName = name.value.text.split(" ").getOrNull(1) ?: ""
                val phoneNumberValue = phoneNumber.value.text
                val lastLocation = city.value

                (context as MainActivity).addUser(firstName, lastName, phoneNumberValue, lastLocation, userDao)

                val intent = Intent(context, SOSActivity::class.java)

                //val intent = Intent(context, GetLocationPage::class.java) //this was just the check

                context.startActivity(intent)
            },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "plus icon for fab"
                )
            },
            text = { Text(text = "NEXT") },
            containerColor = TextOrange,
            contentColor = Color.Black
        )
    }
}
