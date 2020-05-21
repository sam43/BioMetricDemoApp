package com.sam43.biometricdemoapp

import android.content.Context
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.P)
/**
 * Need to work with API level 28
 * API Level 29 (Android Q)
 * And below API level 28 individually
 * this is pain full so that google has introduced
 * with their new api for biometric recognition
 * including face and finger print
 * */

class MainActivity : AppCompatActivity() {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeProperties()
    }

    private fun initializeProperties() {

        biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Fingerprint Authentication")
            .setSubtitle("Use you Fingerprint for the Authentication")
            .setDescription("Use you Fingerprint for the Authentication this is for the security purpose")
            .setNegativeButton("Cancel", executor,
                android.content.DialogInterface.OnClickListener { dialog, which -> /* Nothing TODOo*/ })
            /*.setConfirmationRequired(true)
            .setDeviceCredentialAllowed(true)*/
            .build()


        button.setOnClickListener {
            biometricPrompt.authenticate(CancellationSignal(), executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        runOnUiThread {
                            makeToast("onAuthenticationError!")
                        }
                        Log.wtf("TAG", "$errorCode & msg: $errString")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        runOnUiThread {
                            makeToast("Authentication Succeed!")
                        }
                        Log.wtf("TAG", "$result")
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                        runOnUiThread {
                            makeToast("onAuthenticationHelp")
                        }
                        Log.wtf("TAG", "$helpCode and helpStr: $helpString")
                    }

                    override fun onAuthenticationFailed() {
                        runOnUiThread {
                            makeToast("onAuthenticationFailed")
                        }
                        Log.wtf("TAG", "onAuthenticationFailed")
                    }

                })
        }
    }
}

fun Context.makeToast(msg : String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()