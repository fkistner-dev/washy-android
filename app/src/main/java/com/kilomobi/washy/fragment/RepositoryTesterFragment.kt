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
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Product
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.viewmodel.FeedListViewModel
import com.kilomobi.washy.viewmodel.FeedViewModel
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import com.kilomobi.washy.viewmodel.MerchantViewModel

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
            // View model=
            val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FeedListViewModel::class.java)
            viewModel.getAllFeeds().observe(viewLifecycleOwner, Observer<List<Feed>> {
                if (it != null && it.isNotEmpty()) {
                    Log.d("TAG", "Success tester07")
                } else {
                    Log.d("TAG", "awaiting for info")
                }
            })
        }

        return view
    }
}