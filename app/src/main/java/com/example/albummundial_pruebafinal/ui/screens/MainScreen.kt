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
    val searchResults by viewModel.searchResults.collectAsState()
    val stickers by viewModel.allStickers.collectAsState(initial = emptyList())
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
            Text("Mis Láminas (SQLite):", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
            LazyColumn {
                items(stickers) { sticker ->
                    StickerItem(
                        sticker = sticker,
                        onAddRepeated = { viewModel.addRepeated(sticker.id) },
                        onExchange = { /* Lógica de intercambio */ }
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
fun StickerItem(sticker: StickerEntity, onAddRepeated: () -> Unit, onExchange: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(4.dp), colors = CardDefaults.cardColors(
        containerColor = if (sticker.isObtained) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    )) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${sticker.id} - ${sticker.name}", style = MaterialTheme.typography.bodyLarge)
                Text("Cantidad: ${sticker.quantity}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onAddRepeated) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Repetida")
            }
            if (sticker.quantity > 1) {
                IconButton(onClick = onExchange) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Intercambiar")
                }
            }
        }
    }
}
