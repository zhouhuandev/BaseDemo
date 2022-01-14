package com.hzsoft.lib.base.module.event

/**
 * EventCode
 *
 * @author zhouhuan
 * @time 2022/1/14
 */
interface EventCode {
    interface MainCode//1000开始

    interface NewsCode//2000开始

    interface FindCode//3000开始

    interface MeCode {
        companion object {
            //4000开始
            val NEWS_TYPE_ADD = 4000
            val NEWS_TYPE_DELETE = 4001
            val NEWS_TYPE_UPDATE = 4002
            val NEWS_TYPE_QUERY = 4003

            val news_detail_add = 4004
        }
    }
}
