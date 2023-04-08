package com.stone.goods.client;

import com.stone.goods.client.fallback.UserServiceClientFallback;
import com.stone.sdk.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userServiceClient", url = "http://127.0.0.1:8100", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/userService/get/{id}")
    UserDTO getById(@PathVariable Long id);
}
