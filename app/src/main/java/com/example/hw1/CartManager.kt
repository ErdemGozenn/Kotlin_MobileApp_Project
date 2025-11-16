package com.erdemgozen.hw1

object CartManager {
    private val items = mutableListOf<CityData>()

    fun addHouse(city: CityData) {
        items.add(city)
    }
    fun getCartItems(): List<CityData> {

        return items.toList()
    }
    fun clearCart() {
        items.clear()
    }
    fun getCartCost():String{
    val totalCost = items.sumOf { it.cityCost }
        return totalCost.toString()
    }

}