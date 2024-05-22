package pl.jawegiel.rickandmortyapp.presenter

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import pl.jawegiel.rickandmortyapp.interfaces.ListItemMVPContract
import pl.jawegiel.rickandmortyapp.model.AdditionalData
import pl.jawegiel.rickandmortyapp.model.RickAndMortyData

// @formatter:off
class PresenterListItem(private val view: ListItemMVPContract.View, private val restModel: ListItemMVPContract.Model) : ListItemMVPContract.Presenter {

    override fun makeACall(id: Int) {
        restModel.fetchResponse(this, id)
    }

    override fun loadItems(id: Int, isOnline: Boolean) {
        if (isOnline) {
            makeACall(id)
        }
    }

    override fun processData(isArgsExist: Boolean, isOnline: Boolean) {
        if (isArgsExist) {
            view.setBasicValuesToViews()
            loadItems(view.getItemPosition(), isOnline)
        }
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
        var species = ""
        var jsonObj: JsonObject
        var originName = ""
        var originUrl = ""
        var locationName = ""
        var locationUrl = ""
        var created = ""
        for (key in rootKeys) {
            when (key) {
                "id" -> id = rootObj.get(key).asInt
                "image" -> image = rootObj.get(key).asString
                "name" -> name = rootObj.get(key).asString
                "status" -> status = rootObj.get(key).asString
                "species" -> species = rootObj.get(key).asString
                "origin" -> {
                    jsonObj = rootObj.get(key).asJsonObject
                    originName = jsonObj.get("name").asString
                    originUrl = jsonObj.get("url").asString
                }
                "location" -> {
                    jsonObj = rootObj.get(key).asJsonObject
                    locationName = jsonObj.get("name").asString
                    locationUrl = jsonObj.get("url").asString
                }
                "created" -> created = rootObj.get(key).asString
            }
        }

        return RickAndMortyData(id, image, name, status, AdditionalData(species, originName, originUrl, locationName, locationUrl, created))
    }
}