package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestHandlerUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            RequestHandlerUtils handler = new RequestHandlerUtils(in);
            //1단계 - 요청 정보 전체 출력하기
            //handler.printRequestHeader();

            //2단계 - 요청 정보 중 첫번째 line에서 "/index.html" 추출하기
            String path = handler.getRequestURL();
            log.info("path = {}", path);


            //3단계 - 요청 URL에 해당하는 파일을 webapp 디렉토리에 읽은 후 전달
            byte[] requestBody = handler.getRequestBody(path);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, path, requestBody.length);
            responseBody(dos, requestBody);

//            byte[] body = "Hello World".getBytes();
//            response200Header(dos, body.length);
//            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String path, int lengthOfBodyContent) {
        try {
            String contentType = setContentType(path);
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 요청 URL에 맞게 Content-Type 변환하기
     * @param path
     * @return
     */
    private String setContentType(String path) {
        if(path.endsWith("css")) {
            return "text/css";
        } else {
            return "text/html";
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
