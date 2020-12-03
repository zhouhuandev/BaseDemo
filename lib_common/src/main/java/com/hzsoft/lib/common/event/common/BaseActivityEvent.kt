package com.hzsoft.lib.common.event.common

import com.hzsoft.lib.common.event.BaseEvent

/**
 * 基于事件
 *
 * @author zhouhuan
 * @Data
 */
class BaseActivityEvent<T>(code: Int) : BaseEvent<T>(code)
