import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Application {
    final String togepiImageURL = "https://vignette.wikia.nocookie.net/pokemony/images/f/ff/Togepi.png/revision/latest?cb=20150809201237&path-prefix=pl";
    final String bulbasaurImageURL = "https://cdn.bulbagarden.net/upload/thumb/2/21/001Bulbasaur.png/250px-001Bulbasaur.png";

    public static void main(String[] args) {
        int serverPort = 8003;

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
            initializeRoute(server);
            setPokemonCreateRoute(server);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeRoute(HttpServer server){
        HttpContext context = server.createContext("/api/pokemons");
        context.setHandler(Application::handleRequest);
    }

    public static void setPokemonCreateRoute(HttpServer server) {
        HttpContext context = server.createContext("/api/pokemons/create");
        context.setHandler(Application::handleCreatePokemonRequest);
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String respText = "Hello big dick";
        printRequestInfo(exchange);
        exchange.sendResponseHeaders(200, respText.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.getBytes());
        output.flush();
        output.close();
    }

    private static void handleCreatePokemonRequest(HttpExchange exchange) throws IOException {
        printRequestInfo(exchange);
        Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());

        String noNameText = "Anonymous";
        String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);
        String type = params.getOrDefault("type", List.of(noNameText)).stream().findFirst().orElse(noNameText);
        String strength = params.getOrDefault("strength", List.of(noNameText)).stream().findFirst().orElse(noNameText);
        String agility = params.getOrDefault("agility", List.of(noNameText)).stream().findFirst().orElse(noNameText);

        Pokemon createdPokemon = new Pokemon(name,type,strength, agility);

        String respText = String.format("Hello %s! You're type is %s, your strength is %s, your agility is %s", name, type, strength, agility);

        exchange.sendResponseHeaders(200, respText.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.getBytes());
        output.flush();
        output.close();
    }

    private static void printRequestInfo(HttpExchange exchange) {
        System.out.println("-- headers --");
        Headers requestHeaders = exchange.getRequestHeaders();
        requestHeaders.entrySet().forEach(System.out::println);

        System.out.println("-- principle --");
        HttpPrincipal principal = exchange.getPrincipal();
        System.out.println(principal);

        System.out.println("-- HTTP method --");
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);

        System.out.println("-- query --");
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
        System.out.println(query);

        System.out.println("-- request body --");
        InputStream requestBody = exchange.getRequestBody();
        String requestBodyString = requestBody.toString();
        System.out.println(requestBodyString);
    }

    public static Map<String, List<String>> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays .copyOf(s.split("="), 2))
                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));

    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }


}
