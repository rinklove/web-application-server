package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;
import java.nio.file.Files;


public class RequestHandlerUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final BufferedReader br;

    public RequestHandlerUtils(InputStream in) {
        br = new BufferedReader(new InputStreamReader(in));
    }

    /**
     * 1단계 - 요청 정보를 확인
     */
    public void printRequestHeader() {
        String line = "";
        while (true) {
            try {
                if ((line = br.readLine()).isEmpty())
                    break;
                log.info(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 2단계 - 요청 URL 추출
     */
    public String getRequestURL() {
        String line = "";
        String path = "";
        try  {
            if((line = br.readLine()).isEmpty())
                throw new RuntimeException();

            path = line.split(" ")[1];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path.equals("/")? "/index.html" : path;
    }

    public byte[] getRequestBody(String path) {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(new File("./webapp" + path).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

}
