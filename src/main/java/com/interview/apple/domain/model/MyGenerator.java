package com.interview.apple.domain.model;

import com.interview.apple.config.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class MyGenerator implements IdentifierGenerator, Configurable {
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        // empty
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return RandomStringUtils.randomAlphanumeric(Constants.ID_LENGTH);
    }
}
