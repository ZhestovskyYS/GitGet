package com.example.gitget.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitget.databinding.ItemListFragmentBinding
import com.example.gitget.viewModel.RepoSearchViewModel
import com.example.gitget.viewModel.RepoSearchViewModelFactory

class ItemListFragment : Fragment() {
    private val viewModel: RepoSearchViewModel by activityViewModels {
        RepoSearchViewModelFactory()
    }

    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = ItemListFragmentBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action =
                ItemListFragmentDirections.actionItemListFragmentToItemDetailsFragment(it.repoName)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter/*
        binding.recyclerView.addItemDecoration()*/
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allRepoItem.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
        binding.searchAction.setOnClickListener {
            viewModel.searchText = binding.repoNameField.text.toString()
            viewModel.initializeInfo()
        }


    }


}