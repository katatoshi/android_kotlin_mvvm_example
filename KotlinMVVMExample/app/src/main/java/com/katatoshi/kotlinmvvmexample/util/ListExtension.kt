package com.katatoshi.kotlinmvvmexample.util

/**
 * MutableList の要素を別の List の要素に置き換えます。
 */
fun <E> MutableList<E>.replace(srcList: List<E>) {
    val dstListSize = size
    val srcListSize = srcList.size

    if (dstListSize < srcListSize) {
        for (i in 0 until srcListSize) {
            if (i < dstListSize) {
                set(i, srcList[i])
            } else {
                add(srcList[i])
            }
        }
    } else {
        for (i in 0 until dstListSize) {
            if (i < srcListSize) {
                set(i, srcList[i])
            } else {
                removeAt(srcListSize)
            }
        }
    }
}