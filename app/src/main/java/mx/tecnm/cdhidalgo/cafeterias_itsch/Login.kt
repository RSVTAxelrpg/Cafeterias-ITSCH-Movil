package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

private lateinit var btnAnonymous: Button
private lateinit var btnEmail: Button
private lateinit var btnGoogle: Button

private lateinit var tvSignIn: TextView

private lateinit var auth: FirebaseAuth

private val db = FirebaseFirestore.getInstance()

private val GOOGLE_SIGN_IN = 100

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        screenSplash.setKeepOnScreenCondition { false }

        btnAnonymous = findViewById(R.id.btnLoginAnonymousXML)
        btnEmail = findViewById(R.id.btnLoginEmailXML)
        btnGoogle = findViewById(R.id.btnLoginGoogleXML)

        tvSignIn = findViewById(R.id.tvLoginSignInXML)

        auth = Firebase.auth

        session()

        setup()
    }

    private fun session() {

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email != null) {
            val intent = Intent(this, Choice::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun setup() {

        title = "Ingresar"

        btnAnonymous.setOnClickListener {

            auth.signInAnonymously().addOnCompleteListener {
                val intent = Intent(this, Choice::class.java)
                startActivity(intent)
            }
        }

        btnEmail.setOnClickListener {

            val intent = Intent(this, LoginEmail::class.java)
            startActivity(intent)
        }

        btnGoogle.setOnClickListener {

            val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConfig)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }

        tvSignIn.setOnClickListener {

            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account.email?.endsWith("@itsch.edu.mx") == true) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    auth.signInWithCredential(credential).addOnCompleteListener {

                        val prefs =
                            getSharedPreferences(
                                getString(R.string.prefs_file),
                                Context.MODE_PRIVATE
                            ).edit()
                        prefs.putString("email", account.email)
                        prefs.apply()

                        db.collection("users").document(account.email!!).set(
                            hashMapOf(
                                "nombre" to (account.givenName?.toLowerCase() ?: ""),
                                "apellido" to (account.familyName?.toLowerCase() ?: ""),
                                "correo" to account.email,
                            )
                        )

                        val intent = Intent(this, Choice::class.java)
                        finish()
                        startActivity(intent)

                    }.addOnFailureListener {
                        showAlert(it.message.toString())
                    }
                } else {
                    showAlert("El correo no pertenece a la institucion")
                }
            } catch (e: ApiException) {
                showAlert(e.message.toString())
            }
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
}