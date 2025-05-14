package com.example.test
data class CodeSnippet(
        val id: Int,
        val text: String,
        val correctPosition: Int,
        var currentPosition: Int
    )
