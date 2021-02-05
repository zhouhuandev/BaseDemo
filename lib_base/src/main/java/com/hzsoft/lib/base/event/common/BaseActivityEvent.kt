package com.hzsoft.lib.base.event.common

import com.hzsoft.lib.base.event.BaseEvent

/**
 * 基于事件
 *
 * @author zhouhuan
 * @Data
 */
open class BaseActivityEvent<T>(code: Int) : BaseEvent<T>(code)
