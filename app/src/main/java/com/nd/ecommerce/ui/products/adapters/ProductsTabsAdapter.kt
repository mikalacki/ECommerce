package com.nd.ecommerce.ui.products.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nd.ecommerce.ui.products.AllProductsFragment
import com.nd.ecommerce.ui.products.favourite.FavouriteProductsFragment

class ProductsTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllProductsFragment()
            else -> FavouriteProductsFragment()
        }
    }
}