package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.cafeterias_itsch.adapter.AdapterOrder
import mx.tecnm.cdhidalgo.cafeterias_itsch.model.ModelOrder

class Order : AppCompatActivity() {

    private lateinit var adapterOrder: AdapterOrder

    private lateinit var auth: FirebaseAuth

    private lateinit var orderArrayList: ArrayList<ModelOrder>

    private lateinit var db: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        recyclerView = findViewById(R.id.rvOrderXML)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        orderArrayList = arrayListOf()

        adapterOrder = AdapterOrder(orderArrayList)

        recyclerView.adapter = adapterOrder

        auth = Firebase.auth

        var collection = ""
        collection = intent.getStringExtra("collection").toString()

        setup(collection)
    }

    private fun setup(collection: String) {

        title = "Pedidos"

        var name = auth.currentUser?.displayName?.toLowerCase()

        db = FirebaseFirestore.getInstance()
        db.collection(collection)
            .orderBy("entregado", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener {

                for (document in it) {
                    if (document.data.get("nombre") == name) {
                        orderArrayList.add(
                            ModelOrder(
                                document.data.get("entregado").toString().toBoolean(),
                                document.data.get("nombre").toString(),
                                document.data.get("pedido").toString(),
                                document.data.get("total").toString().toInt()
                            )
                        )
                    }
                }

                adapterOrder.notifyDataSetChanged()
            }
    }
}
