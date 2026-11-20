package com.example.listacompra

data class Producto(
    val id: Int,
    val nombre: String,
    val comprado: Boolean = false
)