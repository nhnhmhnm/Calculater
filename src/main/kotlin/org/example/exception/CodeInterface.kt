package org.example.exception

interface CodeInterface {
    val status: Int                  // http status code
    val code: String               // error code
    val message: String         // error message
}
