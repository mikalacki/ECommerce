package com.nd.ecommerce.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.nd.ecommerce.databinding.FragmentProductsBinding
import com.nd.ecommerce.ui.products.adapters.ProductsLoadStateAdapter
import com.nd.ecommerce.ui.products.adapters.ProductsPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsViewModel by activityViewModels()
    private var productsAdapter: ProductsPagingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "ECommerce"

        setupRecyclerView()
        observePagingData()
        observeLoadState()
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            productsAdapter = ProductsPagingAdapter { productId ->
                val action =
                    ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                        productId
                    )
                findNavController().navigate(action)
            }
            adapter = productsAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)

            binding.rvProducts.layoutManager = gridLayoutManager

            binding.rvProducts.adapter = productsAdapter?.withLoadStateFooter(
                footer = ProductsLoadStateAdapter { productsAdapter?.retry() })

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == productsAdapter?.itemCount) 2 else 1
                }
            }
        }
    }

    private fun observePagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productsPagingData.collectLatest { pagingData ->
                productsAdapter?.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            productsAdapter?.loadStateFlow?.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}