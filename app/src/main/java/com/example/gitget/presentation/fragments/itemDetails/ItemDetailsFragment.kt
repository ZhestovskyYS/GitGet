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
import com.example.gitget.viewModel.RepoSearchViewModel
import com.example.gitget.utils.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ItemDetailsFragment : DaggerFragment(R.layout.item_details_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by activityViewModels<ItemDetailsViewModel> { viewModelFactory }

    private val navigationArgs: ItemDetailsFragmentArgs by navArgs()

    private lateinit var repo: RepoItem

    private val binding by viewBinding(ItemDetailsFragmentBinding::bind)

    private fun bind(repo: RepoItem) {
        val action = ItemDetailsFragmentDirections.actionItemDetailsFragmentToItemListFragment()
        binding.apply {
            repName.setText(repo.repoName, TextView.BufferType.SPANNABLE)
            repOwnerName.setText(repo.repoOwner, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener {
                findNavController().navigate(action)
                updateItem()
            }
            cancelAction.setOnClickListener {
                findNavController().navigate(action)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        repo = viewModel.retrieveRepo(id)
        bind(repo)
        if (viewModel.allRepoItem.value!![id].lastCommitDate.isBlank()) viewModel.initializeDate(id)
        binding.lastCommitDate.setText(viewModel.date.value, TextView.BufferType.SPANNABLE)
        dateFieldSrcSwitcher(id)
    }

    private fun dateFieldSrcSwitcher(itemId: Int) {
        viewModel.date.observe(this.viewLifecycleOwner) {
            if (viewModel.allRepoItem.value!![itemId].lastCommitDate.isEmpty()) {
                viewModel.date.observe(this.viewLifecycleOwner) {
                    binding.lastCommitDate.setText(it)
                }
            } else {
                viewModel.allRepoItem.observe(this.viewLifecycleOwner) {
                    binding.lastCommitDate.setText(it[itemId].lastCommitDate)
                }
            }
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateRepo(
                repo.id,
                this.binding.repName.text.toString(),
                repo.repoUrl,
                this.binding.repOwnerName.text.toString(),
                this.binding.lastCommitDate.text.toString()
            )
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