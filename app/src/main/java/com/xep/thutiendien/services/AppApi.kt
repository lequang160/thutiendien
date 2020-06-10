package com.xep.thutiendien.services

import com.xep.thutiendien.entries.OrderEntry
import com.xep.thutiendien.entries.StatusEntry
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {
    @GET("/api/thutiendien/danhsachkhachhang")
    suspend fun fetchOrderList(@Query("isStatus") status: String, @Query("Page") page: String): List<OrderEntry>

    @GET("/api/thutiendien/thutienkhachhang")
    suspend fun updateStatusOrder(@Query("idThuTien") id: String,
                                  @Query("TrangThai") status: String) : StatusEntry
}