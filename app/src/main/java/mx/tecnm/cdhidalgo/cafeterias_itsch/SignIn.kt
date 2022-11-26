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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

private lateinit var btnSignIn: Button

private lateinit var etEmail: EditText
private lateinit var etEmailConf: EditText
private lateinit var etLastName: EditText
private lateinit var etName: EditText
private lateinit var etPassword: EditText
private lateinit var etPasswordConf: EditText

private lateinit var tvLogin: TextView

private lateinit var auth: FirebaseAuth

private val db = FirebaseFirestore.getInstance()

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnSignIn = findViewById(R.id.btnSignInXML)

        etEmail = findViewById(R.id.etSignInEmailXML)
        etEmailConf = findViewById(R.id.etSignInEmailConfXML)
        etLastName = findViewById(R.id.etSignInLastNameXML)
        etName = findViewById(R.id.etSignInNameXML)
        etPassword = findViewById(R.id.etSignInPasswordXML)
        etPasswordConf = findViewById(R.id.etSignInPasswordConfXML)

        tvLogin = findViewById(R.id.tvSignInLoginXML)

        auth = Firebase.auth

        setup()
    }

    private fun setup() {

        btnSignIn.setOnClickListener {

            if (etName.text.isEmpty()) {
                showAlert("El nombre esta vacio")
            } else if (etLastName.text.isEmpty()) {
                showAlert("Los apellidos estan vacios")
            } else if (etEmail.text.isEmpty()) {
                showAlert("El correo esta vacio")
            } else if (!etEmail.text.endsWith("@itsch.edu.mx")) {
                showAlert("El correo no pertenece a la institucion")
            } else if (!validateEmail(etEmail.text.toString())) {
                showAlert("El correo no es valido")
            } else if (!etEmail.text.toString().equals(etEmailConf.text.toString())) {
                showAlert("Los correos no son iguales")
            } else if (etPassword.text.isEmpty()) {
                showAlert("La contraseña esta vacia")
            } else if (etPassword.text.length < 6) {
                showAlert("La contraseña es muy debil")
            } else if (!etPassword.text.toString().equals(etPasswordConf.text.toString())) {
                showAlert("Las contraseñas no son iguales")
            } else {

                val name = etName.text.toString().trimStart().trimEnd().toLowerCase()
                val lastname = etLastName.text.toString().trimStart().trimEnd().toLowerCase()
                val email = etEmail.text.toString().trimStart().trimEnd()
                val password = etPassword.text.toString().trimStart().trimEnd()

                auth.createUserWithEmailAndPassword(
                    email, password
                ).addOnCompleteListener {
                    val user = Firebase.auth.currentUser

                    if (user != null) {
                        user.sendEmailVerification()
                    }

                    val prefs =
                        getSharedPreferences(
                            getString(R.string.prefs_file),
                            Context.MODE_PRIVATE
                        ).edit()
                    prefs.putString("email", email)
                    prefs.apply()

                    db.collection("users").document(email).set(
                        hashMapOf(
                            "Nombre" to name,
                            "Apellido" to lastname,
                            "Correo" to email,
                        )
                    )

                    val intent = Intent(this, Choice::class.java)
                    startActivity(intent)

                }.addOnFailureListener {
                    showAlert(it.message.toString())
                }
            }
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
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