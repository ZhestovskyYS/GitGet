package com.example.gitget.presentation.fragments.itemList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gitget.R
import com.example.gitget.databinding.ItemListFragmentBinding
import com.example.gitget.presentation.fragments.itemDetails.ItemDetailsFragmentArgs
import com.example.gitget.presentation.fragments.itemList.adapter.ItemListAdapter
import com.example.gitget.utils.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ItemListFragment
    : DaggerFragment(R.layout.item_list_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by activityViewModels<ItemListViewModel> { viewModelFactory }

    private val navArgs: ItemDetailsFragmentArgs by navArgs()

    private val binding by viewBinding(ItemListFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: Bundle? = arguments
        val date = args?.getString("date")
        if (!date.isNullOrBlank())
            viewModel.updateRepo(
                id = navArgs.itemId,
                repoName = navArgs.repoName,
                repoOwner = navArgs.repoOwner,
                date = navArgs.date
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action = ItemListFragmentDirections
                .actionItemListFragmentToItemDetailsFragment(
                    itemId = it.id,
                    repoName = it.repoName,
                    repoOwner = it.repoOwner,
                    date = it.lastCommitDate
                )
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("LifeCycle", "View is destroyed")
    }
}