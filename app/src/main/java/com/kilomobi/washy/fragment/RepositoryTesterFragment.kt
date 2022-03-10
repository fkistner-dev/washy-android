package com.kilomobi.washy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.gson.GsonBuilder
import com.kilomobi.washy.R
import com.kilomobi.washy.model.*
import com.kilomobi.washy.util.GeoPointDeserializer
import com.kilomobi.washy.viewmodel.FeedListViewModel
import com.kilomobi.washy.viewmodel.FeedViewModel
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import com.kilomobi.washy.viewmodel.MerchantViewModel
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.GeoLocation
import org.imperiumlabs.geofirestore.core.GeoHash
import org.imperiumlabs.geofirestore.extension.getAtLocation
import java.io.*

class RepositoryTesterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_tester_repository, container, false)

        view.findViewById<Button>(R.id.tester01).setOnClickListener {
            val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FeedListViewModel::class.java)
            viewModel.getAllFeeds().observe(viewLifecycleOwner, Observer<List<Feed>> {
                if (it != null && it.isNotEmpty()) {
                    Log.d("TAG", "Success tester01")
                } else {
                    Log.d("TAG", "awaiting for info")
                }
            })
        }

        view.findViewById<Button>(R.id.tester02).setOnClickListener {
            val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FeedViewModel::class.java)
            viewModel.getMerchantFeed("ptOx8KQHo0t8zT0Okpgi")?.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {
                    Log.d("TAG", "Success tester02")
                } else {
                    Log.d("TAG", "Failure tester02")
                }
            })
        }

        view.findViewById<Button>(R.id.tester03).setOnClickListener {
            val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MerchantListViewModel::class.java)
            viewModel.getAllMerchants().observe(viewLifecycleOwner, Observer<List<Merchant>> {
                if (it != null && it.isNotEmpty()) {
                    Log.d("TAG", "Success tester03")
                } else {
                    Log.d("TAG", "awaiting for info")
                }
            })
        }

        view.findViewById<Button>(R.id.tester04).setOnClickListener {
            val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MerchantViewModel::class.java)
            viewModel.getMerchant("U90uUV7Mya5nr5atEiFN")?.observe(viewLifecycleOwner, Observer<Merchant> {
                if (it != null) {
                    Log.d("TAG", "Success tester04")
                } else {
                    Log.d("TAG", "Failure tester04")
                }
            })
        }

        view.findViewById<Button>(R.id.tester05).setOnClickListener {
            // View model=
            val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MerchantViewModel::class.java)
            viewModel.getRatings("ptOx8KQHo0t8zT0Okpgi").observe(viewLifecycleOwner, Observer<List<Rating>> {
                if (it != null && it.isNotEmpty()) {
                    Log.d("TAG", "Success tester05")
                } else {
                    Log.d("TAG", "awaiting for info")
                }
            })
        }

        view.findViewById<Button>(R.id.tester06).setOnClickListener {
            // View model=
            val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MerchantViewModel::class.java)
            viewModel.getProducts("U90uUV7Mya5nr5atEiFN").observe(viewLifecycleOwner, Observer<List<Product>> {
                if (it != null && it.isNotEmpty()) {
                    Log.d("TAG", "Success tester06")
                } else {
                    Log.d("TAG", "awaiting for info")
                }
            })
        }

        view.findViewById<Button>(R.id.tester07).setOnClickListener {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(GeoPoint::class.java,
                GeoPointDeserializer()
            )

            val gson = gsonBuilder.create()
            val `is`: InputStream = resources.openRawResource(R.raw.firebase_carwash)
            val writer: Writer = StringWriter()
            val buffer = CharArray(1024)
            `is`.use { `is` ->
                val reader: Reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
                var n: Int
                while (reader.read(buffer).also { n = it } != -1) {
                    writer.write(buffer, 0, n)
                }
            }

            val jsonString: String = writer.toString()
            val merchantListResponse = gson.fromJson(jsonString, MerchantListResponse::class.java)

            val viewModel = MerchantViewModel()
//            viewModel.addMerchant(merchantListResponse.merchants[0])

            for (merchant in merchantListResponse.merchants) {
                if (merchant.position != null) {
                    val geoHash = GeoHash(GeoLocation(merchant.position!!.latitude, merchant.position!!.longitude))
                    merchant.geohash = geoHash.geoHashString
                    viewModel.addMerchant(merchant)
                }
            }
        }

        view.findViewById<Button>(R.id.tester08).setOnClickListener {
            val collectionRef = FirebaseFirestore.getInstance().collection("merchants")
            val geoFirestore = GeoFirestore(collectionRef)
//
//            geoFirestore.setLocation(
//                "0FOejkOzUJ3ZRiDfBzcc",
//                GeoPoint(48.60368749777777, 7.602562499977777)
//            ) { exception ->
//                if (exception == null)
//                    Log.d("TAG", "Location saved on server successfully!")
//            }
        }

        view.findViewById<Button>(R.id.tester09).setOnClickListener {
            val collectionRef = FirebaseFirestore.getInstance().collection("merchants")
            val geoFirestore = GeoFirestore(collectionRef)

//            geoFirestore.getLocation("0FOejkOzUJ3ZRiDfBzcc") { location, exception ->
//                if (exception == null && location != null){
//                    Log.d("TAG", "The location for this document is $location")
//                }
//            }

            geoFirestore.getAtLocation(GeoPoint(48.60368749777777, 7.602562499977777), 8000.0) { docs, ex ->
                if (ex != null) {
                    val geoQuery = geoFirestore.queryAtLocation(GeoPoint(48.59, 7.70), 0.6)

                    Log.e("TAG", "onError: ", ex)
                    return@getAtLocation
                } else {
                    val geoFirestore = GeoFirestore(collectionRef)
                    // ...
                }
            }
        }

        view.findViewById<Button>(R.id.tester10).setOnClickListener {
            val collectionRef = FirebaseFirestore.getInstance().collection("merchants")
            val geoFirestore = GeoFirestore(collectionRef)

            geoFirestore.removeLocation("0FOejkOzUJ3ZRiDfBzcc")
        }
        return view
    }
}