package com.katatoshi.kotlinmvvmexample.viewmodel

import android.databinding.ObservableArrayList

class RepositoryListViewModel {

    val exampleList = ObservableArrayList<RepositoryViewModel>()

    init {
        exampleList.addAll(listOf(
                RepositoryViewModel("Item 01"),
                RepositoryViewModel("Item 02"),
                RepositoryViewModel("Item 03"),
                RepositoryViewModel("Item 04"),
                RepositoryViewModel("Item 05"),
                RepositoryViewModel("Item 06"),
                RepositoryViewModel("Item 07"),
                RepositoryViewModel("Item 08"),
                RepositoryViewModel("Item 09"),
                RepositoryViewModel("Item 10"),
                RepositoryViewModel("Item 11"),
                RepositoryViewModel("Item 12"),
                RepositoryViewModel("Item 13"),
                RepositoryViewModel("Item 14"),
                RepositoryViewModel("Item 15"),
                RepositoryViewModel("Item 16"),
                RepositoryViewModel("Item 17"),
                RepositoryViewModel("Item 18"),
                RepositoryViewModel("Item 19"),
                RepositoryViewModel("Item 20")
        ))
    }
}