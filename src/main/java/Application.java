import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Application {
    public static void main(String[] args) {
        int serverPort = 8000;
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

            server.createContext("api/pokemons", (exchange -> {
                String respText = "Hello";
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
                exchange.close();
            }));

            server.setExecutor(null);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}