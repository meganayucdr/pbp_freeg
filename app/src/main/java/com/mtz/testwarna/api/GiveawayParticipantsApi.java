package com.mtz.testwarna.api;

import com.mtz.testwarna.dao.GiveawayParticipantDAO;
import com.mtz.testwarna.value.GiveawayParticipantValue;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

//list partisipan yang ikut giveaway
public interface GiveawayParticipantsApi {

    @GET("giveaways/{giveaway_id}/participants")
    Call<GiveawayParticipantValue> getGiveawayParticipantsList(@Path("giveaway_id") int giveawayId);

    @POST("giveaways/{giveaway_id}/participants")
    @FormUrlEncoded
    Call<GiveawayParticipantDAO> joinGiveaway(@Field("user_id") String userId,
                                              @Path("giveaway_id") int giveawayId);
}
