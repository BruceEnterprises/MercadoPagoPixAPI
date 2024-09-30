package org.example.project

import com.mercadopago.MercadoPagoConfig
import com.mercadopago.client.common.IdentificationRequest
import com.mercadopago.client.payment.PaymentClient
import com.mercadopago.client.payment.PaymentCreateRequest
import com.mercadopago.client.payment.PaymentPayerRequest
import com.mercadopago.core.MPRequestOptions
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    routing {
        get("/") {
            call.respondText("<Hello World!>")
        }
    }
}

suspend fun createPixPayment(name: String, email: String, cpf: String, function: (String) -> Unit) {
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

            function(response.pointOfInteraction.transactionData.qrCode)
        }
    } catch (e: Exception) {
        println("Erro ao iniciar pagamento: ${e.message}")
        function(e.message!!)
    }
}
