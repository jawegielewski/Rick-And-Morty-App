package pl.jawegiel.rickandmortyapp

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.jawegiel.rickandmortyapp.databinding.RecyclerviewItemBinding
import pl.jawegiel.rickandmortyapp.model.RickAndMortyData
import pl.jawegiel.rickandmortyapp.view.ListItemFragment
import pl.jawegiel.rickandmortyapp.view.MainActivity

// @formatter:off
class ItemAdapter(private var items: List<RickAndMortyData>, private val activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, activity)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataViewHolder)
            holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class DataViewHolder(view: RecyclerviewItemBinding, private val activity: Activity) : RecyclerView.ViewHolder(view.root) {
        val tvId: TextView = view.tvPos
        val ivImage: ImageView = view.ivImage
        val tvName: TextView = view.tvName
        val tvStatus: TextView = view.tvStatus

        fun bind(rickAndMortyData: RickAndMortyData) {
            val pos = adapterPosition + 1
            tvId.text = pos.toString()

            Glide.with(itemView.context).load(rickAndMortyData.image).into(ivImage);

            tvName.text = rickAndMortyData.name
            tvStatus.text = rickAndMortyData.status
            itemView.setOnClickListener {
                activity as MainActivity
                activity.changeFragment(ListItemFragment(), RickAndMortyData(rickAndMortyData.id, rickAndMortyData.image, rickAndMortyData.name, rickAndMortyData.status)
                )
            }
        }
    }
}