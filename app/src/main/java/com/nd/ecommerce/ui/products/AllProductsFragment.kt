package com.nd.ecommerce.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.nd.ecommerce.databinding.FragmentAllProductsBinding
import com.nd.ecommerce.ui.MainViewModel
import com.nd.ecommerce.ui.products.adapters.ProductsLoadStateAdapter
import com.nd.ecommerce.ui.products.adapters.ProductsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllProductsFragment : Fragment() {

    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()


    private val productsAdapter by lazy {
        ProductsPagingAdapter(
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
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePagingData()
        observeLoadState()
        observeFavouriteIds()

    }

    private fun observePagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsPagingData.collectLatest { pagingData ->
                    productsAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        binding.rvProducts.layoutManager = gridLayoutManager
        binding.rvProducts.adapter = productsAdapter.withLoadStateFooter(
            footer = ProductsLoadStateAdapter { productsAdapter.retry() }
        )
        (binding.rvProducts.itemAnimator as? androidx.recyclerview.widget.SimpleItemAnimator)
            ?.supportsChangeAnimations = false
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == productsAdapter.itemCount) 2 else 1
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsAdapter.loadStateFlow.collectLatest { loadStates ->
                    binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                }
            }
        }
    }

    private fun observeFavouriteIds() {
        viewModel.favouriteProductIds.observe(viewLifecycleOwner) { favouriteIds ->
            productsAdapter.updateFavouriteIds(favouriteIds)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}