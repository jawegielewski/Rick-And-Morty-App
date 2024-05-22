package pl.jawegiel.rickandmortyapp.presenter

import com.google.gson.JsonParser
import pl.jawegiel.rickandmortyapp.interfaces.ListMVPContract
import pl.jawegiel.rickandmortyapp.model.RickAndMortyData
import pl.jawegiel.rickandmortyapp.view.ListFragment

// @formatter:off
class ListPresenter(private val view: ListMVPContract.View, private val restModel: ListMVPContract.Model) : ListMVPContract.Presenter {

    override fun makeACall() {
        restModel.fetchResponse(this)
    }

    override fun loadItems(listSizeNow: Int, isOnline: Boolean) {
        if (isOnline) {
            view.setLoadingState()
            val nextLimit = listSizeNow + ListFragment.MORE_ELEMENTS_NUMBER
            for (i in listSizeNow until nextLimit) {
                makeACall()
            }
            view.notifyChangedItemAdapter()
        } else {
            view.showToastAboutNoInternet()
        }
    }

    override fun processData(isSet: Boolean) {
        if (isSet) {
            view.removeLoadingState()
        }
    }

    override fun processScroll(isEligible: Boolean, isOnline: Boolean) {
        if (isEligible) {
            view.setItemCountBeforeAddingMoreItems()
            loadItems(view.getMainDataListSize(), isOnline)
        }
    }

    override fun reorderResult(rickAndMortyDataList: List<RickAndMortyData>): List<RickAndMortyData> {
        return rickAndMortyDataList.sortedBy { rickAndMortyData: RickAndMortyData -> rickAndMortyData.id }
    }

    override fun passErrorResponseMessageToView(errorResponse: String) {
        view.showErrorResponseToast(errorResponse)
    }

    override fun passResponseToView(rickAndMortyData: RickAndMortyData?) {
        view.assignResponse(rickAndMortyData)
    }

    override fun processRawJson(rawJson: String): RickAndMortyData {
        val rootObj = JsonParser().parse(rawJson).asJsonObject
        val rootKeys = rootObj.keySet()
        var id = 0
        var image = ""
        var name = ""
        var status = ""
        for (key in rootKeys) {
            when (key) {
                "id" -> id = rootObj.get(key).asInt
                "image" -> image = rootObj.get(key).asString
                "name" -> name = rootObj.get(key).asString
                "status" -> status = rootObj.get(key).asString
            }
        }

        return RickAndMortyData(id, image, name, status)
    }
}