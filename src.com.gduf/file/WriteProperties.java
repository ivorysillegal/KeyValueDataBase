package file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WriteProperties {
    public static void main(String[] args) {
        Properties properties = new Properties();

        // 添加配置数据到 Properties 对象
        properties.setProperty("METHODS_PATH", "src.com.gduf\\data\\MethodData.properties");
        properties.setProperty("TYPE_PATH", "src.com.gduf\\data\\TypeData.properties");
        properties.setProperty("STRING_DATA_PATH", "src.com.gduf\\data\\key_value_data\\StringData.properties");
        properties.setProperty("LINKED_LIST_DATA_PATH", "src.com.gduf\\data\\key_value_data\\LinkedListData.properties");
        properties.setProperty("HASH_DATA_PATH", "src.com.gduf\\data\\key_value_data\\HashData.properties");
        properties.setProperty("SET_DATA_PATH", "src.com.gduf\\data\\key_value_data\\SetData.properties");
        properties.setProperty("DATA_PATH_PATH", "src.com.gduf\\data\\PathData.properties");

        properties.setProperty("HOST_NAME","127.0.0.1");
        properties.setProperty("PORT", "8080");
        properties.setProperty("STRING_CLASSNAME", "command.StringCommand");
        properties.setProperty("LINKED_LIST_CLASSNAME", "command.LinkedListCommand");
        properties.setProperty("HASH_CLASSNAME", "command.HashCommand");
        properties.setProperty("SET_CLASSNAME", "command.SetCommand");
        properties.setProperty("IO_CLASSNAME", "command.IOCommand");
        properties.setProperty("DATA_CLASSNAME", "command.DataCommand");

        // 将 Properties 对象保存到文件
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src.com.gduf\\data\\Config.properties");
            properties.store(fileOutputStream, "Configuration Properties");
            fileOutputStream.close();
            System.out.println("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
