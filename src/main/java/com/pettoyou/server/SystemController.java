package com.pettoyou.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {

    /***
     * AWS의 ALB중 HealthCheck를 위해 만들어놓은 컨트롤러 입니다.
     * @return -> 무조건 200 응답을 리턴해줍니다.
     */
    @GetMapping("/")
    public ResponseEntity<HttpStatus> healthCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
