package br.com.whatadice.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.whatadice.R
import br.com.whatadice.data.SessionManager
import br.com.whatadice.data.UserStorage
import br.com.whatadice.util.cleanPassword
import br.com.whatadice.util.normalizeEmail

class LoginActivity: AppCompatActivity() {
    /* prefixo et = EditText*/
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoRegister: Button

    private lateinit var session: SessionManager
    private lateinit var storage: UserStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)
        storage = UserStorage(this)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoRegister = findViewById(R.id.btnGoRegister)

        btnLogin.setOnClickListener {
            val rawEmail = etEmail.text.toString().trim()
            val email = normalizeEmail(rawEmail)
            val password = cleanPassword(etPassword.text.toString())

            if (email.isEmpty() || password.isEmpty()) {
                toast("Preencha todos os campos.")
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                toast("Email invalido.")
            }
            if (!storage.validateLogin(email, password)) {
                toast("Credenciais invalidas.")
                storage.debugDump()
                return@setOnClickListener
            }

            session.login(email)
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}