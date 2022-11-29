package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.cafeterias_itsch.adapter.AdapterMenu
import mx.tecnm.cdhidalgo.cafeterias_itsch.model.ModelMenu

private lateinit var btnCheck: Button

class Cafeteria : AppCompatActivity() {

    private lateinit var adapterMenu: AdapterMenu

    private lateinit var auth: FirebaseAuth

    private lateinit var menuArrayList: ArrayList<ModelMenu>

    private lateinit var db: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria)

        var collection = ""
        collection = intent.getStringExtra("collection").toString()

        btnCheck = findViewById(R.id.btnCafeteria1CheckXML)

        recyclerView = findViewById(R.id.rvCafeteria1XML)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        menuArrayList = arrayListOf()

        adapterMenu = AdapterMenu(menuArrayList)

        recyclerView.adapter = adapterMenu

        auth = Firebase.auth

        setup(collection)
    }

    private fun setup(collection: String) {

        title = "Cafeteria"

        db = FirebaseFirestore.getInstance()
        db.collection(collection).orderBy("nombre", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {

                for (document in it) {
                    menuArrayList.add(
                        0, ModelMenu(
                            0,
                            document.data.get("nombre").toString(),
                            document.data.get("precio").toString().toInt(),
                            0
                        )
                    )
                }

                adapterMenu.notifyDataSetChanged()
            }

        btnCheck.setOnClickListener {

            if (auth.currentUser?.isEmailVerified == true) {
                var total = 0

                for (i in menuArrayList.indices) {
                    if (menuArrayList[i].cantidad >= 1) {
                        total += menuArrayList[i].total
                    }
                }

                val intent = Intent(this, Check::class.java)
                intent.putExtra("list", menuArrayList)
                intent.putExtra("total", total)
                if (collection == "menu") {
                    intent.putExtra("collection", "orders")
                } else if (collection == "menu2") {
                    intent.putExtra("collection", "orders2")
                }
                startActivity(intent)
            } else {
                showAlert("Usuario no verificado")
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

