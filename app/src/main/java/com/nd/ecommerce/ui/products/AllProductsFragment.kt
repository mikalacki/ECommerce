package com.nd.ecommerce.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
        setupSearch()
        binding.btnRetry.setOnClickListener {
            productsAdapter.retry()
        }

    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { editable ->
            viewModel.updateSearchQuery(editable?.toString().orEmpty())
        }
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
        val gridLayoutManager = GridLayoutManager(requireContext(), getSpanCount())

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
                    val isLoading = loadStates.refresh is LoadState.Loading
                    val isError = loadStates.refresh is LoadState.Error
                    val isNotLoading = loadStates.refresh is LoadState.NotLoading
                    val isEmpty =
                        loadStates.refresh is LoadState.NotLoading && productsAdapter.itemCount == 0

                    binding.tvEmpty.isVisible = isEmpty
                    binding.rvProducts.isVisible = !isError && !isEmpty
                    binding.progressBar.isVisible = isLoading
                    binding.rvProducts.isVisible = isNotLoading
                    binding.tvError.isVisible = isError
                    binding.btnRetry.isVisible = isError

                    if (isError) {
                        val errorState = loadStates.refresh as LoadState.Error
                        binding.tvError.text = errorState.error.localizedMessage
                            ?: "Failed to load products"
                    }
                }
            }
        }
    }

    private fun observeFavouriteIds() {
        viewModel.favouriteProductIds.observe(viewLifecycleOwner) { favouriteIds ->
            productsAdapter.updateFavouriteIds(favouriteIds)
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