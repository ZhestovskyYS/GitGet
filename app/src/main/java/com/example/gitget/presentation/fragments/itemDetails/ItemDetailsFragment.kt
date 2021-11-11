package com.example.gitget.presentation.fragments.itemDetails

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitget.R
import com.example.gitget.data.ArgData
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

    private fun bindFields(){
        binding.apply {
            repName.setText(navArgs.repoName, TextView.BufferType.SPANNABLE)
            repOwnerName.setText(navArgs.repoOwner, TextView.BufferType.SPANNABLE)
        }
    }

    private fun bindActions() {
        var argsData = ArgData(0, "","", "")
        viewModel.args.observe(this.viewLifecycleOwner){
            argsData = ArgData(
                it.itemId,
                it.repoName,
                it.repoOwner,
                it.date
            )
        }
        binding.apply {
            saveAction.setOnClickListener {
                viewModel.initializeArgs(
                    navArgs.itemId,
                    binding.repName.text.toString(),
                    binding.repOwnerName.text.toString(),
                    binding.lastCommitDate.text.toString()
                )
                if (isEntryValid())
                    findNavController().navigate(
                        ItemDetailsFragmentDirections
                            .actionItemDetailsFragmentToItemListFragment(
                                itemId = argsData.itemId,
                                repoName = argsData.repoName,
                                repoOwner = argsData.repoOwner,
                                date = argsData.date
                            )
                    )
                else
                    findNavController().navigate(
                        ItemDetailsFragmentDirections
                            .actionItemDetailsFragmentToItemListFragment(
                                itemId = navArgs.itemId,
                                repoName = navArgs.repoName,
                                repoOwner = navArgs.repoOwner,
                                date = binding.lastCommitDate.text.toString()
                            )
                    )
            }
            cancelAction.setOnClickListener {
                findNavController().navigate(
                    ItemDetailsFragmentDirections
                        .actionItemDetailsFragmentToItemListFragment(
                            itemId = navArgs.itemId,
                            repoName = navArgs.repoName,
                            repoOwner = navArgs.repoOwner,
                            date = binding.lastCommitDate.text.toString()
                        )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindFields()
        if (navArgs.date.isBlank()) viewModel.initializeDate(
            navArgs.repoName,
            navArgs.repoOwner
        )
        binding.lastCommitDate.setText(viewModel.date.value, TextView.BufferType.SPANNABLE)
        dateFieldSrcSwitcher()
        bindActions()
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