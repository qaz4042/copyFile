package code.copyfile;

import cn.hutool.core.io.resource.ClassPathResource;

import java.io.IOException;

public class TestApplication {

    /**
     * 根据resources下的copyFile.json来测试
     */
    public static void main(String args[]) throws IOException {
        Application.main(new ClassPathResource("copyFile.json").getStream());
    }
}
