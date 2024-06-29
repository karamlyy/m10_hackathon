package com.example.qrcodescanner

import java.math.BigDecimal

data class PaymentRequestDTO(
    val userId: Long,
    val otp: String,
    val amount: BigDecimal
)
