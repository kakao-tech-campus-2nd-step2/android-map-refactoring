package campus.tech.kakao.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.Profile
import campus.tech.kakao.map.databinding.ActivityItemViewBinding

class Adapter(private val profiles: MutableList<Profile>) : RecyclerView.Adapter<Adapter.ProfileViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(name: String, address: String, latitude: String, longitude: String)
    }

    var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ProfileViewHolder(val binding: ActivityItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    val profile = profiles[position]
                    listener?.onItemClick(profile.name, profile.address, profile.latitude, profile.longitude)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityItemViewBinding.inflate(inflater, parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.binding.profile = profile
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    fun updateProfiles(newProfiles: List<Profile>) {
        profiles.clear()
        profiles.addAll(newProfiles)
        notifyDataSetChanged()
    }
}
