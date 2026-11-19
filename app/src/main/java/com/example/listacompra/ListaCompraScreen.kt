import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

@Composable
fun ListaCompraScreen(
    viewModel: ListaCompraViewModel,
    modifier: Modifier = Modifier
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    val productos = viewModel.productos
    val pendientes = productos.count { !it.comprado }
    val margen = dimensionResource(R.dimen.screen_margin)
    val espacio = dimensionResource(R.dimen.spacing_small)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(margen)
    ) {
        // Contador (plurals)
        Text(
            text = pluralStringResource(R.plurals.pending_products_count, pendientes, pendientes),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(espacio))

        // Fila de entrada
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(stringResource(R.string.product_hint)) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(espacio))
            Button(onClick = {
                viewModel.anadirProducto(nombre)
                nombre = ""
            }) {
                Text(stringResource(R.string.add_button))
            }
        }
        Spacer(modifier = Modifier.height(margen))

        // Lista o mensaje vacío
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_list_message),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(espacio)) {
                items(productos, key = { it.id }) { producto ->
                    ProductoCard(
                        producto = producto,
                        onAlternar = { viewModel.alternarComprado(producto) },
                        onEliminar = { viewModel.eliminarProducto(producto) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    onAlternar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val margen = dimensionResource(R.dimen.screen_margin)

    Card(
        onClick = onAlternar,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Box para colocar el icono de estado en una esquina
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp)
                    .padding(margen)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Icon(
                    imageVector = if (producto.comprado) Icons.Filled.CheckCircle
                    else Icons.Filled.ShoppingCart,
                    contentDescription = stringResource(
                        if (producto.comprado) R.string.icon_bought_description
                        else R.string.icon_pending_description
                    ),
                    tint = if (producto.comprado) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.icon_delete_description)
                )
            }
        }
    }
}