package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.cdhidalgo.cafeterias_itsch.adapter.AdapterMenuCheck
import mx.tecnm.cdhidalgo.cafeterias_itsch.model.ModelMenu

private lateinit var tvTotal: TextView

class Check : AppCompatActivity() {

    private lateinit var adapterMenu: AdapterMenuCheck

    private lateinit var ordenArrayList: ArrayList<ModelMenu>

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        val list = intent.getParcelableArrayListExtra<ModelMenu>("list")
        val total = intent.getIntExtra("total", 0)

        tvTotal = findViewById(R.id.tvCheckTotalXML)

        recyclerView = findViewById(R.id.rvCheckXML)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        ordenArrayList = arrayListOf()

        adapterMenu = AdapterMenuCheck(ordenArrayList)

        recyclerView.adapter = adapterMenu

        tvTotal.text = total.toString()

        if (list != null) {
            for (i in list.indices) {
                if (list[i].cantidad >= 1)
                    ordenArrayList.add(list[i])
            }
        }
    }


}
