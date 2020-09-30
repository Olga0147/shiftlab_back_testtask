package httpWorker;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dao.H2DBWorker.*;

public class Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String result;

        if("GET".equals(httpExchange.getRequestMethod())) {
            result = handleGetRequest(httpExchange);
        } else if("POST".equals(httpExchange.getRequestMethod())) {
            result = handlePostRequest(httpExchange);
        } else{
            result = "Incorrect method (need GET or POST)";
        }
        handleResponse(httpExchange,result);
    }

    private String handleGetRequest(HttpExchange httpExchange) {

        String[] splitURL = httpExchange.getRequestURI().toString().split("\\?");
        String str;

        if(splitURL.length == 1){
            str = "Hello!";
        }
        else{
            String[] param = splitURL[1].split("=");

            switch (param[0]){
                case "type":
                    str = selectByType(param[1]);
                    break;
                case "id":
                    str = selectById(Integer.valueOf(param[1]));
                    break;
                default:
                    str = "Incorrect param in URL (need type or id)";
            }

        }
        return str;
    }

    private String handlePostRequest(HttpExchange exchange) {

        String postBody;
        try (InputStream in = exchange.getRequestBody()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                out.write(buf, 0, n);
            }
            postBody = new String(out.toByteArray(), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            return e.getMessage();
        }

        Map<String, String> params = new HashMap<>();
        String action = "";
        String tableName = "";

        Pattern MY_PATTERN = Pattern.compile("name=\"\\w+\"\\r\\n\\r\\n\\w+\\r\\n");
        Matcher m = MY_PATTERN.matcher(postBody);
        while (m.find()) {
            String s = m.group();
            if(!s.equals("")){
                s=s.substring(6,s.length()-2);
                String[] str = s.split("\"\\r\\n\\r\\n");

                if(str[0].equals("action")){
                    action = str[1];
                }else if(str[0].equals("table")){
                    tableName= str[1];
                }
                else{
                    params.put(str[0],str[1]);
                }
            }
        }

        if(tableName.equals("") || params.isEmpty()){
            return  "Incorrect POST body";
        }

        String result;

        switch (action){
            case "insert":
                result = insertH2(tableName,params);
                break;
            case "update":
                result = updateH2(tableName,params);
                break;
            default:
                result = "Incorrect param in POST (action =  insert or update)";
        }

        return result;
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {

        String htmlResponse = "<html><h3>" +
                requestParamValue +
                "</h3></html>";

        byte[] bs = htmlResponse.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, bs.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(bs);
        os.flush();
        os.close();
    }
}
