package com.study.dynamicloadrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Adapter
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.dynamicloadrecycler.`interface`.ILoadMore
import com.study.dynamicloadrecycler.adapter.MyAdapter
import com.study.dynamicloadrecycler.model.Item
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ILoadMore {

    val items: MutableList<Item?> = ArrayList()
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        random10Data()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(recyclerView, this, items)
        recyclerView.adapter = adapter
        adapter.setLoadedMore(this)
    }

    private fun random10Data() {
        for (i in 0..9) {
            val name = UUID.randomUUID().toString()
            val item = Item(name, name.length)
            items.add(item)
        }
    }

    override fun onLoadMore() {
        if (items.size < 50) {
            items.add(null)
            adapter.notifyItemInserted(items.size - 1)
            Handler().postDelayed({
                items.removeAt(items.size - 1)
                adapter.notifyItemRemoved(items.size)

                val index = items.size
                val end = index + 10

                for (i in index until end) {
                    val name = UUID.randomUUID().toString()
                    val item = Item(name, name.length)
                    items.add(item)
                }
                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }, 3000)
        } else {
            Toast.makeText(this, "MAX DATA IS 50", Toast.LENGTH_SHORT).show()
        }
    }
}

