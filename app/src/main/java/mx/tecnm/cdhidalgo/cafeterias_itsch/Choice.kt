package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var btnChoice1: Button
private lateinit var btnChoice2: Button
private lateinit var btnSignOut: Button

private lateinit var ivChoice1: ImageView
private lateinit var ivChoice2: ImageView

class Choice : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        btnChoice1 = findViewById(R.id.btnChoice1XML)
        btnChoice2 = findViewById(R.id.btnChoice2XML)
        btnSignOut = findViewById(R.id.btnChoiceSignOutXML)

        ivChoice1 = findViewById(R.id.ivChoice1XML)
        ivChoice2 = findViewById(R.id.ivChoice2XML)

        auth = Firebase.auth

        session()

        setup()
    }

    private fun session() {

        if (!auth.currentUser?.isEmailVerified!!) {
            showAlert(
                "Advertencia",
                "Correo no verificado, no podras realizar pedidos hasta verificar tu correo"
            )
        }
    }

    private fun setup() {

        title = "Selecciona una cafetería"

        btnChoice1.setOnClickListener {

            if (auth.currentUser?.isEmailVerified == true) {
                val intent = Intent(this, Order::class.java)
                intent.putExtra("collection", "orders")
                startActivity(intent)
            } else {
                showAlert("Error", "Usuario no verificado")
            }
        }

        btnChoice2.setOnClickListener {

            if (auth.currentUser?.isEmailVerified == true) {
                val intent = Intent(this, Order::class.java)
                intent.putExtra("collection", "orders2")
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
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        ivChoice1.setOnClickListener {

            val intent = Intent(this, Cafeteria::class.java)
            intent.putExtra("collection", "menu")
            startActivity(intent)
        }

        ivChoice2.setOnClickListener {

            val intent = Intent(this, Cafeteria::class.java)
            intent.putExtra("collection", "menu2")
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