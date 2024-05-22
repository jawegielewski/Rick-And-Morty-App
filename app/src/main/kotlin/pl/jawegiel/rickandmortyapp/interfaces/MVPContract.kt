package pl.jawegiel.rickandmortyapp.interfaces

import pl.jawegiel.rickandmortyapp.model.RickAndMortyData

// @formatter:off
interface MVPContract {

    interface Model

    interface View {
        fun assignResponse(rickAndMortyData: RickAndMortyData?)
        fun showErrorResponseToast(errorResponse: String)
    }

    interface Presenter {
        fun passResponseToView(rickAndMortyData: RickAndMortyData?)
        fun processRawJson(rawJson: String): RickAndMortyData
        fun passErrorResponseMessageToView(errorResponse: String)
    }
}
