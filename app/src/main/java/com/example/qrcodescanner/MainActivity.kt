package com.example.qrcodescanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcodescanner.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start QR code scanning on button click
        binding.buttonScan.setOnClickListener {
            val amountText = binding.editTextAmount.text.toString()
            if (amountText.isBlank()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            } else {
                startQRScanner()
            }
        }
    }

    private fun startQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.setCaptureActivity(CustomScannerActivity::class.java)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                binding.textViewResult.text = "Cancelled"
            } else {
                val scannedText = result.contents
                try {
                    val qrCodeScanDTO = parseJsonToQRCodeScanDTO(scannedText)
                    binding.textViewResult.text = "Parsed QRCodeScanDTO: $qrCodeScanDTO"
                    sendPaymentRequest(qrCodeScanDTO)
                } catch (e: JsonSyntaxException) {
                    binding.textViewResult.text = "Invalid JSON format"
                } catch (e: Throwable) {
                    binding.textViewResult.text = "Error: ${e.message}"
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun parseJsonToQRCodeScanDTO(json: String): QRCodeScanDTO {
        val gson = Gson()
        return gson.fromJson(json, QRCodeScanDTO::class.java)
    }

    private fun sendPaymentRequest(qrCodeScanDTO: QRCodeScanDTO) {
        val amountText = binding.editTextAmount.text.toString()
        val amount = BigDecimal(amountText)
        val paymentRequestDTO = PaymentRequestDTO(
            userId = qrCodeScanDTO.userId,
            otp = qrCodeScanDTO.otp,
            amount = amount
        )

        RetrofitClient.instance.sendPaymentRequest(paymentRequestDTO)
            .enqueue(object : Callback<TransactionDTO> {
                override fun onResponse(call: Call<TransactionDTO>, response: Response<TransactionDTO>) {
                    if (response.isSuccessful) {
                        binding.textViewResult.text = "Payment sent successfully"
                        Toast.makeText(this@MainActivity, "Payment sent successfully", Toast.LENGTH_SHORT).show()
                        // Start SuccessActivity
                        startActivity(Intent(this@MainActivity, SuccessActivity::class.java))
                    } else {
                        binding.textViewResult.text = "Failed to send payment request"
                        Toast.makeText(this@MainActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TransactionDTO>, t: Throwable) {
                    binding.textViewResult.text = t.message
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
