package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


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
        return path;
    }



}
