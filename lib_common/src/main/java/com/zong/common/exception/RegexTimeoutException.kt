package com.zong.common.exception

import com.zong.common.exception.NoStackTraceException

class RegexTimeoutException(msg: String) : NoStackTraceException(msg)