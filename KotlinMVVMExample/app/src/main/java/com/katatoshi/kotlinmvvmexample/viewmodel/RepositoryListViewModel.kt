package com.katatoshi.kotlinmvvmexample.viewmodel

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.alwaysUi
import nl.komponents.kovenant.ui.successUi

class RepositoryListViewModel {

    val exampleList = ObservableArrayList<RepositoryViewModel>()

    val needPaging = ObservableBoolean(true)

    val loading = ObservableBoolean(false)

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

    fun loadPage() {
        if (loading.get()) {
            return
        }

        if (!needPaging.get()) {
            return
        }

        loading.set(true)

        task {
            Thread.sleep(2000)
        } successUi {
            exampleList.addAll(listOf(
                    RepositoryViewModel("Item 21"),
                    RepositoryViewModel("Item 22"),
                    RepositoryViewModel("Item 23"),
                    RepositoryViewModel("Item 24"),
                    RepositoryViewModel("Item 25"),
                    RepositoryViewModel("Item 26"),
                    RepositoryViewModel("Item 27"),
                    RepositoryViewModel("Item 28"),
                    RepositoryViewModel("Item 29"),
                    RepositoryViewModel("Item 30"),
                    RepositoryViewModel("Item 31"),
                    RepositoryViewModel("Item 32"),
                    RepositoryViewModel("Item 33"),
                    RepositoryViewModel("Item 34"),
                    RepositoryViewModel("Item 35"),
                    RepositoryViewModel("Item 36"),
                    RepositoryViewModel("Item 37"),
                    RepositoryViewModel("Item 38"),
                    RepositoryViewModel("Item 39"),
                    RepositoryViewModel("Item 40")
            ))

            needPaging.set(false)
        } alwaysUi {
            loading.set(false)
        }
    }
}