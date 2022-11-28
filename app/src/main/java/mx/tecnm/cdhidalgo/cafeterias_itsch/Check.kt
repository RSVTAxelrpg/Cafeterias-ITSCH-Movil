package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.cafeterias_itsch.adapter.AdapterCheck
import mx.tecnm.cdhidalgo.cafeterias_itsch.model.ModelMenu

private lateinit var btnMakeOrder: Button

private lateinit var tvTotal: TextView

private lateinit var auth: FirebaseAuth

private val db = FirebaseFirestore.getInstance()

class Check : AppCompatActivity() {

    private lateinit var adapterCheck: AdapterCheck

    private lateinit var orderArrayList: ArrayList<ModelMenu>

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        var collection = ""
        collection = intent.getStringExtra("collection").toString()
        println(collection)

        val list = intent.getParcelableArrayListExtra<ModelMenu>("list")
        val total = intent.getIntExtra("total", 0)

        btnMakeOrder = findViewById(R.id.btnCheckMakeOrderXML)

        tvTotal = findViewById(R.id.tvCheckTotalXML)

        recyclerView = findViewById(R.id.rvCheckXML)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        orderArrayList = arrayListOf()

        adapterCheck = AdapterCheck(orderArrayList)

        recyclerView.adapter = adapterCheck

        tvTotal.text = total.toString()

        if (list != null) {
            for (i in list.indices) {
                if (list[i].cantidad >= 1)
                    orderArrayList.add(list[i])
            }
        }

        auth = Firebase.auth

        setup(collection, orderArrayList, total)
    }

    private fun setup(collection: String, order: ArrayList<ModelMenu>, total: Int) {

        btnMakeOrder.setOnClickListener {

            if (total > 0) {
                var products = ""
                for (i in order.indices) {
                    products += "/" + order[i].cantidad + ":" + order[i].nombre + "\n"
                }
                products = products.trimEnd()

                var name = auth.currentUser?.displayName?.toLowerCase()
                if (name != null) {
                    db.collection(collection).document().set(
                        hashMapOf(
                            "entregado" to false,
                            "nombre" to name,
                            "pedido" to products,
                            "total" to total
                        )
                    ).addOnCompleteListener {
                        showAlertOk()
                    }
                }
            } else {
                showAlertError()
            }
        }
    }

    private fun showAlertOk() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Listo!")
        builder.setMessage("¡Has realizado tu pedido correctamente!")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialogInterface, i ->
            goChoice()
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertError() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No has seleccionado algun alimento")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goChoice() {
        val intent = Intent(this, Choice::class.java)
        startActivity(intent)
    }
}
