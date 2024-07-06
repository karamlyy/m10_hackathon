package com.example.qrcodescanner.model

import java.math.BigDecimal

data class PaymentRequestDTO(
    val userId: Long,
    val otp: String,
    val amount: BigDecimal
)
