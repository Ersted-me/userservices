package com.ersted.userservices.utils;

import com.ersted.userservices.enums.ResponseStatus;
import ru.ersted.common.dto.ResponseDto;

public class ResponseDataUtils {
    public static ResponseDto success(String id, String message){
        return new ResponseDto(ResponseStatus.SUCCESS.name(), message, id);
    }

    public static ResponseDto failed(String id, String message){
        return new ResponseDto(ResponseStatus.FAILED.name(), message, id);
    }
}
