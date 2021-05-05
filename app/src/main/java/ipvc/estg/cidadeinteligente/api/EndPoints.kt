package ipvc.estg.cidadeinteligente.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @GET("/auth/")
    fun getAuth():Call<List<User>>

    @GET("/auth/{id}")
    fun getAuthById(@Path("id")id: Int):Call<User>

    @FormUrlEncoded
    @POST("api/auth/new")
    fun userLogin(@Field("username") username :String, @Field("password") password: String): Call<User>
}