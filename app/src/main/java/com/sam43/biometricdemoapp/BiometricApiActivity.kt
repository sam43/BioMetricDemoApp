package com.sam43.biometricdemoapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_biometric_api.*

class BiometricApiActivity : AppCompatActivity() {

    private var authSuccessCount: Int = 0
    private var authErrorCount: Int = 0
    private var authFailedCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric_api)
        checkIfDeviceSupportsBiometricAuthentication()
    }

    private fun checkIfDeviceSupportsBiometricAuthentication() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            // TODO: show in-app settings, make authentication calls.
            startAuthenticationProcess()
        } else
            makeToast("Your device does not support Biometric authentication")
    }

    private fun startAuthenticationProcess() {
        button.setOnClickListener {
            instanceOfBiometricPrompt().authenticate(getPromptInfo())
        }

    }

    @SuppressLint("SetTextI18n")
    private fun instanceOfBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)
        val authCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                authErrorCount++
                runOnUiThread {
                    makeToast("Authentication ERROR")
                    textView.text = "Success: $authSuccessCount times\t\tError:$authErrorCount times\t\tFailed: $authFailedCount"
                    textView_result.text = "onAuthenticationError with \n" +
                            "errorCode: $errorCode & msg: $errString"
                }
                Log.wtf(
                    "AndroidAPI",
                    "onAuthenticationError with \nerrorCode: $errorCode & msg: $errString"
                )
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                authSuccessCount++
                runOnUiThread {
                    makeToast("Authentication SUCCEED")
                    textView.text = "Success: $authSuccessCount times\t\tError:$authErrorCount times\t\tFailed: $authFailedCount"

                    textView_result.text = "onAuthenticationSucceeded\n" +
                            "result: ${result.cryptoObject?.signature?.algorithm}"
                }
                Log.wtf("AndroidAPI", "onAuthenticationSucceeded\nresult: $result")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                authFailedCount++
                runOnUiThread {
                    makeToast("Authentication FAILED")
                    textView.text = "Success: $authSuccessCount times\t\tError:$authErrorCount times\t\tFailed: $authFailedCount"
                }
                Log.wtf("AndroidAPI", "onAuthenticationFailed")
            }
        }

        return BiometricPrompt(this, executor, authCallback)

    }

    private fun getPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("App's Authentication")
            .setSubtitle("Please login to get access")
            .setDescription("This App is using Android biometric authentication")
            .setDeviceCredentialAllowed(true)
            .build()
    }

}
