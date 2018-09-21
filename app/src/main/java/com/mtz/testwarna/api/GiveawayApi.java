package com.mtz.testwarna.api;

import com.mtz.testwarna.value.GiveawayValue;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GiveawayApi {
    @GET("giveaways")
    Call<GiveawayValue> getAllGiveaways();

    @GET("giveaways/{user_id}")
    Call<GiveawayValue> getGiveawaysByUser(@Path("user_id") String user_id);

    @POST("giveaways")
    @FormUrlEncoded
    Call<String> addGiveaway(@Field("user_id") String user_id,
                             @Field("content") String content,
                             @Field("image") String image,
                             @Field("participants") int participants,
                             @Field("status") String status);

    @PUT("giveaways/{giveaway_id}")
    @FormUrlEncoded
    Call<String> updateGiveaway(@Field("user_id") String user_id,
                                @Field("content") String content,
                                @Field("image") String image,
                                @Field("participants") int participants,
                                @Field("status") String status,
                                @Path("giveaway_id") int giveaway_id);

    @PUT("giveaways/{giveaway_id}")
    @FormUrlEncoded
    Call<String> deleteGiveaway( @Field("status") String status,
                                 @Path("giveaway_id") int giveaway_id);
}
