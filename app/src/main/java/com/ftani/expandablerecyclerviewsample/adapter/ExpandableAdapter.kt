package com.ftani.expandablerecyclerviewsample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.ftani.expandablerecyclerviewsample.databinding.RowExpandableHeaderBinding
import com.ftani.expandablerecyclerviewsample.databinding.RowExpandableItemBinding
import com.ftani.expandablerecyclerviewsample.databinding.RowHeaderBinding

class ExpandableAdapter(
    private val header: Header,
    private var expandableHeader: ExpandableHeader,
    private val expandableItems: List<ExpandableItem> = emptyList(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isExpanded: Boolean = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_HEADER ->
            HeaderViewHolder.create(LayoutInflater.from(parent.context), parent)
        VIEW_TYPE_EXPANDABLE_HEADER ->
            ExpandableHeaderViewHolder.create(LayoutInflater.from(parent.context), parent)
        else ->
            ExpandableItemViewHolder.create(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder ->
                holder.setData(header)
            is ExpandableHeaderViewHolder -> {
                holder.setData(expandableHeader)
                holder.onToggleExpandable(::toggle)
            }
            is ExpandableItemViewHolder ->
                holder.setData(expandableItems[position - 2]) // ヘッダーと開閉部分のヘッダーの数を引く
        }
    }

    override fun getItemCount(): Int = if (isExpanded) {
        HEADER_COUNT + EXPANDABLE_HEADER_COUNT + expandableItems.size
    } else {
        HEADER_COUNT + EXPANDABLE_HEADER_COUNT
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_HEADER
            1 -> VIEW_TYPE_EXPANDABLE_HEADER
            else -> VIEW_TYPE_EXPANDABLE_ITEM
        }
    }

    private fun toggle() {
        isExpanded = !isExpanded
        if (isExpanded) {
            expandableHeader = expandableHeader.copy(isExpanded = isExpanded)
            notifyItemChanged(VIEW_TYPE_EXPANDABLE_HEADER)
            notifyItemRangeInserted(VIEW_TYPE_EXPANDABLE_ITEM, expandableItems.size)
        } else {
            expandableHeader = expandableHeader.copy(isExpanded = isExpanded)
            notifyItemChanged(VIEW_TYPE_EXPANDABLE_HEADER)
            notifyItemRangeRemoved(VIEW_TYPE_EXPANDABLE_ITEM, expandableItems.size)
        }
    }

    companion object {
        private const val HEADER_COUNT = 1
        private const val EXPANDABLE_HEADER_COUNT = 1

        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_EXPANDABLE_HEADER = 1
        private const val VIEW_TYPE_EXPANDABLE_ITEM = 2
    }
}

class HeaderViewHolder private constructor(
    private val binding: RowHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(
        data: Header
    ) {
        binding.header.text = data.name
    }

    companion object {
        fun create(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
        ): HeaderViewHolder {
            val binding = RowHeaderBinding.inflate(layoutInflater, parent, false)
            return HeaderViewHolder(binding)
        }
    }
}

class ExpandableHeaderViewHolder private constructor(
    private val binding: RowExpandableHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun setData(
        data: ExpandableHeader
    ) {
        binding.expandableHeader.text = data.name

        binding.expandableHeaderCheck.isChecked = data.isChecked
        binding.expandableHeaderCheck.setOnClickListener {
            data.onSelectedHeader.invoke(!data.isChecked)
        }
    }

    fun onToggleExpandable(onToggleItems: () -> Unit) {
        binding.root.setOnClickListener {
            onToggleItems.invoke()
        }
    }

    companion object {
        fun create(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
        ): ExpandableHeaderViewHolder {
            val binding = RowExpandableHeaderBinding.inflate(layoutInflater, parent, false)
            return ExpandableHeaderViewHolder(binding)
        }
    }
}

class ExpandableItemViewHolder private constructor(
    private val binding: RowExpandableItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(
        data: ExpandableItem
    ) {
        binding.expandableItem.text = data.name
        binding.expandableItemCheck.isChecked = data.isChecked
        binding.expandableItemCheck.setOnClickListener {
            data.onSelectedItem.invoke((it as CheckBox).isChecked)
        }
    }

    companion object {
        fun create(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
        ): ExpandableItemViewHolder {
            val binding = RowExpandableItemBinding.inflate(layoutInflater, parent, false)
            return ExpandableItemViewHolder(binding)
        }
    }
}

data class Header(
    val name: String
)

typealias OnSelectedHeader = (Boolean) -> Unit

data class ExpandableHeader(
    val name: String,
    val isChecked: Boolean,
    val isExpanded: Boolean,
    val onSelectedHeader: OnSelectedHeader,
)

typealias OnSelectedItem = (Boolean) -> Unit

data class ExpandableItem(
    val name: String,
    val isChecked: Boolean,
    val onSelectedItem: OnSelectedItem
)