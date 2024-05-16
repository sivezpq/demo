package com.website.framework.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperty {
    Log log = LogFactory.getLog(ApplicationProperty.class);
    private Properties properties = null;

    public ApplicationProperty() {
    }

    public void readFromClassLoader(String filename){
        properties = new Properties();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
                filename);
        try {
            properties.load(stream);
        } catch (IOException e) {
            log.error("",e);
        }
    }

    public ApplicationProperty readFromClassPath(String filename){
        properties = new Properties();
        ClassPathResource resource = new ClassPathResource(filename);
        try {
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            log.error("",e);
        }
        return this;
    }

    public String getValue(String key){
        return (String) properties.get(key);
    }
}
