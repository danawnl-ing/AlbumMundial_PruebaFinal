package com.example.albummundial_pruebafinal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.albummundial_pruebafinal.data.local.StickerEntity
import com.example.albummundial_pruebafinal.data.remote.Player
import com.example.albummundial_pruebafinal.data.repository.StickerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StickerViewModel(
    private val repository: StickerRepository
) : ViewModel() {

    val allStickers = repository.allStickers
    val obtainedStickers = repository.obtainedStickers
    val pendingStickers = repository.pendingStickers
    val repeatedStickers = repository.repeatedStickers

    private val _searchResults = MutableStateFlow<List<Player>>(emptyList())
    val searchResults: StateFlow<List<Player>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun registerSticker(
        id: String,
        name: String,
        team: String
    ) {
        viewModelScope.launch {
            repository.registerSticker(
                StickerEntity(
                    id = id,
                    name = name,
                    team = team,
                    isObtained = true,
                    quantity = 1
                )
            )
        }
    }

    fun obtainSticker(sticker: StickerEntity) {
        viewModelScope.launch {
            repository.registerSticker(
                sticker.copy(
                    isObtained = true,
                    quantity = 1
                )
            )
        }
    }

    fun addRepeated(id: String) {
        viewModelScope.launch {
            repository.incrementSticker(id)
        }
    }

    fun useRepeatedSticker(id: String) {
        viewModelScope.launch {
            repository.exchangeStickers(id, "")
        }
    }

    fun exchangeSticker(
        giveId: String,
        receiveId: String
    ) {
        viewModelScope.launch {
            repository.exchangeStickers(
                giveId,
                receiveId
            )
        }
    }

    fun searchPlayer(name: String) {
        viewModelScope.launch {

            _isLoading.value = true

            try {
                val response = repository.searchPlayer(name)
                _searchResults.value =
                    response.players ?: emptyList()

            } catch (_: Exception) {

                _searchResults.value = emptyList()

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun loadInitialStickers() {
        viewModelScope.launch {

            val stickers = listOf(

                // Argentina (10)
                StickerEntity("ARG1", "Lionel Messi", "Argentina"),
                StickerEntity("ARG2", "Julián Álvarez", "Argentina"),
                StickerEntity("ARG3", "Enzo Fernández", "Argentina"),
                StickerEntity("ARG4", "Lautaro Martínez", "Argentina"),
                StickerEntity("ARG5", "Rodrigo De Paul", "Argentina"),
                StickerEntity("ARG6", "Alexis Mac Allister", "Argentina"),
                StickerEntity("ARG7", "Emiliano Martínez", "Argentina"),
                StickerEntity("ARG8", "Cristian Romero", "Argentina"),
                StickerEntity("ARG9", "Nahuel Molina", "Argentina"),
                StickerEntity("ARG10", "Nicolás Otamendi", "Argentina"),

                // Brasil (10)
                StickerEntity("BRA1", "Vinicius Jr", "Brasil"),
                StickerEntity("BRA2", "Rodrygo", "Brasil"),
                StickerEntity("BRA3", "Neymar Jr", "Brasil"),
                StickerEntity("BRA4", "Alisson", "Brasil"),
                StickerEntity("BRA5", "Marquinhos", "Brasil"),
                StickerEntity("BRA6", "Gabriel Jesus", "Brasil"),
                StickerEntity("BRA7", "Raphinha", "Brasil"),
                StickerEntity("BRA8", "Bruno Guimarães", "Brasil"),
                StickerEntity("BRA9", "Éder Militão", "Brasil"),
                StickerEntity("BRA10", "Casemiro", "Brasil"),

                // Colombia (10)
                StickerEntity("COL1", "Luis Díaz", "Colombia"),
                StickerEntity("COL2", "James Rodríguez", "Colombia"),
                StickerEntity("COL3", "Jhon Arias", "Colombia"),
                StickerEntity("COL4", "Richard Ríos", "Colombia"),
                StickerEntity("COL5", "Daniel Muñoz", "Colombia"),
                StickerEntity("COL6", "Rafael Santos Borré", "Colombia"),
                StickerEntity("COL7", "Camilo Vargas", "Colombia"),
                StickerEntity("COL8", "Davinson Sánchez", "Colombia"),
                StickerEntity("COL9", "Jhon Córdoba", "Colombia"),
                StickerEntity("COL10", "Jefferson Lerma", "Colombia"),

                // Francia (10)
                StickerEntity("FRA1", "Kylian Mbappé", "Francia"),
                StickerEntity("FRA2", "Antoine Griezmann", "Francia"),
                StickerEntity("FRA3", "Ousmane Dembélé", "Francia"),
                StickerEntity("FRA4", "Aurélien Tchouaméni", "Francia"),
                StickerEntity("FRA5", "Eduardo Camavinga", "Francia"),
                StickerEntity("FRA6", "Theo Hernández", "Francia"),
                StickerEntity("FRA7", "Mike Maignan", "Francia"),
                StickerEntity("FRA8", "Dayot Upamecano", "Francia"),
                StickerEntity("FRA9", "Jules Koundé", "Francia"),
                StickerEntity("FRA10", "Kingsley Coman", "Francia"),

                // Portugal (10)
                StickerEntity("POR1", "Cristiano Ronaldo", "Portugal"),
                StickerEntity("POR2", "Bruno Fernandes", "Portugal"),
                StickerEntity("POR3", "Bernardo Silva", "Portugal"),
                StickerEntity("POR4", "Rafael Leão", "Portugal"),
                StickerEntity("POR5", "João Félix", "Portugal"),
                StickerEntity("POR6", "Rúben Dias", "Portugal"),
                StickerEntity("POR7", "Diogo Costa", "Portugal"),
                StickerEntity("POR8", "João Cancelo", "Portugal"),
                StickerEntity("POR9", "Vitinha", "Portugal"),
                StickerEntity("POR10", "Pepe", "Portugal"),

                // España (10)
                StickerEntity("ESP1", "Pedri", "España"),
                StickerEntity("ESP2", "Gavi", "España"),
                StickerEntity("ESP3", "Lamine Yamal", "España"),
                StickerEntity("ESP4", "Álvaro Morata", "España"),
                StickerEntity("ESP5", "Nico Williams", "España"),
                StickerEntity("ESP6", "Rodri", "España"),
                StickerEntity("ESP7", "Dani Olmo", "España"),
                StickerEntity("ESP8", "Unai Simón", "España"),
                StickerEntity("ESP9", "Aymeric Laporte", "España"),
                StickerEntity("ESP10", "Ferran Torres", "España"),

                // Inglaterra (10)
                StickerEntity("ENG1", "Harry Kane", "Inglaterra"),
                StickerEntity("ENG2", "Jude Bellingham", "Inglaterra"),
                StickerEntity("ENG3", "Phil Foden", "Inglaterra"),
                StickerEntity("ENG4", "Bukayo Saka", "Inglaterra"),
                StickerEntity("ENG5", "Declan Rice", "Inglaterra"),
                StickerEntity("ENG6", "Kyle Walker", "Inglaterra"),
                StickerEntity("ENG7", "Jordan Pickford", "Inglaterra"),
                StickerEntity("ENG8", "John Stones", "Inglaterra"),
                StickerEntity("ENG9", "Marcus Rashford", "Inglaterra"),
                StickerEntity("ENG10", "Trent Alexander-Arnold", "Inglaterra"),

                // Alemania (10)
                StickerEntity("GER1", "Jamal Musiala", "Alemania"),
                StickerEntity("GER2", "Florian Wirtz", "Alemania"),
                StickerEntity("GER3", "Kai Havertz", "Alemania"),
                StickerEntity("GER4", "Ilkay Gündogan", "Alemania"),
                StickerEntity("GER5", "Joshua Kimmich", "Alemania"),
                StickerEntity("GER6", "Antonio Rüdiger", "Alemania"),
                StickerEntity("GER7", "Marc-André ter Stegen", "Alemania"),
                StickerEntity("GER8", "Leroy Sané", "Alemania"),
                StickerEntity("GER9", "Niclas Füllkrug", "Alemania"),
                StickerEntity("GER10", "Jonathan Tah", "Alemania"),

                // Uruguay (10)
                StickerEntity("URU1", "Federico Valverde", "Uruguay"),
                StickerEntity("URU2", "Darwin Núñez", "Uruguay"),
                StickerEntity("URU3", "Ronald Araújo", "Uruguay"),
                StickerEntity("URU4", "José María Giménez", "Uruguay"),
                StickerEntity("URU5", "Manuel Ugarte", "Uruguay"),
                StickerEntity("URU6", "Sergio Rochet", "Uruguay"),
                StickerEntity("URU7", "Facundo Pellistri", "Uruguay"),
                StickerEntity("URU8", "Maximiliano Araújo", "Uruguay"),
                StickerEntity("URU9", "Nicolás De La Cruz", "Uruguay"),
                StickerEntity("URU10", "Matías Viña", "Uruguay"),

                // Países Bajos (10)
                StickerEntity("NED1", "Virgil van Dijk", "Países Bajos"),
                StickerEntity("NED2", "Cody Gakpo", "Países Bajos"),
                StickerEntity("NED3", "Memphis Depay", "Países Bajos"),
                StickerEntity("NED4", "Frenkie de Jong", "Países Bajos"),
                StickerEntity("NED5", "Xavi Simons", "Países Bajos"),
                StickerEntity("NED6", "Nathan Aké", "Países Bajos"),
                StickerEntity("NED7", "Denzel Dumfries", "Países Bajos"),
                StickerEntity("NED8", "Bart Verbruggen", "Países Bajos"),
                StickerEntity("NED9", "Stefan de Vrij", "Países Bajos"),
                StickerEntity("NED10", "Joey Veerman", "Países Bajos")
            )

            stickers.forEach {
                repository.registerSticker(it)
            }
        }
    }
}

class StickerViewModelFactory(
    private val repository: StickerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(StickerViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return StickerViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}