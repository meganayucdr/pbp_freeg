
package com.mtz.testwarna.value;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mtz.testwarna.dao.GiveawayDAO;

public class GiveawayValue {

    @SerializedName("data")
    @Expose
    private List<GiveawayDAO> data = null;

    public List<GiveawayDAO> getData() {
        return data;
    }

    public void setData(List<GiveawayDAO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GiveawayValue{" +
                "data=" + data +
                '}';
    }
}
