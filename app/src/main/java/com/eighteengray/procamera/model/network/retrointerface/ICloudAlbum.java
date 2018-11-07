package com.eighteengray.procamera.model.network.retrointerface;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ICloudAlbum
{
    @GET("editors_choice/?media_type=photo&pagi=11")
    Call<String> getCloudImageList();
}
