package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kilomobi.washy.*
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.feed.Feed
import com.kilomobi.washy.feed.FeedAdapter
import com.kilomobi.washy.recycler.RecyclerItem
import kotlinx.android.synthetic.main.fragment_list_dealer.*

class FeedFragment : Fragment(), AdapterListener, MainActivityDelegate {

    private val listAdapter by lazy { FeedAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        listAdapter.submitList(ExampleFeedData.createList())
    }

    override fun listen(click: AdapterClick?) {
        TODO("Not yet implemented")
    }

    override fun setupNavDrawer(toolbar: Toolbar) {
        TODO("Not yet implemented")
    }

    override fun enableNavDrawer(enable: Boolean) {
        TODO("Not yet implemented")
    }
}

object ExampleFeedData {

    fun createList(): List<RecyclerItem> {
        val list = ArrayList<RecyclerItem>()

        list.add(
            Feed(
                id = "1",
                name = "Total Wash",
                message = "Promotion exceptionnelle"
            )
        )

        list.add(
            Feed(
                id = "2",
                name = "Total Wash",
                message = "Tout pour le déconfinement !"
            )
        )

        list.add(
            Feed(
                id = "3",
                name = "Total Wash",
                message = "Washement Propwe !"
            )
        )

        return list
    }

}