package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private lateinit var btnAnonymous: Button
private lateinit var btnEmail: Button
private lateinit var btnGoogle: Button

private lateinit var tvSignIn: TextView

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnAnonymous = findViewById(R.id.btnLoginAnonymousXML)
        btnEmail = findViewById(R.id.btnLoginEmailXML)
        btnGoogle = findViewById(R.id.btnLoginGoogleXML)

        tvSignIn = findViewById(R.id.tvLoginSignInXML)

        setup()
    }

    private fun setup() {

        btnAnonymous.setOnClickListener {

        }

        btnEmail.setOnClickListener {
            val intent = Intent(this, LoginEmail::class.java)
            startActivity(intent)
        }

        btnGoogle.setOnClickListener {

        }

        tvSignIn.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }
}