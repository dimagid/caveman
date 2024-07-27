package example;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        var server = HttpServer.create(new InetSocketAddress(9999), 0);
        server.createContext("/", exchange -> {
            var headers = exchange.getResponseHeaders();
            headers.put("Content-Type", List.of("text/html"));

            exchange.sendResponseHeaders(200, 0);
            try (var os = exchange.getResponseBody()) {
                os.write("Hello, world".getBytes());
            }
        });

        server.start();
    }
}
