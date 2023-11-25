package com.codingtester.eatwell.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingtester.eatwell.model.pojo.Address
import com.codingtester.eatwell.model.pojo.Category
import com.codingtester.eatwell.model.pojo.Order
import com.codingtester.eatwell.model.pojo.OrderLine
import com.codingtester.eatwell.model.pojo.Point
import com.codingtester.eatwell.model.pojo.Product
import com.codingtester.eatwell.model.pojo.Reserve
import com.codingtester.eatwell.model.pojo.Subscribe
import com.codingtester.eatwell.model.repo.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(private val repository: DataRepository): ViewModel() {

    private val category: MutableLiveData<List<Category>> = MutableLiveData()
    val categoryLiveData: LiveData<List<Category>> = category

    private val popular: MutableLiveData<List<Product>> = MutableLiveData()
    val popularLiveData: LiveData<List<Product>> = popular

    private val products: MutableLiveData<List<Product>> = MutableLiveData()
    val productsLiveData: LiveData<List<Product>> = products

    private val orderLine: MutableLiveData<String> = MutableLiveData()
    val orderLineLiveData: LiveData<String> = orderLine

    private val cart: MutableLiveData<List<OrderLine>> = MutableLiveData()
    val cartLiveData: LiveData<List<OrderLine>> = cart

    private val orderTotalPrice: MutableLiveData<Int> = MutableLiveData()
    val orderTotalPriceLiveData: LiveData<Int> = orderTotalPrice

    private val deleteOrderLine: MutableLiveData<String> = MutableLiveData()

    private val getAddress: MutableLiveData<List<Address>> = MutableLiveData()
    val getAddressLiveData: LiveData<List<Address>> = getAddress

    private val addAddress: MutableLiveData<String> = MutableLiveData()

    private val sendOrder: MutableLiveData<String> = MutableLiveData()
    val sendOrderLiveData: LiveData<String> = sendOrder

    private val getOrders: MutableLiveData<List<Order>> = MutableLiveData()
    val getAllOrdersLiveData: LiveData<List<Order>> = getOrders

    private val getPoints: MutableLiveData<List<Point>> = MutableLiveData()
    val getPointsLiveData: LiveData<List<Point>> = getPoints

    private val sendPointRequest: MutableLiveData<String> = MutableLiveData()
    val sendPointsRequestLiveData: LiveData<String> = sendPointRequest

    private val getUserPoints: MutableLiveData<String> = MutableLiveData()
    val getUserPointsLiveData: LiveData<String> = getUserPoints

    private val getPointsRequests: MutableLiveData<List<Point>> = MutableLiveData()
    val getPointsRequestsLiveData: LiveData<List<Point>> = getPointsRequests

    private val getSubscribeItems: MutableLiveData<List<Subscribe>> = MutableLiveData()
    val getSubscribeItemsLiveData: LiveData<List<Subscribe>> = getSubscribeItems

    private val addSubscribeItem: MutableLiveData<String> = MutableLiveData()
    val addSubscribeItemsLiveData: LiveData<String> = addSubscribeItem

    private val getSubscribePercentage: MutableLiveData<String> = MutableLiveData()
    val getSubscribePercentageLiveData: LiveData<String> = getSubscribePercentage

    private val getAdminsToken: MutableLiveData<List<String>> = MutableLiveData()
    val getAdminsTokenLiveData: LiveData<List<String>> = getAdminsToken

    private val getReserved: MutableLiveData<List<Reserve>> = MutableLiveData()
    val getReservedLiveData: LiveData<List<Reserve>> = getReserved

    private val setReserve: MutableLiveData<String> = MutableLiveData()
    val setReserveLiveData: LiveData<String> = setReserve

    fun getAllCategories() {
        repository.getAllCategories(category)
    }

    fun getPopularData() {
        repository.getPopularProducts(popular)
    }

    fun getProductsByCategory(category: String) {
        repository.getProducts(products, category)
    }

    fun addToCart(line: OrderLine) {
        repository.addToCart(line, orderLine)
    }

    fun getCart() {
        repository.getCart(cart)
    }

    fun getOrderPrices() {
        repository.calcTotalPrice(orderTotalPrice)
    }

    fun deleteOrderLine(orderId: String) {
        repository.deleteOrderLine(deleteOrderLine, orderId)
    }

    fun getAddressData() {
        repository.getAllAddress(getAddress)
    }

    fun addNewAddress(address: Address) {
        repository.addNewAddress(addAddress, address)
    }

    fun removeAddress(address: Address) {
        repository.removeAddress(address)
    }

    fun sendOrder(order: Order) {
        repository.sendOrder(order,sendOrder)
    }

    fun clearCart() {
        repository.clearCart()
    }

    fun getAllOrders() {
        repository.getAllOrders(getOrders)
    }

    fun getPoints() {
        repository.getPointItems(getPoints)
    }

    fun sendPointRequest(point: Point) {
        repository.sendPointRequest(point, sendPointRequest)
    }

    fun getPointRequest() {
        repository.getPointRequest(getPointsRequests)
    }

    fun getUserPoints() {
        repository.getUserPoints(getUserPoints)
    }

    fun calcPointsAfterSendRewards(pointTotal: String) {
        repository.calcPointsAfterSendReward(pointTotal)
    }

    fun calcPointsAfterSendOrder(pointTotal: String) {
        repository.calcPointsAfterSendOrder(pointTotal)
    }

    fun getSubscribeItems() {
        repository.getSubscribeItems(getSubscribeItems)
    }

    fun addSubscribeItem(title: String) {
        repository.addSubscribeModel(title, addSubscribeItem)
    }

    fun getSubscribePercentage() {
        repository.getSubscribePercentage(getSubscribePercentage)
    }

    fun getAllAdminTokens() {
        repository.getAllAdminTokens(getAdminsToken)
    }

    fun sendReserveToServer(reserve: Reserve) {
        repository.sendReserve(reserve, setReserve)
    }

    fun getAllReserve() {
        repository.getAllReserve(getReserved)
    }
}