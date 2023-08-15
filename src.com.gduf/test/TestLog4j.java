package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import server.DataBaseServer;

import java.net.URL;

public class TestLog4j {
    public static void main(String[] args) {
        ClassLoader classLoader = TestLog4j.class.getClassLoader();

        // 使用 ClassLoader 获取资源的相对路径
        String configFileRelativePath = "log4j.xml";
        URL configFileUrl = classLoader.getResource(configFileRelativePath);

        if (configFileUrl != null) {
            String configFileAbsolutePath = configFileUrl.getPath();
            System.out.println("Log4j XML Config Path: " + configFileAbsolutePath);
            Configurator.initialize(null, configFileAbsolutePath);

        } else {
            System.out.println("Log4j XML Config not found.");
        }
        Logger logger = LogManager.getLogger(DataBaseServer.class);

    }
}
