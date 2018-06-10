package com.open.framework.commmon.utils;

import com.open.framework.commmon.BaseConstant;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;


public class YamlUtils {


    private static YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();

    public static void setSource(Resource resource) {
        yaml.setResources(resource);
    }

    public static Properties DefaultYaml2Properties() {
        try {
            Properties properties = yaml.getObject();
            if (properties.isEmpty()) {
                yaml.setResources(new ClassPathResource(BaseConstant.DEFAULT_CONFIG));
            }
            return yaml.getObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String DefaultGetYamlValue(String key) {
        Properties properties = DefaultYaml2Properties();
        if (null != properties) {
            String value = properties.getProperty(key);
            return value;
        }
        return null;
    }

    public static Properties readYaml2Properties(String path) {
        try {
            YamlPropertiesFactoryBean readYaml = new YamlPropertiesFactoryBean();
            readYaml.setResources(new ClassPathResource(path));
            return yaml.getObject();
        } catch (Exception e) {
            return null;
        }
    }
}
