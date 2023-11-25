package com.codingtester.eatwell.model.repo

import androidx.lifecycle.MutableLiveData
import com.codingtester.eatwell.model.pojo.Address
import com.codingtester.eatwell.model.pojo.Admin
import com.codingtester.eatwell.model.pojo.Category
import com.codingtester.eatwell.model.pojo.Order
import com.codingtester.eatwell.model.pojo.OrderLine
import com.codingtester.eatwell.model.pojo.Point
import com.codingtester.eatwell.model.pojo.Product
import com.codingtester.eatwell.model.pojo.Reserve
import com.codingtester.eatwell.model.pojo.Subscribe
import com.codingtester.eatwell.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataRepository {

    private val databaseRef = FirebaseDatabase.getInstance()

    fun getAllCategories(liveData: MutableLiveData<List<Category>>) {
        databaseRef.getReference("Categories")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val allCategories: List<Category> = snapshot.children.map { data ->
                        data.getValue(Category::class.java)!!
                    }
                    liveData.postValue(allCategories)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getPopularProducts(liveData: MutableLiveData<List<Product>>) {
        databaseRef.getReference("Products").child("Popular")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allProducts: List<Product> = snapshot.children.map { data ->
                        data.getValue(Product::class.java)!!
                    }
                    liveData.postValue(allProducts)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getProducts(liveData: MutableLiveData<List<Product>>, category: String) {
        databaseRef.getReference("Products").child(category)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allProducts: List<Product> = snapshot.children.map { data ->
                        data.getValue(Product::class.java)!!
                    }
                    liveData.postValue(allProducts)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getCart(liveData: MutableLiveData<List<OrderLine>>) {
        databaseRef.getReference("Cart")
            .child(Constants.currentCustomer.id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allCart: List<OrderLine> = snapshot.children.map { data ->
                        data.getValue(OrderLine::class.java)!!
                    }
                    liveData.postValue(allCart)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun addToCart(orderLine: OrderLine, liveData: MutableLiveData<String>) {
        databaseRef.getReference("Cart")
            .child(Constants.currentCustomer.id.toString())
            .child(orderLine.productId)
            .setValue(orderLine)
            .addOnSuccessListener {
                liveData.postValue("Success")
            }
            .addOnFailureListener {
                liveData.postValue("Failed")
            }
    }

    fun calcTotalPrice(liveData: MutableLiveData<Int>) {
        databaseRef.getReference("Cart")
            .child(Constants.currentCustomer.id.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var totalOrderPrice = 0
                    snapshot.children.forEach { snapshot: DataSnapshot? ->
                        val orderLine = snapshot?.getValue(OrderLine::class.java)
                        totalOrderPrice += orderLine?.totalPrice!!
                    }
                    liveData.postValue(totalOrderPrice)

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun deleteOrderLine(liveData: MutableLiveData<String>, productId: String) {
        databaseRef.getReference("Cart")
            .child(Constants.currentCustomer.id.toString())
            .child(productId)
            .removeValue()
            .addOnSuccessListener {
                liveData.postValue("Success")
            }
            .addOnFailureListener {
                liveData.postValue("Failed")
            }
    }

    fun clearCart() {
        databaseRef.getReference("Cart")
            .child(Constants.currentCustomer.id.toString())
            .removeValue()
    }

    fun getAllAddress(liveData: MutableLiveData<List<Address>>) {
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("addresses")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allCart: List<Address> = snapshot.children.map { data ->
                        data.getValue(Address::class.java)!!
                    }
                    liveData.postValue(allCart)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun addNewAddress(liveData: MutableLiveData<String>, address: Address) {
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("addresses")
            .child(address.id)
            .setValue(address)
            .addOnSuccessListener {
                liveData.postValue("Success")
            }
            .addOnFailureListener {
                liveData.postValue("Failed")
            }
    }

    fun removeAddress(address: Address) {
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("addresses")
            .child(address.id)
            .removeValue()
    }

    fun sendOrder(order: Order, liveData: MutableLiveData<String>) {
        databaseRef.getReference("Orders")
            .child(order.id)
            .setValue(order)
            .addOnSuccessListener {
                liveData.postValue("Success")
            }.addOnFailureListener {
                liveData.postValue("failed")
            }
    }

    fun sendReserve(reserve: Reserve, liveData: MutableLiveData<String>) {
        databaseRef.getReference("Reserve")
            .child(reserve.id)
            .setValue(reserve)
            .addOnSuccessListener {
                liveData.postValue("Success")
            }.addOnFailureListener {
                liveData.postValue("failed")
            }
    }

    fun getAllReserve(liveData: MutableLiveData<List<Reserve>>) {
        databaseRef.getReference("Reserve")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allReserve: List<Reserve> = snapshot.children.map { data ->
                        data.getValue(Reserve::class.java)!!
                    }
                    liveData.postValue(allReserve.filter { it.customer.id == Constants.currentCustomer.id })
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getAllOrders(liveData: MutableLiveData<List<Order>>) {
        databaseRef.getReference("Orders")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allOrders: List<Order> = snapshot.children.map { data ->
                        data.getValue(Order::class.java)!!
                    }
                    liveData.postValue(allOrders.filter { it.customer?.id == Constants.currentCustomer.id })
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getPointItems(liveData: MutableLiveData<List<Point>>) {
        databaseRef.getReference("Points")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allCart: List<Point> = snapshot.children.map { data ->
                        data.getValue(Point::class.java)!!
                    }
                    liveData.postValue(allCart)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getAllAdminTokens(liveData: MutableLiveData<List<String>>) {
        databaseRef.getReference("Admins")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<String>()
                    snapshot.children.forEach {
                        val admin = it.getValue(Admin::class.java)
                        list.add(admin?.token.toString())
                    }
                    liveData.postValue(list)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun sendPointRequest(point: Point, liveData: MutableLiveData<String>) {
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("pointRequest")
            .push()
            .setValue(point)
            .addOnSuccessListener {
                liveData.postValue("Success")
            }.addOnFailureListener {
                liveData.postValue("failed")
            }
    }

    fun getPointRequest(liveData: MutableLiveData<List<Point>>) {
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("pointRequest")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allRequests: List<Point> = snapshot.children.map { data ->
                        data.getValue(Point::class.java)!!
                    }
                    liveData.postValue(allRequests)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getUserPoints(liveData: MutableLiveData<String>) {
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("points")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val totalPoints = snapshot.getValue(String::class.java)!!
                    liveData.postValue(totalPoints)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun calcPointsAfterSendReward(pointCount: String) {
        val totalPoints = (Constants.currentCustomer.points.toInt() - pointCount.toInt())
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("points")
            .setValue(totalPoints.toString())
    }

    fun calcPointsAfterSendOrder(pointCount: String) {
        val totalPoints = (Constants.currentCustomer.points.toInt() + pointCount.toInt())
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .child("points")
            .setValue(totalPoints.toString())
    }

    fun getSubscribeItems(liveData: MutableLiveData<List<Subscribe>>) {
        databaseRef.getReference("Subscribe")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allCart: List<Subscribe> = snapshot.children.map { data ->
                        data.getValue(Subscribe::class.java)!!
                    }
                    liveData.postValue(allCart)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun addSubscribeModel(subscribeTitle: String, liveData: MutableLiveData<String>) {
        val hashData = HashMap<String, Any>()
        hashData["subscribeModel"] = subscribeTitle
        hashData["subscribeDate"] = System.currentTimeMillis().toString()
        hashData["isSubscribe"] = true
        databaseRef.getReference("Users")
            .child(Constants.currentCustomer.id.toString())
            .updateChildren(hashData)
            .addOnSuccessListener {
                liveData.postValue("Success")
            }.addOnFailureListener {
                liveData.postValue("failed")
            }
    }

    fun getSubscribePercentage(liveData: MutableLiveData<String>) {
        databaseRef.getReference("Subscribe")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.map { data ->
                        val subscribe = data.getValue(Subscribe::class.java)!!
                        if(subscribe.title == Constants.currentCustomer.subscribeModel) {
                            liveData.postValue(subscribe.percentageDiscount)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}