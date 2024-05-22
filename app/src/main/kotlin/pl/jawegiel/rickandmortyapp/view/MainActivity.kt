package pl.jawegiel.rickandmortyapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import pl.jawegiel.rickandmortyapp.databinding.MainActivityBinding
import pl.jawegiel.rickandmortyapp.model.RickAndMortyData

// @formatter:off
class MainActivity : AppCompatActivity() {

    companion object {
        const val ID = "id"
        const val IMAGE = "image"
        const val NAME = "name"
        const val STATUS = "status"
    }

    private lateinit var fragmentContainer: View
    private lateinit var currentFragment: Fragment

    private var _binding: MainActivityBinding? = null

    private val binding get() = _binding!!
    private val listFragment: ListFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentFragment = listFragment
        fragmentContainer = binding.fragmentContainer
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.apply {
            add(fragmentContainer.id, listFragment, "main")
            commit()
        }

        changeToolbarSubtitle(currentFragment::class.simpleName.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        if (supportFragmentManager.getBackStackEntryCount() > 0) {
            supportFragmentManager.popBackStack()

            currentFragment = listFragment
            changeToolbarSubtitle(currentFragment::class.simpleName.toString())

        } else {
            super.onBackPressed()
        }
    }

    fun changeFragment(fragment: Fragment, rickAndMortyData: RickAndMortyData) {
        if (fragment is ListItemFragment) {
            val args = Bundle()
            args.putSerializable(ID, rickAndMortyData.id)
            args.putSerializable(IMAGE, rickAndMortyData.image)
            args.putString(NAME, rickAndMortyData.name)
            args.putString(STATUS, rickAndMortyData.status)
            fragment.setArguments(args)
        }
        changeFragment(fragment)
    }

    private fun changeFragment(fragment: Fragment) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            hide(currentFragment)

            when (fragment) {
                is ListFragment -> {
                    currentFragment = fragment
                    show(currentFragment)
                    changeToolbarSubtitle(currentFragment::class.simpleName.toString())
                }

                is ListItemFragment -> {
                    currentFragment = fragment
                    add(fragmentContainer.id, currentFragment)
                    changeToolbarSubtitle(currentFragment::class.simpleName.toString())
                }
            }

            addToBackStack(null)
            commit()
        }
    }

    private fun changeToolbarSubtitle(subtitle: String) {
        supportActionBar!!.subtitle = subtitle
    }

    fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressbar.visibility = View.GONE
    }
}