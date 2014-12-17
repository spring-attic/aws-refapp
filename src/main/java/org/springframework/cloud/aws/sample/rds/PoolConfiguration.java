package org.springframework.cloud.aws.sample.rds;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.aws.jdbc.datasource.TomcatJdbcDataSourceFactory;
import org.springframework.stereotype.Component;

/**
 * @author Alain Sahli
 * @since 1.0
 */
@Component
public class PoolConfiguration implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TomcatJdbcDataSourceFactory) {
            TomcatJdbcDataSourceFactory tomcatJdbcDataSourceFactory = (TomcatJdbcDataSourceFactory) bean;
            tomcatJdbcDataSourceFactory.setTestOnBorrow(true);
            tomcatJdbcDataSourceFactory.setValidationQuery("SELECT 1");
        }
        return bean;
    }
}
