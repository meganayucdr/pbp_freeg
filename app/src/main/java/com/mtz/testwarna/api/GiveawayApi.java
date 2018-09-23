package com.mtz.testwarna.api;

import com.mtz.testwarna.dao.GiveawayDAO;
import com.mtz.testwarna.value.GiveawayValue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GiveawayApi {
    @GET("giveaways")
    Call<GiveawayValue> getAllGiveaways();

    @GET("giveaways/show/{user_id}")
    Call<GiveawayValue> getGiveawaysByUser(@Path("user_id") String user_id);

    @GET("giveaways/{user_id/joined")
    Call<GiveawayValue> getJoinedGiveaway(@Path("user_id") String user_id);

    @GET("giveaways/{user_id/won")
    Call<GiveawayValue> getWonGiveaway(@Path("user_id") String user_id);

    @POST("giveaways")
    @FormUrlEncoded
    Call<GiveawayDAO> addGiveaway(@Field("user_id") String user_id,
                                  @Field("description") String description,
                                  @Field("image") String image,
                                  @Field("location") String location,
                                  @Field("participants") int participants,
                                  @Field("status") String status);

    @PUT("giveaways/{giveaway_id}")
    @FormUrlEncoded
    Call<GiveawayDAO> updateGiveaway(@Field("user_id") String user_id,
                                @Field("description") String description,
                                @Field("image") String image,
                                @Field("participants") int participants,
                                @Field("status") String status,
                                @Path("giveaway_id") int giveaway_id);

    @DELETE("giveaways/{giveaway_id}")
    @FormUrlEncoded
    Call<ResponseBody> deleteGiveaway(@Path("giveaway_id") int giveaway_id);

}
