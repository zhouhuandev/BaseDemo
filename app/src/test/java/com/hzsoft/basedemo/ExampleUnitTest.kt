package com.hzsoft.basedemo

import com.wx.jetpack.core.utils.fromJson
import com.wx.jetpack.core.utils.toJson
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun moshiJsonTest(){
        // json转对象：
        val a :String = """["1","2","3","4"]"""
        val list = a.fromJson<List<String>>()
        println(list)
        // 对象转json
        val json = list?.toJson()
        print(json)
    }
}