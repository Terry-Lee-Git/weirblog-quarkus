package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * 反序列化
 * @author weir
 *
 */
public class UserInfoDeserializer extends ObjectMapperDeserializer<UserInfo> {
    public UserInfoDeserializer() {
        super(UserInfo.class);
    }

}
