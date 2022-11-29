package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

private lateinit var btnLogin: Button

private lateinit var etEmail: EditText
private lateinit var etPassword: EditText

private lateinit var tvSignIn: TextView

private lateinit var auth: FirebaseAuth

class LoginEmail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_email)

        btnLogin = findViewById(R.id.btnLoginEmailLoginXML)

        etEmail = findViewById(R.id.etLoginEmailEmailXML)
        etPassword = findViewById(R.id.etLoginEmailPasswordXML)

        tvSignIn = findViewById(R.id.tvLoginEmailSignInXML)

        auth = Firebase.auth

        setup()

        session()
    }

    private fun session() {

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email != null) {
            val intent = Intent(this, Choice::class.java)
            startActivity(intent)
        }
    }

    private fun setup() {

        btnLogin.setOnClickListener {
            if (etEmail.text.isEmpty()) {
                showAlert("El correo esta vacio")
            } else if (!validateEmail(etEmail.text.toString())) {
                showAlert("El correo no es valido")
            } else if (etPassword.text.isEmpty()) {
                showAlert("La contraseña esta vacia")
            } else {

                auth.signInWithEmailAndPassword(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
                    .addOnSuccessListener {

                        val prefs =
                            getSharedPreferences(
                                getString(R.string.prefs_file),
                                Context.MODE_PRIVATE
                            ).edit()
                        prefs.putString("email", etEmail.text.toString())
                        prefs.apply()

                        val intent = Intent(this, Choice::class.java)
                        finish()
                        startActivity(intent)
                    }.addOnFailureListener {
                        showAlert(it.message.toString())
                    }
            }
        }

        tvSignIn.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }

    private fun showAlert(alert: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(alert)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun validateEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}