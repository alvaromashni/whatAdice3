package br.com.whatadice.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.whatadice.R
import br.com.whatadice.data.SessionManager
import br.com.whatadice.data.UserStorage

class HomeActivity: AppCompatActivity() {
    /* prefixo tv = TextView */
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnLogout: Button

    private lateinit var session: SessionManager
    private lateinit var storage: UserStorage

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        session = SessionManager(this)
        storage = UserStorage(this)

        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        btnLogout = findViewById(R.id.btnLogout)

        val email = session.currentEmail()
        val user = email?.let { storage.getUser(it)}

        tvName.text = user?.name ?: "-"
        tvEmail.text = user?.email ?: "-"

        btnLogout.setOnClickListener {
            session.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}