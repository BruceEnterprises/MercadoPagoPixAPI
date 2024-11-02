package org.example.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Composable
fun App() {
    MaterialTheme {
        PaymentForm()
    }
}

@Composable
fun PaymentForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        if (responseText.isNotEmpty()) {
            Text(
                text = responseText,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            )
        }

        if (responseText.isEmpty()) {
            Text(
                text = "Atenção! Digite o CPF válido ou não será possível gerar a chave pix",
                color = Color.Red,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            name = "bruce"
            email = "teste@email.com"

            TextField(
                value = cpf,
                onValueChange = { cpf = it },
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            var isLoading by remember { mutableStateOf(false) }

            @Serializable
            data class PixPaymentRequest(val name: String, val email: String, val cpf: String)

            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Green,
                    disabledContentColor = Color.Green,
                ),
                onClick = {
                    scope.launch {
                        val client = HttpClient {
                            install(ContentNegotiation) {
                                json()
                            }
                        }
                        isLoading = true
                        try {
                            val pix = client.post("https://mediummercadopagoapi.rj.r.appspot.com/pix") {
                                contentType(io.ktor.http.ContentType.Application.Json)
                                setBody(PixPaymentRequest(name, email, cpf))
                            }.body<String>()
                            responseText = pix
                        } catch (e: Exception) {
                            responseText = "Erro: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text(
                        "Assinar",
                        color = Color.White,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
