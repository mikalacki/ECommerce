package com.nd.ecommerce.ui.products.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nd.ecommerce.R
import com.nd.ecommerce.data.Product
import com.nd.ecommerce.databinding.ItemProductBinding

class FavouriteProductsAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onFavouriteClick: (Product) -> Unit
) : ListAdapter<Product, FavouriteProductsAdapter.FavouriteProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavouriteProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onProductClick(product.id)
        }

        holder.binding.ivFavourite.setOnClickListener {
            onFavouriteClick(product)
        }
    }

    class FavouriteProductViewHolder(
        val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvTitle.text = product.title
            binding.tvPrice.text = itemView.context.getString(R.string.price, product.price)

            Glide.with(binding.ivProduct.context)
                .load(product.thumbnail)
                .fitCenter()
                .into(binding.ivProduct)

            binding.ivFavourite.setImageResource(R.drawable.ic_favorite)
        }
    }

    companion object {
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