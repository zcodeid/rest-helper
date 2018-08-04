package id.zcode.rest.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ZCodeResponse extends ResponseEntity {

    private Object body;

    public ZCodeResponse(@Nullable Object body, HttpStatus status) {
        super(body, status);
        this.body = body;
    }

    public ZCodeResponse(HttpStatus status) {
        super(status);
    }

    @Override
    public Object getBody(){
        if (body instanceof String){
            Map map = new HashMap();
            map.put("message", body);
            return map;
        }else{
            return body;
        }
    }
}
