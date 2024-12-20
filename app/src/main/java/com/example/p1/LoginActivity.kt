package com.example.p1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var checkBoxRememberMe: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialiser les vues
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewForgotPassword = findViewById<TextView>(R.id.textViewForgotPassword)
        val textViewCreateAccount = findViewById<TextView>(R.id.textViewCreateAccount)
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe)

        // Initialiser SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)

        // Vérifier si les informations sont stockées et les pré-remplir
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")
        val isRemembered = sharedPreferences.getBoolean("rememberMe", false)

        if (isRemembered) {
            editTextEmail.setText(savedUsername)
            editTextPassword.setText(savedPassword)
            checkBoxRememberMe.isChecked = true
        }

        val clientId = "kotlin-client"
        val grantType = "password"
        val clientSecret = "t3bCmItJ78btBIVJVuzszPJYJBB35oWG"
        val kscope = "openid"
        val scope = CoroutineScope(Dispatchers.Main)

        buttonLogin.setOnClickListener {
            val usernameT = editTextEmail.text.toString()
            val passwordT = editTextPassword.text.toString()

            if (usernameT.isEmpty() || passwordT.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enregistrer les informations si la case est cochée
            if (checkBoxRememberMe.isChecked) {
                val editor = sharedPreferences.edit()
                editor.putString("username", usernameT)
                editor.putString("password", passwordT)
                editor.putBoolean("rememberMe", true)
                editor.apply()
            } else {
                // Ne pas enregistrer si la case n'est pas cochée
                val editor = sharedPreferences.edit()
                editor.remove("username")
                editor.remove("password")
                editor.remove("rememberMe")
                editor.apply()
            }

            scope.launch {
                try {
                    val response = ApiClient.login().getAccessToken(
                        clientId, grantType, clientSecret, kscope, usernameT, passwordT
                    )

                    if (response.isSuccessful && response.body() != null) {
                        val accessToken = response.body()?.accessToken.orEmpty()
                        val providerId = response.body()?.idToken.orEmpty() // Assume `idToken` contains the providerId

                        Log.i("LoginActivity", "Access Token: $accessToken, Provider ID: $providerId")
                        Toast.makeText(this@LoginActivity, "Connexion réussie", Toast.LENGTH_SHORT).show()

                        // Pass the providerId to ServicesActivity
                        val intent = Intent(this@LoginActivity, ServicesActivity::class.java)
                        intent.putExtra("providerId", providerId)
                        intent.putExtra("accessToken", accessToken)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Échec de la connexion", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Error: ${e.message}")
                    Toast.makeText(this@LoginActivity, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        textViewForgotPassword.setOnClickListener {
            Toast.makeText(this, "Mot de passe oublié cliqué", Toast.LENGTH_SHORT).show()
        }

        textViewCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivityy::class.java))
        }
    }
}
