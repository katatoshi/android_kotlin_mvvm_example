package com.katatoshi.kotlinmvvmexample.view

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.katatoshi.kotlinmvvmexample.BR
import com.katatoshi.kotlinmvvmexample.R
import com.katatoshi.kotlinmvvmexample.databinding.ActivityRepositoryListBinding
import com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview.VariableLayoutPair
import com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview.bind
import com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview.bindSortable
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

        binding.recyclerView.bindSortable(viewModel.exampleList, VariableLayoutPair(BR.viewModel, R.layout.item_repository), R.id.text_view_language) {
            Toast.makeText(this, it.fullName, Toast.LENGTH_SHORT).show()
        }
    }
}
