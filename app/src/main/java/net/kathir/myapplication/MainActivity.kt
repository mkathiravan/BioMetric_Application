package net.kathir.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MyLogTag"

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    private lateinit var biometricManager: BiometricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        biometricManager = BiometricManager.from(this)
        val executor = ContextCompat.getMainExecutor(this)

        checkBiometricStatus(biometricManager)

        biometricPrompt = BiometricPrompt(this,executor,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast("Authentication error $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG,"RESUOT "+ result.cryptoObject);
                    goToHomeActivity()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast("Authentication Failed")
                }
            })


        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for the app")
            .setDescription("Use device biometric feature for login to account")
            .setNegativeButtonText("use email for login")
            .build()


        login.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }




    }


    private fun showToast(message : String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }

    private fun goToHomeActivity(){
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
    }

    private fun checkBiometricStatus(biometricManager: BiometricManager){
        when(biometricManager.canAuthenticate()){
            BiometricManager.BIOMETRIC_SUCCESS->
                Log.d(TAG, "checkBiometricStatus: App can use biometric authenticate")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE->
                Log.e(TAG, "checkBiometricStatus: No biometric features available in this device")

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE->
                Log.e(TAG, "checkBiometricStatus: Biometric features currently unavailable")

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED->
                Log.e(TAG, "checkBiometricStatus: The user has'nt enrolled with any biometric configuration"+
                        " in this device")
        }
    }


}
