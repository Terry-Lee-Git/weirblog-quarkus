package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * εεΊεε
 * @author weir
 *
 */
public class UserInfoDeserializer extends ObjectMapperDeserializer<UserInfo> {
    public UserInfoDeserializer() {
        super(UserInfo.class);
    }

}
