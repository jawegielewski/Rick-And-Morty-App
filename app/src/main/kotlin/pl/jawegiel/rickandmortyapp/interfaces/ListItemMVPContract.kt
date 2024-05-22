package pl.jawegiel.rickandmortyapp.interfaces

// @formatter:off
interface ListItemMVPContract : MVPContract {

    interface Model : MVPContract.Model {
        fun fetchResponse(presenter: Presenter, position: Int)
    }

    interface View :MVPContract.View {
        fun setBasicValuesToViews()
        fun getItemPosition(): Int
    }

    interface Presenter : MVPContract.Presenter {
        fun makeACall(id: Int)
        fun loadItems(id: Int, isOnline: Boolean)
        fun processData(isArgsExist: Boolean, isOnline: Boolean)
    }
}
