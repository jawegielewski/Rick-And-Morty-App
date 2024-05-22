package pl.jawegiel.rickandmortyapp.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jawegiel.rickandmortyapp.APIClient
import pl.jawegiel.rickandmortyapp.interfaces.ListItemMVPContract

// @formatter:off
object RestModelListItem : ListItemMVPContract.Model {

    private lateinit var coroutineScope : CoroutineScope

    private val apiClient = APIClient.getInstance()

    override fun fetchResponse(presenter: ListItemMVPContract.Presenter, position: Int) {
        coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            val response = apiClient.getCharacterById(position)
            withContext(Dispatchers.Main) {
                try {
                    val rickAndMortyModel = presenter.processRawJson(response.body()!!)
                    presenter.passResponseToView(rickAndMortyModel)
                }
                catch (ex: Exception) {
                    presenter.passErrorResponseMessageToView(ex.message.toString())
                }
            }
        }
    }

    fun cancelCoroutine() {
        coroutineScope.cancel()
    }
}