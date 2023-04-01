package com.zong.common.utils.coroutine

import kotlin.coroutines.cancellation.CancellationException

class ActivelyCancelException : CancellationException()