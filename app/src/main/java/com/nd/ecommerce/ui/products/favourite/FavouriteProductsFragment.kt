package com.nd.ecommerce.ui.products.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nd.ecommerce.databinding.FragmentFavouriteProductsBinding
import com.nd.ecommerce.ui.MainViewModel
import com.nd.ecommerce.ui.products.ProductsFragmentDirections
import com.nd.ecommerce.ui.products.adapters.FavouriteProductsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteProductsFragment : Fragment() {

    private var _binding: FragmentFavouriteProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteProductsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()


    private val favouritesAdapter by lazy {
        FavouriteProductsAdapter(
            onProductClick = { productId ->
                val action =
                    ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                        productId
                    )
                findNavController().navigate(action)
                mainViewModel.setBnbVisibility(false)
            },
            onFavouriteClick = { product ->
                viewModel.toggleFavourite(product)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavouriteProducts.layoutManager =
            GridLayoutManager(requireContext(), getSpanCount())
        binding.rvFavouriteProducts.adapter = favouritesAdapter

        viewModel.favouriteProducts.observe(viewLifecycleOwner) { products ->
            favouritesAdapter.submitList(products)
            binding.tvEmpty.isVisible = products.isEmpty()
        }
    }

    private fun getSpanCount(): Int {
        val swDp = resources.configuration.smallestScreenWidthDp
        return when {
            swDp >= 840 -> 4
            swDp >= 600 -> 3
            else -> 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}