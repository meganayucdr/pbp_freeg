package com.mtz.testwarna.api;

import com.mtz.testwarna.value.GiveawayParticipantValue;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GiveawayParticipantsApi {

    @GET("giveaways/{giveaway_id}/participants")
    Call<GiveawayParticipantValue> getGiveawayParticipantsList(@Path("giveaway_id") int giveawayId);

    @POST("giveaways/{giveaway_id}/participants")
    @FormUrlEncoded
    Call<String> joinGiveaway(@Field("user_id") String userId,
                              @Field("status") String status,
                              @Path("giveaway_id") int giveawayId);
}
