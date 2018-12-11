package com.testapp.onetwotriptestapplication.data.remote

/**
 * Created by Ilya Bakerin on 12/10/18.
 */
class Resource<out T>(val status: Status, val data: T?, val message: String?)