package dev.jj.myapplication.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dev.jj.myapplication.MainActivity
import dev.jj.myapplication.R

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencias a los elementos de la UI
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Configurar listener para el botón de login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Inicio de sesión exitoso, navegar a la actividad principal
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // Error en la autenticación
                        Toast.makeText(this, "Error en la autenticación", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor ingresa email y contraseña", Toast.LENGTH_SHORT).show()
        }
    }
}
