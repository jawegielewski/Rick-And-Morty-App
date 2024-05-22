package pl.jawegiel.rickandmortyapp.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jawegiel.rickandmortyapp.APIClient
import pl.jawegiel.rickandmortyapp.interfaces.ListMVPContract

// @formatter:off
object RestModelList : ListMVPContract.Model {

    private var coroutineScope : CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var id: Int = 0

    private val apiClient = APIClient.getInstance()

    override fun fetchResponse(presenter: ListMVPContract.Presenter) {
        coroutineScope.launch {
            id++
            val response = apiClient.getCharacterById(id)
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