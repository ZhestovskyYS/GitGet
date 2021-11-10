package com.example.gitget.presentation.fragments.itemList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gitget.R
import com.example.gitget.databinding.ItemListFragmentBinding
import com.example.gitget.presentation.fragments.itemList.adapter.ItemListAdapter
import com.example.gitget.viewModel.RepoSearchViewModel
import com.example.gitget.utils.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ItemListFragment
    : DaggerFragment(R.layout.item_list_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by activityViewModels<ItemListViewModel> { viewModelFactory }

    private val binding by viewBinding(ItemListFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action =
                ItemListFragmentDirections.actionItemListFragmentToItemDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter/*
        binding.recyclerView.addItemDecoration()*/
        viewModel.allRepoItem.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
        binding.searchAction.setOnClickListener {
            viewModel.clearAllRepoItem()
            val searchText = binding.repoNameField.text.toString()
            viewModel.initializeInfo(searchText)
        }
    }
}