package com.katatoshi.kotlinmvvmexample.view

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.katatoshi.kotlinmvvmexample.BR
import com.katatoshi.kotlinmvvmexample.R
import com.katatoshi.kotlinmvvmexample.databinding.ActivityRepositoryListBinding
import com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview.*
import com.katatoshi.kotlinmvvmexample.viewmodel.RepositoryListViewModel

class RepositoryListActivity : AppCompatActivity() {

    private val viewModel: RepositoryListViewModel by lazy {
        RepositoryListViewModel()
    }

    private val binding: ActivityRepositoryListBinding by lazy {
        DataBindingUtil.setContentView<ActivityRepositoryListBinding>(this, R.layout.activity_repository_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        binding.recyclerView.bindSectionBinderList(listOf(
                SectionBinder(Section("header 1", viewModel.exampleList), VariableLayoutPair(BR.viewModel, R.layout.item_header), VariableLayoutPair(BR.viewModel, R.layout.item_repository)),
                SectionBinder(Section("header 2", viewModel.exampleList), VariableLayoutPair(BR.viewModel, R.layout.item_header), VariableLayoutPair(BR.viewModel, R.layout.item_repository)),
                SectionBinder(Section("header 3", viewModel.exampleList), VariableLayoutPair(BR.viewModel, R.layout.item_header), VariableLayoutPair(BR.viewModel, R.layout.item_repository))
        ))
    }
}
