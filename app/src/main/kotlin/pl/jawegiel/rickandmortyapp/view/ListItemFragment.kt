package pl.jawegiel.rickandmortyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import pl.jawegiel.rickandmortyapp.interfaces.ListItemMVPContract
import pl.jawegiel.rickandmortyapp.model.RestModelListItem
import pl.jawegiel.rickandmortyapp.model.RickAndMortyData
import pl.jawegiel.rickandmortyapp.presenter.PresenterListItem
import pl.jawegiel.rickandmortyapp.R
import pl.jawegiel.rickandmortyapp.Util
import pl.jawegiel.rickandmortyapp.databinding.ListItemFragmentBinding

// @formatter:off
class ListItemFragment : Fragment(), ListItemMVPContract.View {

    private lateinit var presenter: ListItemMVPContract.Presenter

    private var itemPosition: Int = 0
    private var _binding: ListItemFragmentBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ListItemFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = PresenterListItem(this, RestModelListItem)
        presenter.processData(arguments != null, Util.isOnline(requireActivity()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        RestModelListItem.cancelCoroutine()
    }

    // sets the values to the views in common with ListFragment
    override fun setBasicValuesToViews() {
        itemPosition = requireArguments().getSerializable(MainActivity.ID) as Int
        binding.tvId.text = itemPosition.toString()
        Glide.with(requireContext())
            .load(requireArguments().getSerializable(MainActivity.IMAGE))
            .into(binding.ivImage);
        binding.tvName.text = requireArguments().getSerializable(MainActivity.NAME) as CharSequence?
        binding.tvStatus.text = requireArguments().getSerializable(MainActivity.STATUS) as CharSequence?
    }

    override fun getItemPosition(): Int {
        return itemPosition
    }

    // sets the values to the rest of views
    override fun assignResponse(rickAndMortyData: RickAndMortyData?) {
        binding.tvSpecies.text = rickAndMortyData?.additionalData?.species
        binding.tvOriginName.text = rickAndMortyData?.additionalData?.originName
        val originUrl = if (rickAndMortyData?.additionalData?.originUrl.equals("")) "-" else rickAndMortyData?.additionalData?.originUrl
        binding.tvOriginUrl.text = originUrl
        binding.tvLocationName.text = rickAndMortyData?.additionalData?.locationName
        val locationUrl = if (rickAndMortyData?.additionalData?.locationUrl.equals("")) "-" else rickAndMortyData?.additionalData?.locationUrl
        binding.tvLocationUrl.text = locationUrl
        binding.tvCreated.text = rickAndMortyData?.additionalData?.created
    }

    override fun showErrorResponseToast(errorResponse: String) {
        Toast.makeText(activity, getString(R.string.error_during_api_call, errorResponse), Toast.LENGTH_SHORT).show()
    }
}