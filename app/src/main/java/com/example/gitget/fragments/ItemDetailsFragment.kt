package com.example.gitget.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitget.data.RepoItem
import com.example.gitget.databinding.ItemDetailsFragmentBinding
import com.example.gitget.viewModel.RepoSearchViewModel
import com.example.gitget.viewModel.RepoSearchViewModelFactory

class ItemDetailsFragment : Fragment() {

    private val viewModel: RepoSearchViewModel by activityViewModels {
        RepoSearchViewModelFactory()
    }
    private val navigationArgs: ItemDetailsFragmentArgs by navArgs()

    private lateinit var repo: RepoItem

    private var _binding: ItemDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(repo: RepoItem) {
        val action = ItemDetailsFragmentDirections.actionItemDetailsFragmentToItemListFragment()
        binding.apply {
            repName.setText(repo.repoName, TextView.BufferType.SPANNABLE)
            repOwnerName.setText(repo.repoOwner, TextView.BufferType.SPANNABLE)
            lastCommitDate.setText(repo.lastCommitDate, TextView.BufferType.SPANNABLE)
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
        val name = navigationArgs.itemName
        viewModel.getCurrentRepoItemIndex(name)
        repo = viewModel.retrieveRepo()
        bind(repo)
        viewModel.initializeDate()
        viewModel.date2.observe(this.viewLifecycleOwner) {
            binding.lastCommitDate.setText(it, TextView.BufferType.SPANNABLE)
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateRepo(
                this.binding.repName.text.toString(),
                repo.repoUrl,
                this.binding.repOwnerName.text.toString(),
                this.binding.lastCommitDate.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
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