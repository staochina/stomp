package lifecycle.controller;

import lifecycle.service.AuthService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Desc:
 * Created by sun.tao on 2019/5/5
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    /**
     * @param reqMap
     * @return
     */
    @GetMapping(value = "/info",produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject findfileInfos(@RequestParam(required = false) Map<String,String> reqMap){
        JSONObject params = new JSONObject();
        params.put("userKey","");
        params.put("uid",893);
        params.put("cmd","auth");
        return authService.getUserInfo(params);
    }

}
