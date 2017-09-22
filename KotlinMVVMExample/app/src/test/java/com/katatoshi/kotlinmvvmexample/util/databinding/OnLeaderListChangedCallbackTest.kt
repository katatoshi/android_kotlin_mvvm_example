package com.katatoshi.kotlinmvvmexample.util.databinding

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.katatoshi.kotlinmvvmexample.util.identity
import com.katatoshi.kotlinmvvmexample.util.replace
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class OnLeaderListChangedCallbackTest {

    class 要素の型が同じでリストの初期状態が空でonInsertedMapperのみ指定する場合 {

        private lateinit var leaderList: ObservableList<String>

        private lateinit var followerList: MutableList<String>

        private lateinit var onLeaderListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<String>>

        @Before
        fun setUp() {
            leaderList = ObservableArrayList()
            followerList = leaderList.toMutableList()

            onLeaderListChangedCallback = OnLeaderListChangedCallback(followerList, ::identity)

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストに要素を3回追加すると同じ要素が同じ順番で追従するリストにも追加される() {
            leaderList.add("apple")
            leaderList.add("banana")
            leaderList.add("cherry")

            assertThat(followerList, contains("apple", "banana", "cherry"))
        }

        @Test
        fun 先導するリストに要素を3つ一度に追加すると同じ要素が同じ順番で追従するリストにも追加される() {
            leaderList.addAll(mutableListOf("apple", "banana", "cherry"))

            assertThat(followerList, contains("apple", "banana", "cherry"))
        }

        @After
        fun tearDown() {
            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }
    }

    class 要素の型が同じでリストの初期状態が非空でonInsertedMapperのみ指定する場合 {

        private lateinit var leaderList: ObservableList<String>

        private lateinit var followerList: MutableList<String>

        private lateinit var onLeaderListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<String>>

        @Before
        fun setUp() {
            leaderList = ObservableArrayList()
            leaderList.addAll(mutableListOf("apple", "banana", "cherry"))
            followerList = leaderList.toMutableList()

            onLeaderListChangedCallback = OnLeaderListChangedCallback(followerList, ::identity)

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストに要素を2回追加すると同じ要素が同じ順番で追従するリストにも追加される() {
            leaderList.add("durian")
            leaderList.add("elderberry")

            assertThat(followerList, contains("apple", "banana", "cherry", "durian", "elderberry"))
        }

        @Test
        fun 先導するリストのインデックスが1と2の要素に別の要素をセットすると追従するリストのインデックスが1と2の要素も同じ要素になる() {
            leaderList[1] = "hoge"
            leaderList[2] = "piyo"

            assertThat(followerList, contains("apple", "hoge", "piyo"))
        }

        @Test
        fun 先導するリストの末尾の要素を2回削除すると追従するリストの末尾の要素も2回削除される() {
            leaderList.removeAt(leaderList.size - 1)
            leaderList.removeAt(leaderList.size - 1)

            assertThat(followerList, contains("apple"))
        }

        @Test
        fun 先導するリストをクリアすると追従するリストもクリアされる() {
            leaderList.clear()

            assertThat(followerList, empty())
        }

        @After
        fun tearDown() {
            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }
    }

    class 要素の型が同じでリストの初期状態が非空でonChangedMapperも指定する場合 {

        private lateinit var leaderList: ObservableList<String>

        private lateinit var followerList: MutableList<String>

        private lateinit var onLeaderListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<String>>

        @Before
        fun setUp() {
            leaderList = ObservableArrayList()
            leaderList.addAll(mutableListOf("apple", "banana", "cherry"))
            followerList = leaderList.toMutableList()

            onLeaderListChangedCallback = OnLeaderListChangedCallback(followerList, onChangedMapper = { r, f -> f + r }, onInsertedMapper = ::identity)

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストのインデックスが1と2の要素に別の要素をセットすると追従するリストのインデックスが1と2の要素はセット前の文字列にセットされた文字列が連結された要素になる() {
            leaderList[1] = "hoge"
            leaderList[2] = "piyo"

            assertThat(followerList, contains("apple", "bananahoge", "cherrypiyo"))
        }

        @After
        fun tearDown() {
            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }
    }

    class 要素の型が異なりリストの初期状態が空でonInsertedMapperのみ指定する場合 {

        private lateinit var leaderList: ObservableList<String>

        private lateinit var followerList: MutableList<Int>

        private lateinit var onLeaderListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<String>>

        @Before
        fun setUp() {
            leaderList = ObservableArrayList()
            followerList = leaderList.map { it.count() }.toMutableList()

            onLeaderListChangedCallback = OnLeaderListChangedCallback(followerList) { it.count() }

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストに要素を3回追加すると対応する要素が同じ順番で追従するリストにも追加される() {
            leaderList.add("apple")
            leaderList.add("banana")
            leaderList.add("cherry")

            assertThat(followerList, contains(5, 6, 6))
        }

        @Test
        fun 先導するリストに要素を3つ一度に追加すると対応する要素が同じ順番で追従するリストにも追加される() {
            leaderList.addAll(mutableListOf("apple", "banana", "cherry"))

            assertThat(followerList, contains(5, 6, 6))
        }

        @After
        fun tearDown() {
            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }
    }

    class 要素の型が異なりリストの初期状態が非空でonInsertedMapperのみ指定する場合 {

        private lateinit var leaderList: ObservableList<String>

        private lateinit var followerList: MutableList<Int>

        private lateinit var onLeaderListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<String>>

        @Before
        fun setUp() {
            leaderList = ObservableArrayList()
            leaderList.addAll(mutableListOf("apple", "banana", "cherry"))
            followerList = leaderList.map { it.count() }.toMutableList()

            onLeaderListChangedCallback = OnLeaderListChangedCallback(followerList) { it.count() }

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストに要素を2回追加すると対応する要素が対応する順番で追従するリストにも追加される() {
            leaderList.add("durian")
            leaderList.add("elderberry")

            assertThat(followerList, contains(5, 6, 6, 6, 10))
        }

        @Test
        fun 先導するリストのインデックスが1と2の要素に別の要素をセットすると追従するリストのインデックスが1と2の要素も対応する要素になる() {
            leaderList[1] = "hoge"
            leaderList[2] = "piyo"

            assertThat(followerList, contains(5, 4, 4))
        }

        @Test
        fun 先導するリストの末尾の要素を2回削除すると追従するリストの末尾の要素も2回削除される() {
            leaderList.removeAt(leaderList.size - 1)
            leaderList.removeAt(leaderList.size - 1)

            assertThat(followerList, contains(5))
        }

        @Test
        fun 先導するリストをクリアすると追従するリストもクリアされる() {
            leaderList.clear()

            assertThat(followerList, empty())
        }

        @After
        fun tearDown() {
            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }
    }

    class 要素の型が異なりリストの初期状態が非空でonChangedMapperも指定する場合 {

        private lateinit var leaderList: ObservableList<String>

        private lateinit var followerList: MutableList<Int>

        private lateinit var onLeaderListChangedCallback: ObservableList.OnListChangedCallback<ObservableList<String>>

        @Before
        fun setUp() {
            leaderList = ObservableArrayList()
            leaderList.addAll(mutableListOf("apple", "banana", "cherry"))
            followerList = leaderList.map { it.count() }.toMutableList()

            onLeaderListChangedCallback = OnLeaderListChangedCallback(followerList, onChangedMapper = { r, f -> f + r.count() }, onInsertedMapper = { it.count() })

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストのインデックスが1と2の要素に別の要素をセットすると追従するリストのインデックスが1と2の要素はセット前の文字列の文字数にセットされた文字列の文字数を加えた要素になる() {
            leaderList[1] = "hoge"
            leaderList[2] = "piyo"

            assertThat(followerList, contains(5, 10, 10))
        }

        @After
        fun tearDown() {
            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }
    }

    class replace関数を組み合わせたテスト {

        @Test
        fun 先導するリストをサイズが大きいリストに置き換えると追従するリストも対応するサイズが大きいリストに置き換わる() {
            val leaderList = ObservableArrayList<String>()
            leaderList.addAll(mutableListOf("apple", "banana", "cherry"))
            val followerList = leaderList.map { it.count() }.toMutableList()

            val onLeaderListChangedCallback = OnLeaderListChangedCallback<String, Int>(followerList, onChangedMapper = { r, f -> f + r.count() }, onInsertedMapper = { it.count() })

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)

            leaderList.replace(listOf("Taro", "Jiro", "Saburo", "Shiro", "Goro"))

            assertThat(followerList, contains(9, 10, 12, 5, 4))

            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストをサイズが小さいリストに置き換えると追従するリストも対応するサイズが小さいリストに置き換わる() {
            val leaderList = ObservableArrayList<String>()
            leaderList.addAll(mutableListOf("apple", "banana", "cherry", "durian", "elderberry"))
            val followerList = leaderList.map { it.count() }.toMutableList()

            val onLeaderListChangedCallback = OnLeaderListChangedCallback<String, Int>(followerList, onChangedMapper = { r, f -> f + r.count() }, onInsertedMapper = { it.count() })

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)

            leaderList.replace(listOf("Taro", "Jiro", "Saburo"))

            assertThat(followerList, contains(9, 10, 12))

            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }

        @Test
        fun 先導するリストをサイズが同じリストに置き換えると追従するリストも対応するサイズが同じリストに置き換わる() {
            val leaderList = ObservableArrayList<String>()
            leaderList.addAll(mutableListOf("apple", "banana", "cherry", "durian", "elderberry"))
            val followerList = leaderList.map { it.count() }.toMutableList()

            val onLeaderListChangedCallback = OnLeaderListChangedCallback<String, Int>(followerList, onChangedMapper = { r, f -> f + r.count() }, onInsertedMapper = { it.count() })

            leaderList.addOnListChangedCallback(onLeaderListChangedCallback)

            leaderList.replace(listOf("Taro", "Jiro", "Saburo", "Shiro", "Goro"))

            assertThat(followerList, contains(9, 10, 12, 11, 14))

            leaderList.removeOnListChangedCallback(onLeaderListChangedCallback)
        }
    }
}