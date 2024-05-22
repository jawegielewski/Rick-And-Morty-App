package pl.jawegiel.rickandmortyapp.interfaces

import pl.jawegiel.rickandmortyapp.model.RickAndMortyData

// @formatter:off
interface ListMVPContract : MVPContract {

    interface Model : MVPContract.Model {
        fun fetchResponse(presenter: Presenter)
    }

    interface View : MVPContract.View {
        fun setLoadingState()
        fun removeLoadingState()
        fun notifyChangedItemAdapter()
        fun showToastAboutNoInternet()
        fun setItemCountBeforeAddingMoreItems()
        fun getMainDataListSize(): Int
    }

    interface Presenter : MVPContract.Presenter{
        fun reorderResult(rickAndMortyDataList: List<RickAndMortyData>): List<RickAndMortyData>
        fun makeACall()
        fun loadItems(listSizeNow: Int, isOnline: Boolean)
        fun processData(isSet: Boolean)
        fun processScroll(isEligible: Boolean, isOnline: Boolean)
    }
}
