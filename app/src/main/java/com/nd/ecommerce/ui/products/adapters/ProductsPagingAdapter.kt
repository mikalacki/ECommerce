package com.nd.ecommerce.ui.products.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nd.ecommerce.R
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.databinding.ItemProductBinding

class ProductsPagingAdapter(
    private val onProductClick: (Int) -> Unit, private val onFavouriteClick: (Product) -> Unit
) : PagingDataAdapter<Product, ProductsPagingAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    private var favouriteIds: Set<Int> = emptySet()

    fun updateFavouriteIds(newFavouriteIds: Set<Int>) {
        val oldFavouriteIds = favouriteIds
        favouriteIds = newFavouriteIds

        val changedIds =
            oldFavouriteIds.minus(newFavouriteIds).union(newFavouriteIds.minus(oldFavouriteIds))

        changedIds.forEach { changedId ->
            val position = snapshot().items.indexOfFirst { it.id == changedId }
            if (position != -1) {
                notifyItemChanged(position, PAYLOAD_FAVOURITE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { product ->
            val isFavourite = favouriteIds.contains(product.id)
            holder.bind(product, isFavourite)

            holder.itemView.setOnClickListener {
                onProductClick(product.id)
            }

            holder.binding.ivFavourite.setOnClickListener {
                onFavouriteClick(product)
            }
        }
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        val product = getItem(position) ?: return

        if (payloads.contains(PAYLOAD_FAVOURITE)) {
            holder.updateFavouriteIcon(favouriteIds.contains(product.id))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    class ProductViewHolder(
        val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product, isFavourite: Boolean) {
            binding.tvTitle.text = product.title
            binding.tvPrice.text = itemView.context.getString(R.string.price, product.price)

            Glide.with(binding.ivProduct.context).load(product.thumbnail).fitCenter()
                .into(binding.ivProduct)

            updateFavouriteIcon(isFavourite)
        }

        fun updateFavouriteIcon(isFavourite: Boolean) {
            binding.ivFavourite.setImageResource(
                if (isFavourite) R.drawable.ic_favorite
                else R.drawable.ic_favorite_border
            )
        }
    }

    companion object {
        private const val PAYLOAD_FAVOURITE = "payload_favourite"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}