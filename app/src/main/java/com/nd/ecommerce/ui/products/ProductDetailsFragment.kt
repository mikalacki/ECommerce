package com.nd.ecommerce.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nd.ecommerce.R
import com.nd.ecommerce.databinding.FragmentProductDetailsBinding
import com.nd.ecommerce.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        viewModel.fetchProductDetails(args.productId)
        viewModel.observeFavourite(args.productId)

        binding.btnFavourite.setOnClickListener {
            viewModel.toggleFavourite()
        }
    }

    private fun observeData() {
        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            binding.btnFavourite.text = if (isFavourite) "Unlike" else "Like"
        }
        viewModel.productDetails.observe(viewLifecycleOwner) { product ->
            requireActivity().title = product.title
            binding.tvTitle.text = product.title
            binding.tvDescription.text = product.description
            binding.tvPrice.text = requireContext().getString(R.string.price, product.price)

            Glide.with(binding.ivProduct.context)
                .load(product.thumbnail)
                .fitCenter()
                .into(binding.ivProduct)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.tvError.isVisible = !error.isNullOrEmpty()
            binding.tvError.text = error
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.setBnbVisibility(true)
        requireActivity().title = requireContext().getString(R.string.app_name)

        _binding = null
    }
}