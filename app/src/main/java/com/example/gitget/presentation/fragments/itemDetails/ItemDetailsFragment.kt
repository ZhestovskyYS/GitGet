package com.example.gitget.presentation.fragments.itemDetails

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitget.R
import com.example.gitget.data.RepoItem
import com.example.gitget.databinding.ItemDetailsFragmentBinding
import com.example.gitget.utils.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ItemDetailsFragment : DaggerFragment(R.layout.item_details_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by activityViewModels<ItemDetailsViewModel> { viewModelFactory }

    private val navArgs: ItemDetailsFragmentArgs by navArgs()

    private val binding by viewBinding(ItemDetailsFragmentBinding::bind)

    private fun bind() {
        val actionOnSave = ItemDetailsFragmentDirections
            .actionItemDetailsFragmentToItemListFragment(
                itemId = navArgs.itemId,
                repoName = binding.repName.text.toString(),
                repoOwner = binding.repOwnerName.text.toString(),
                date = binding.lastCommitDate.text.toString()
            )
        val actionOnCancel = ItemDetailsFragmentDirections
            .actionItemDetailsFragmentToItemListFragment(
                itemId = navArgs.itemId,
                repoName = binding.repName.text.toString(),
                repoOwner = binding.repOwnerName.text.toString(),
                date = binding.lastCommitDate.text.toString()
            )
        binding.apply {
            repName.setText(navArgs.repoName, TextView.BufferType.SPANNABLE)
            repOwnerName.setText(navArgs.repoOwner, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener {
                if (isEntryValid())
                    findNavController().navigate(actionOnSave)
                else
                    findNavController().navigate(actionOnCancel)
            }
            cancelAction.setOnClickListener {
                findNavController().navigate(actionOnCancel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        if (navArgs.date.isBlank()) viewModel.initializeDate(
            navArgs.repoName,
            navArgs.repoOwner
        )
        binding.lastCommitDate.setText(viewModel.date.value, TextView.BufferType.SPANNABLE)
        dateFieldSrcSwitcher()
    }

    private fun dateFieldSrcSwitcher() {
        viewModel.date.observe(this.viewLifecycleOwner) {
            if (navArgs.date.isBlank()) {
                viewModel.date.observe(this.viewLifecycleOwner) {
                    binding.lastCommitDate.setText(it)
                }
            } else
                    binding.lastCommitDate.setText(navArgs.date)
        }
    }

    override fun onDestroyView() {
        viewModel.clearDateOnCancel()
        super.onDestroyView()
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.repName.text.toString(),
            binding.lastCommitDate.text.toString(),
            binding.repOwnerName.text.toString()
        )
    }
}