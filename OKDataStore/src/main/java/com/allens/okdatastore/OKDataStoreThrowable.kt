package com.allens.okdatastore

class OKDataStoreThrowable : Throwable() {
    override val message: String
        get() = "自定义的一个错误.用于中断流"
}