package com.example.albummundial_pruebafinal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.albummundial_pruebafinal.data.local.StickerEntity
import com.example.albummundial_pruebafinal.data.remote.Player
import com.example.albummundial_pruebafinal.ui.viewmodels.StickerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: StickerViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var mostrarObtenidas by remember { mutableStateOf(true) }
    val searchResults by viewModel.searchResults.collectAsState()
    val obtenidas by viewModel.obtainedStickers.collectAsState(initial = emptyList())
    val pendientes by viewModel.pendingStickers.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Álbum Mundial 2026") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Buscador de API
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar jugador en API") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = { viewModel.searchPlayer(searchQuery) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            // Resultados de API
            if (searchResults.isNotEmpty()) {
                Text("Resultados de la API:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
                LazyColumn(modifier = Modifier.height(200.dp)) {
                    items(searchResults) { player ->
                        PlayerItem(player) {
                            viewModel.registerSticker(player.id, player.name, player.team)
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))



            // Inventario Local
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { mostrarObtenidas = true }
                ) {
                    Text("Obtenidas")
                }

                Button(
                    onClick = { mostrarObtenidas = false }
                ) {
                    Text("Pendientes")
                }
            }
            Text(
                text = if (mostrarObtenidas)
                    "Láminas Obtenidas"
                else
                    "Láminas Pendientes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )

            LazyColumn {

                val listaActual =
                    if (mostrarObtenidas)
                        obtenidas
                    else
                        pendientes

                items(listaActual) { sticker ->

                    StickerItem(
                        sticker = sticker,

                        onAddRepeated = {

                            if (!sticker.isObtained) {
                                viewModel.obtainSticker(sticker)
                            } else {
                                viewModel.addRepeated(sticker.id)
                            }
                        },

                        onExchange = { }
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerItem(player: Player, onRegister: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = player.photoUrl,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                Text(player.name, style = MaterialTheme.typography.bodyLarge)
                Text(player.team, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onRegister) {
                Icon(Icons.Default.Add, contentDescription = "Registrar")
            }
        }
    }
}

@Composable
fun StickerItem(
    sticker: StickerEntity,
    onAddRepeated: () -> Unit,
    onExchange: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "${sticker.id} - ${sticker.name}"
                )

                Text(
                    text = "Equipo: ${sticker.team}"
                )

                if (sticker.isObtained) {
                    Text(
                        text = "Cantidad: ${sticker.quantity}"
                    )
                } else {
                    Text(
                        text = "Pendiente"
                    )
                }
            }

            if (!sticker.isObtained) {

                Button(
                    onClick = onAddRepeated
                ) {
                    Text("OBTENER")
                }

            } else {

                Button(
                    onClick = onAddRepeated
                ) {
                    Text("+1 REPETIDA")
                }

                if (sticker.quantity > 1) {

                    IconButton(
                        onClick = onExchange
                    ) {
                        Icon(
                            Icons.Default.SwapHoriz,
                            contentDescription = "Intercambiar"
                        )
                    }
                }
            }
        }
    }
}
