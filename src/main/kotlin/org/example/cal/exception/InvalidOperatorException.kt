package org.example.cal.exception

class InvalidOperatorException(symbol: String) : RuntimeException("Invalid operator : '$symbol'")