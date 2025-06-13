package com.mera.babycare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val googleSignInButton = findViewById<Button>(R.id.googleSignInButton)
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    private fun signInWithGoogle() {
        CoroutineScope(Dispatchers.Main).launch {
            val signInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(serverClientId = getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val credentialManager = CredentialManager.create(this@MainActivity)

            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@MainActivity
                )

                // Aqui você pode pegar o ID Token e autenticar no Firebase
                Log.d("GoogleSignIn", "Credencial recebida: ${result.credential}")

            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Erro ao fazer login com Google", e)
            }
        }
    }
}
