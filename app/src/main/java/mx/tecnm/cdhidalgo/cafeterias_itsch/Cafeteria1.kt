package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.cdhidalgo.cafeterias_itsch.adapter.AdapterMenu
import mx.tecnm.cdhidalgo.cafeterias_itsch.model.ModelMenu

private lateinit var btnCheck: Button

private lateinit var tvTotal: TextView

class Cafeteria1 : AppCompatActivity() {

    private lateinit var adapterMenu: AdapterMenu

    private lateinit var menuArrayList: ArrayList<ModelMenu>

    private lateinit var db: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafeteria1)

        btnCheck = findViewById(R.id.btnCafeteria1CheckXML)

        tvTotal = findViewById(R.id.tvCafeteria1TotalXML)

        recyclerView = findViewById(R.id.rvCafeteria1XML)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        menuArrayList = arrayListOf()

        adapterMenu = AdapterMenu(menuArrayList)

        recyclerView.adapter = adapterMenu

        //eventChangeListener()

        setData()
    }

    private fun setData() {

        var total = 0

        db = FirebaseFirestore.getInstance()
        db.collection("menu2").get().addOnSuccessListener {

            var number = 0
            for (document in it) {
                menuArrayList.add(
                    0, ModelMenu(
                        document.data.get("nombre").toString(),
                        document.data.get("precio").toString().toInt(),
                        false
                    )
                )
                menuArrayList[number].Check = true
            }

            adapterMenu.notifyDataSetChanged()
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

