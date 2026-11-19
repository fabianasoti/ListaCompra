import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ListaCompraViewModel : ViewModel() {

    private val _productos = mutableStateListOf<Producto>()
    val productos: List<Producto> get() = _productos

    private var siguienteId = 0

    fun anadirProducto(nombre: String) {
        val limpio = nombre.trim()
        if (limpio.isEmpty()) return
        _productos.add(Producto(id = siguienteId++, nombre = limpio))
        Log.d(TAG, "Producto añadido: $limpio")
    }

    fun eliminarProducto(producto: Producto) {
        _productos.remove(producto)
        Log.d(TAG, "Producto eliminado: ${producto.nombre}")
    }

    fun alternarComprado(producto: Producto) {
        val indice = _productos.indexOfFirst { it.id == producto.id }
        if (indice != -1) {
            _productos[indice] = producto.copy(comprado = !producto.comprado)
        }
    }

    companion object {
        private const val TAG = "ListaCompra"
    }
}