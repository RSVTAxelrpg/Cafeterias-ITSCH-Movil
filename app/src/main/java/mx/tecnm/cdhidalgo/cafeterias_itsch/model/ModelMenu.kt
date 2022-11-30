package mx.tecnm.cdhidalgo.cafeterias_itsch.model

import android.os.Parcel
import android.os.Parcelable

data class ModelMenu(
    var cantidad: Int = 0,
    var fecha: String? = "",
    var nombre: String? = "",
    var precio: Int = 0,
    var total: Int = 0,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cantidad)
        parcel.writeString(fecha)
        parcel.writeString(nombre)
        parcel.writeInt(precio)
        parcel.writeInt(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelMenu> {
        override fun createFromParcel(parcel: Parcel): ModelMenu {
            return ModelMenu(parcel)
        }

        override fun newArray(size: Int): Array<ModelMenu?> {
            return arrayOfNulls(size)
        }
    }
}