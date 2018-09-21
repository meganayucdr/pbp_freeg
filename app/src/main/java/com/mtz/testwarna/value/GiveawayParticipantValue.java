
package com.mtz.testwarna.value;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiveawayParticipantValue {

    @SerializedName("data")
    @Expose
    private List<GiveawayParticipantDAO> data = null;

    public List<GiveawayParticipantDAO> getData() {
        return data;
    }

    public void setData(List<GiveawayParticipantDAO> data) {
        this.data = data;
    }

}
