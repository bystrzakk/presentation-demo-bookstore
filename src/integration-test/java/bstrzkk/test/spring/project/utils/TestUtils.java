package bstrzkk.test.spring.project.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class TestUtils {

    private static String getFixtureAsString(String fixtureName) throws IOException {
        return Files.asCharSource(new File(fixtureName), Charsets.UTF_8).read();
    }

    public static String getJsonBodyFromFile(String pathToJsonFile) {
        String body = null;
        try {
            body = getFixtureAsString(pathToJsonFile);
        } catch (IOException e) {
            log.warn("There was a problem retrieving json response data from the source file! [{}]", pathToJsonFile, e);
        }
        return body;
    }
}
