package mx.tecnm.cdhidalgo.cafeterias_itsch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.cdhidalgo.cafeterias_itsch.R
import mx.tecnm.cdhidalgo.cafeterias_itsch.model.ModelMenu

class AdapterMenu(private val menuList: ArrayList<ModelMenu>) :
    RecyclerView.Adapter<AdapterMenu.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_view, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val menu: ModelMenu = menuList[position]

        holder.nombre.text = menu.nombre
        holder.precio.text = menu.precio.toString()

        holder.btnAdd.setOnClickListener {
            var num = 0
            num = holder.cantidad.text.toString().toInt()
            num++
            holder.cantidad.text = num.toString()

            menu.cantidad = holder.cantidad.text.toString().toInt()
            holder.cantidad.text = menu.cantidad.toString()
        }

        /*holder.checkBox.setOnCheckedChangeListener { button, b ->
            menu.Check = button.isChecked
        }

        holder.checkBox.isChecked = menu.Check*/
    }

    override fun getItemCount(): Int {

        return menuList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val btnAdd: Button = itemView.findViewById(R.id.btnListViewAddXML)
        val btnSubtract: Button = itemView.findViewById(R.id.btnListViewSubtractXML)

        var cantidad: TextView = itemView.findViewById(R.id.tvListViewAmountXML)

        //var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

        var nombre: TextView = itemView.findViewById(R.id.tvListViewNameXML)
        var precio: TextView = itemView.findViewById(R.id.tvListViewPriceXML)
    }
}