package com.stone.goods.client;

import com.stone.goods.client.fallback.UserServiceClientFallback;
import com.stone.sdk.user.api.CloudUserApi;
import com.stone.sdk.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = CloudUserApi.CLOUD_USER_FEIGN_CLIENT_NAME, url = CloudUserApi.CLOUD_USER_FEIGN_URL, fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = CloudUserApi.API_GET_ID)
    UserDTO getById(@PathVariable Long id);
}
