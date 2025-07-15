package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtHelper {

    public static String getTokenFromBearer(String bearerToken){
        return bearerToken.replace("Bearer ", "");
    }
}
