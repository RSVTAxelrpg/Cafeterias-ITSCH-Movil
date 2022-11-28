package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var btnChoice1: Button
private lateinit var btnOrders: Button
private lateinit var btnSignOut: Button

class Choice : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        btnChoice1 = findViewById(R.id.btnChoice1XML)
        btnOrders = findViewById(R.id.btnChoiceOrdersXML)
        btnSignOut = findViewById(R.id.btnChoiceSignOutXML)

        auth = Firebase.auth

        setup()

        session()
    }

    private fun session() {
        val user = Firebase.auth.currentUser

        if (user != null) {
            if (!user.isEmailVerified) {
                showAlert(
                    "Advertencia",
                    "Correo no verificado, no podras realizar pedidos hasta verificar tu correo"
                )
            }
        }
    }

    private fun setup() {

        btnChoice1.setOnClickListener {

            val intent = Intent(this, Cafeteria1::class.java)
            startActivity(intent)
        }

        btnOrders.setOnClickListener {

            if (auth.currentUser?.isEmailVerified == true) {
                val intent = Intent(this, Order::class.java)
                startActivity(intent)
            } else {
                showAlert("Error", "Usuario no verificado")
            }
        }

        btnSignOut.setOnClickListener {

            val prefs =
                getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun showAlert(title: String, alert: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(alert)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}