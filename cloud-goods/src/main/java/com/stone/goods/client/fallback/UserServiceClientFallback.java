package com.stone.goods.client.fallback;

import com.stone.goods.client.UserServiceClient;
import com.stone.sdk.user.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserDTO getById(Long id) {
        throw new RuntimeException("网络异常，请稍后重试");
    }

}
