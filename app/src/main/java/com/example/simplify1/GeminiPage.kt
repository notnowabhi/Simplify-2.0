package com.example.simplify1

import android.icu.text.ListFormatter.Width
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplify1.ui.theme.CommonBackground
import com.example.simplify1.ui.theme.TextOrange

import com.google.ai.client.generativeai.GenerativeModel //for gemini
import kotlinx.coroutines.launch

class GeminiPage : ComponentActivity() {

    private val model = GenerativeModel(
        "gemini-1.5-flash",
        "AIzaSyCD76UYBpSuuXp47td_QDFFcAlPwWwrty4",  // api key
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val geminiPromptPrefix = getString(R.string.gemini_prompt_prefix)
            val geminiPromptSuffix = getString(R.string.gemini_prompt_suffix)
            SimpleUI(model, geminiPromptPrefix, geminiPromptSuffix)
        }
    }


//    runBlocking {
//        val response = model.generateContent(prompt)
//        //tVResult.text= response.text
//    }

}

@Composable
fun SimpleUI(model: GenerativeModel, promptPrefix: String, promptSuffix: String) {
    // State variables to manage input text and displayed text
    var inputText = remember { mutableStateOf("") } // Mutable state for input text
    var displayedText = remember { mutableStateOf("") } // Mutable state for displayed text

    val coroutineScope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CommonBackground), // screen 2 = 241b38
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.reliefchain_white),
            contentDescription = "top reliefchain logo",
            modifier = Modifier.size(100.dp)
        )
    }


    Column(
        modifier = Modifier
            .padding(29.dp)
            .offset(y = 131.dp)
    ) {
        Text(
            text = "What Should",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = Color(0xffffffff)
        )

        Row {
            Text(
                text = "I DO",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color.White
            )

            Text(
                text = " NOW",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = TextOrange
            )

            Spacer(modifier = Modifier.size(8.dp))

            //textField here

        }
        Text(
            text = "Feel stuck? Here’s a few things to keep in mind\nas we bring help to you.",
            fontSize = 12.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {



            //textField here
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, TextOrange, RoundedCornerShape(12.dp)),
                value = inputText.value, // Access the current value of inputText
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0x60E75C26),
                    focusedContainerColor = Color(0xff522da2),
                    focusedIndicatorColor = Color.Transparent, // Focused underline color
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xff73ec8b), // Cursor color
                    focusedTextColor = Color.White, // Text color when focused
                    unfocusedTextColor = Color(0xff73ec8b)
                ),
                onValueChange = {
                    inputText.value = it // Update inputText with the new value
                    //displayedText.value = it // Update displayedText with the new value
                },
                label = {
                    Text(
                        text = "Emergency?",
                        color = Color.White
                    )
                } // Use Text composable for label
            )

            Spacer(modifier = Modifier.size(16.dp))

            //Button here
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        val fullPrompt = promptPrefix + inputText.value + promptSuffix
                        // Get the response from the Gemini model
                        val response =
                            generateContentFromGemini(model, fullPrompt) //use the prefix here
                        // Update displayedText with the response
                        displayedText.value = response
                    }

                },
                colors = ButtonDefaults.buttonColors(containerColor = TextOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Get Tips Now!")
            }

        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = displayedText.value,
            color = Color.White
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Text view to display the submitted text
            // response msg
        }
    }

    // Layout setup with vertical arrangement and spacing

}

suspend fun generateContentFromGemini(model: GenerativeModel, prompt: String): String {
    try {
        // Assume you have access to the model instance from GeminiPage
        val response = model.generateContent(prompt)
        return response.text
            ?: "No response received"// Assuming `response.text` contains the response text
    } catch (e: Exception) {
        // Handle any exceptions that occur during the network call
        return "Failed to get response: ${e.message}"
    }
}
