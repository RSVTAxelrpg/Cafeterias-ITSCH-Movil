package mx.tecnm.cdhidalgo.cafeterias_itsch.model

data class ModelOrder(
    var entregado: Boolean,
    var nombre: String,
    var pedido: String,
    var total: Int,
)
