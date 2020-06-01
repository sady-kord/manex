package com.baman.manex.network;

import com.baman.manex.data.model.InternetError;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return InternetError.NoInternet.getValue();
    }

}
