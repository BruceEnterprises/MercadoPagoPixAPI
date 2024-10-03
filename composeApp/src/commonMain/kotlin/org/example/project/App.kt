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
import kotlinx.coroutines.launch

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
    var responseText2 by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        if (responseText2.isEmpty()) {
            Text(
                text = "Atenção! Digite o CPF válido ou não será possível gerar a chave pix",
                color = Color.Red,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.ExtraLight,
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

            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Green,
                    disabledContentColor = Color.Green,
                ),
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {

                        } catch (e: Exception) {
                            responseText2 = "Erro: ${e.message}"
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
