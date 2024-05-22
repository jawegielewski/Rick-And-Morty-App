package pl.jawegiel.rickandmortyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.jawegiel.rickandmortyapp.ItemAdapter
import pl.jawegiel.rickandmortyapp.R
import pl.jawegiel.rickandmortyapp.Util
import pl.jawegiel.rickandmortyapp.databinding.ListFragmentBinding
import pl.jawegiel.rickandmortyapp.interfaces.ListMVPContract
import pl.jawegiel.rickandmortyapp.model.RickAndMortyData
import pl.jawegiel.rickandmortyapp.model.RestModelList
import pl.jawegiel.rickandmortyapp.presenter.ListPresenter

// @formatter:off
class ListFragment : Fragment(), ListMVPContract.View {

    companion object {
        const val MORE_ELEMENTS_NUMBER = 10
        const val DIRECTION_TO_DOWN = 1
    }

    private lateinit var presenter: ListMVPContract.Presenter
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var _layoutManager: LinearLayoutManager

    private var rickAndMortyDataList: MutableList<RickAndMortyData> = mutableListOf()
    private var itemCountBeforeAdding: Int = 0
    private var isBusy: Boolean = false
    private var _binding: ListFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _layoutManager = LinearLayoutManager(activity)
        presenter = ListPresenter(this, RestModelList)
        addScrollerListener()

        itemAdapter = ItemAdapter(rickAndMortyDataList, requireActivity())

        presenter.loadItems(0, Util.isOnline(requireActivity()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        RestModelList.cancelCoroutine()
    }

    override fun setLoadingState() {
        isBusy = true
        (activity as MainActivity).showProgressBar()
    }

    override fun removeLoadingState() {
        (activity as MainActivity).hideProgressBar()
        val resultReordered: List<RickAndMortyData> = presenter.reorderResult(rickAndMortyDataList)
        itemAdapter = activity?.let { ItemAdapter(resultReordered, it) }!!
        binding.rv.adapter = itemAdapter
        isBusy = false
    }

    override fun showErrorResponseToast(errorResponse: String) {
        Toast.makeText(activity, getString(R.string.error_during_api_call, errorResponse), Toast.LENGTH_SHORT).show()
    }

    override fun assignResponse(rickAndMortyData: RickAndMortyData?) {
        binding.rv.apply {
            layoutManager = _layoutManager
            rickAndMortyDataList.add(rickAndMortyData!!)
            itemAdapter = activity?.let { ItemAdapter(rickAndMortyDataList, it) }!!
            adapter = itemAdapter
        }
        presenter.processData(rickAndMortyDataList.size == itemCountBeforeAdding + MORE_ELEMENTS_NUMBER)
    }

    private fun addScrollerListener() {
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rv, newState)
                presenter.processScroll(isEligibleToScroll(newState), Util.isOnline(requireActivity()))
            }
        })
    }

    private fun isEligibleToScroll(newState: Int): Boolean {
        return !binding.rv.canScrollVertically(DIRECTION_TO_DOWN) && newState == RecyclerView.SCROLL_STATE_SETTLING && !isBusy
    }

    override fun notifyChangedItemAdapter() {
        itemAdapter.notifyDataSetChanged()
    }

    override fun showToastAboutNoInternet() {
        Toast.makeText(activity, resources.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
    }

    override fun setItemCountBeforeAddingMoreItems() {
        itemCountBeforeAdding = binding.rv.adapter!!.itemCount
    }

    override fun getMainDataListSize(): Int {
        return rickAndMortyDataList.size
    }
}
