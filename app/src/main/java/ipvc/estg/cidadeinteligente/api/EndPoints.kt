package ipvc.estg.cidadeinteligente.api

import android.app.DownloadManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @GET("api/report/")
    fun getReports():Call<List<ReportOutpost>>

    @GET("api/report/{id}")
    fun getreportById(@Path("id")id: Int):Call<ReportOutpost>

    @FormUrlEncoded
    @POST("api/auth")
    fun userLogin(@Field("username") username: String?,
                  @Field("password") password: String?): Call<User>

    @Multipart
    @POST("api/report/new")
    fun addReport(@Part("title") title:RequestBody,
                  @Part("user_name") user_name:RequestBody,
                  @Part("lat")lat: RequestBody,
                  @Part("lng")lng: RequestBody,
                  @Part("description")description: RequestBody,
                  @Part photo_name: MultipartBody.Part,
                  @Part("type")type: RequestBody): Call<ReportOutpost>

    @FormUrlEncoded
    @POST("api/deleteReport")
    fun deleteReport(@Field("id")id:Int):Call<ReportOutpost>

    @FormUrlEncoded
    @POST("api/editReport")
    fun editReport(@Field("id")id:Int,
                   @Field("title")title:String?,
                   @Field("description")description:String?):Call<ReportOutpost>
}