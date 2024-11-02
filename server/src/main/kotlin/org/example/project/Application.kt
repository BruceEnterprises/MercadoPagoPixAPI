package org.example.project

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.common.IdentificationRequest
import com.mercadopago.client.payment.PaymentClient
import com.mercadopago.client.payment.PaymentCreateRequest
import com.mercadopago.client.payment.PaymentPayerRequest
import com.mercadopago.core.MPRequestOptions
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal

/*
{
    "name": "",
    "email": "",
    "cpf": ""
}
 */
data class PixRequest(val name: String, val email: String, val cpf: String)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            call.respondText("<Hello World!!>")
        }
        post("/pix") {
            var response = "teste mercado pago"

            val pixRequest = call.receive<PixRequest>()

            val name = pixRequest.name
            val email = pixRequest.email
            val cpf = pixRequest.cpf

            createPixPayment(name, email, cpf) {
                response = it
            }
            call.respondText(response)
        }
    }
}

suspend fun createPixPayment(name: String, email: String, cpf: String, responseReturn: (String) -> Unit) {
    try {
        withContext(Dispatchers.IO) {
            val requestOptions = MPRequestOptions.builder()
                .build()

            MercadoPagoConfig.setAccessToken("")

            val client = PaymentClient()

            val paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(BigDecimal("5.00"))
                .description("Assinatura 1 ano Turtus")
                .paymentMethodId("pix")
                .payer(
                    PaymentPayerRequest.builder()
                        .email(email)
                        .firstName(name)
                        .identification(
                            IdentificationRequest.builder().type("CPF").number(cpf).build(),
                        )
                        .build(),
                )
                .build()

            val response = client.create(paymentCreateRequest, requestOptions)

            responseReturn(response.pointOfInteraction.transactionData.qrCode)
        }
    } catch (e: Exception) {
        responseReturn(e.message ?: "Erro para criar o pagamento")
    }
}
