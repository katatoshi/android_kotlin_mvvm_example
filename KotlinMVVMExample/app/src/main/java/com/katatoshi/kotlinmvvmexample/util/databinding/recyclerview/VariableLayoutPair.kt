package com.katatoshi.kotlinmvvmexample.util.databinding.recyclerview

import android.support.annotation.LayoutRes

/**
 * variableId と layoutId のペア。
 */
data class VariableLayoutPair(val variableId: Int, @LayoutRes val layoutId: Int)