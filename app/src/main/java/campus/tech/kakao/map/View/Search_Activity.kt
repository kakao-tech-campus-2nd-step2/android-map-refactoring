package campus.tech.kakao.map.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import androidx.appcompat.widget.SearchView
import campus.tech.kakao.map.Adapter.SavedSearchAdapter
import campus.tech.kakao.map.R
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import campus.tech.kakao.map.Data.SearchViewModel

class Search_Activity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: PlaceAdapter
    private lateinit var savedSearchRecyclerView: RecyclerView
    private lateinit var savedSearchAdapter: SavedSearchAdapter
    private lateinit var placesClient: PlacesClient
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Places.initialize(applicationContext, "AIzaSyCUncz7v8nwT3m5OHasVJTep1e1549yAKM")
        placesClient = Places.createClient(this)

        initViews()
        initAdapters()
        setupRecyclerViews()
        setupSearchView()

        observeSearchResults()
        observeSavedSearches()
    }

    private fun initViews() {
        searchView = findViewById(R.id.search_text)
        searchRecyclerView = findViewById(R.id.RecyclerVer)
        savedSearchRecyclerView = findViewById(R.id.recyclerHor)
    }

    private fun initAdapters() {
        searchResultAdapter = PlaceAdapter(emptyList())
        savedSearchAdapter = SavedSearchAdapter(this, emptyList()) { searchText ->
            searchAndDisplayResults(searchText)
        }
    }

    private fun setupRecyclerViews() {
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        savedSearchRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        searchRecyclerView.adapter = searchResultAdapter
        savedSearchRecyclerView.adapter = savedSearchAdapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchViewModel.insertSearchResult(text = query)
                    searchAndDisplayResults(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    searchAndDisplayResults("")
                } else {
                    searchAndDisplayResults(newText)
                }
                return true
            }
        })
    }

    private fun searchAndDisplayResults(searchText: String) {
        if (searchText.isBlank()) {
            searchRecyclerView.visibility = RecyclerView.GONE
            return
        }
    }

    private fun observeSearchResults() {
        searchViewModel.searchResults.observe(this, Observer { results ->
            if (results.isEmpty()) {
                searchRecyclerView.visibility = RecyclerView.GONE
            } else {
                val places = results.map { result ->
                    mapOf(
                        "name" to result.text,
                        "address" to "",
                        "id" to result.id.toString()
                    )
                }
                searchResultAdapter.updateData(places)
                searchRecyclerView.visibility = RecyclerView.VISIBLE
            }
        })
    }

    private fun observeSavedSearches() {
        searchViewModel.searchResults.observe(this, Observer { savedSearches ->
            savedSearchAdapter.notifyDataSetChanged()
        })
    }

    inner class PlaceAdapter(private var data: List<Map<String, String>>) :
        RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_place_item, parent, false)
            return PlaceViewHolder(view)
        }

        override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
            val place = data[position]
            holder.bind(place)
        }

        override fun getItemCount(): Int = data.size

        fun updateData(newData: List<Map<String, String>>) {
            data = newData
            notifyDataSetChanged()
        }

        inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView: TextView = itemView.findViewById(R.id.name)
            private val addressTextView: TextView = itemView.findViewById(R.id.place)

            fun bind(place: Map<String, String>) {
                nameTextView.text = place["name"] ?: "No Name"
                addressTextView.text = place["address"] ?: "No Address"

                itemView.setOnClickListener {
                    val placeId = place["id"]
                    if (!placeId.isNullOrBlank()) {
                        fetchPlaceDetails(placeId)
                    }
                }
            }

            private fun fetchPlaceDetails(placeId: String) {
                val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                placesClient.fetchPlace(request).addOnSuccessListener { response ->
                    val place = response.place
                    val latLng = place.latLng

                    if (latLng != null) {
                        val placeName = place.name ?: "Unknown"
                        val placeAddress = place.address ?: "Unknown"

                        val intent = Intent(itemView.context, Map_Activity::class.java).apply {
                            putExtra(
                                "selectedPlace",
                                campus.tech.kakao.map.Data.Place(
                                    placeName,
                                    placeAddress,
                                    "",
                                    latLng.latitude,
                                    latLng.longitude
                                )
                            )
                        }
                        itemView.context.startActivity(intent)
                    } else {
                        Log.e("FetchPlaceDetails", "latLng is null for placeId: $placeId")
                    }
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                    Toast.makeText(itemView.context, "Failed to fetch place details", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}







