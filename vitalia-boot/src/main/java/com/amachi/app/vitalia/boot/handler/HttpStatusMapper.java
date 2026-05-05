package com.amachi.app.vitalia.boot.handler;

import com.amachi.app.core.common.http.HttpStatusCode;
import org.springframework.http.HttpStatus;

public final class HttpStatusMapper {

    public static HttpStatus toSpringStatus(HttpStatusCode status) {
        if (status == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpStatus resolved = HttpStatus.resolve(status.getCode());
        return (resolved != null) ? resolved : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}