package mx.tecnm.cdhidalgo.cafeterias_itsch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.cdhidalgo.cafeterias_itsch.R
import mx.tecnm.cdhidalgo.cafeterias_itsch.model.ModelMenu

class AdapterMenuCheck(private val menuList: ArrayList<ModelMenu>) :
    RecyclerView.Adapter<AdapterMenuCheck.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_check, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val menu: ModelMenu = menuList[position]

        holder.cantidad.text = menu.cantidad.toString()
        holder.nombre.text = menu.nombre
        holder.precio.text = menu.precio.toString()
    }

    override fun getItemCount(): Int {

        return menuList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cantidad: TextView = itemView.findViewById(R.id.tvListCheckAmountXML)
        var nombre: TextView = itemView.findViewById(R.id.tvListCheckNameXML)
        var precio: TextView = itemView.findViewById(R.id.tvListCheckPriceXML)
    }
}