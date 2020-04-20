package com.study.dynamicloadrecycler.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.dynamicloadrecycler.R
import com.study.dynamicloadrecycler.`interface`.ILoadMore
import com.study.dynamicloadrecycler.model.Item
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.loading_layout.view.*

internal class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var progressBar: ProgressBar = itemView.progressBar
}

internal class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView = itemView.txtName
    var length: TextView = itemView.txtLength
}

class MyAdapter(
    recyclerView: RecyclerView,
    var activity: Activity,
    private var items: MutableList<Item?>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var loadMore: ILoadMore? = null
    internal var isLoading: Boolean = false
    internal var visibleThreshold = 5
    internal var lastVisibleItem: Int = 0
    internal var totalItemCount: Int = 0

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM_TYPE) {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_layout, parent, false)
            ItemViewHolder(view)
        } else {
            val loading =
                LayoutInflater.from(activity).inflate(R.layout.loading_layout, parent, false)
            LoadingViewHolder(loading)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = items[position]
            holder.name.text = item?.name
            holder.length.text = item?.length.toString()
        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    fun setLoaded() {
        isLoading = false
    }

    fun setLoadedMore(iLoadMore: ILoadMore) {
        this.loadMore = iLoadMore
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) VIEW_LOADINGTYPE else VIEW_ITEM_TYPE
    }

    companion object {
        const val VIEW_ITEM_TYPE = 0
        const val VIEW_LOADINGTYPE = 1
    }
}