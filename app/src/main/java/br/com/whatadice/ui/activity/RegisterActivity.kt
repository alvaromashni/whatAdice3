package br.com.whatadice.ui.activity


import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.whatadice.R
import br.com.whatadice.data.UserStorage
import br.com.whatadice.model.User
import br.com.whatadice.util.cleanPassword
import br.com.whatadice.util.normalizeEmail
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity: AppCompatActivity() {
//    private lateinit var etName: EditText
//    private lateinit var etEmail: EditText
//    private lateinit var etPassword: EditText
//    private lateinit var btnRegister: MaterialButton

    private var btnBackToLogin: MaterialButton? = null


    private lateinit var storage: UserStorage

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        storage = UserStorage(this)

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val btnBackToLogin = findViewById<MaterialButton?>(R.id.btnBackToLogin)

        btnRegister.setOnClickListener {
            try {
                val name = etName.text.toString().trim()
                val rawEmail = etEmail.text.toString().trim()
                val email = normalizeEmail(rawEmail)
                val password = cleanPassword(etPassword.text.toString())

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                    toast("Preencha todos os campos.")
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    toast("Email invalido.")
                    return@setOnClickListener
                }
                if (password.length < 6) {
                    toast("A senha precisa ter no minimo 6 caracteres.")
                    return@setOnClickListener
                }
                if (storage.userExists(email)){
                    toast("Usuario ja cadastrado.")
                    return@setOnClickListener
                }

                val registerOk = storage.addUser(User(name, email, password))
                if (registerOk){
                    toast("Usuario ja cadastrado.")
                    finish()
                } else {
                    toast("Erro ao salvar cadastro.")
                }
            } catch (e: Exception) {
                android.util.Log.e("RegisterActivity", "Erro no cadastro", e)
                toast("Erro inesperado no cadastro")
            }
        }
        btnBackToLogin?.setOnClickListener { finish() }
    }
    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}