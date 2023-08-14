// Import statements - these are classes and packages that our code needs to run.
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;  // Exception handling related to Input/Output operations.
import java.io.OutputStream; // Allows writing byte data.
import java.net.InetSocketAddress;  // Provides an endpoint for a socket.
import java.nio.file.Files;  // Reading/writing from/to files.
import java.nio.file.Paths;  // Representing file paths.
import java.util.concurrent.Executor;  // Interface to manage threads.
import java.util.concurrent.Executors; // Factory to create thread pools.


// In order to search -> http://localhost:8000/search?word=enter_word_here
// Example: http://localhost:8000/search?word=war

// Define the class ThroughputHttpServer.
public class ThroughputHttpServer {
    // Constants defining the path to the input file and number of threads.
    private static final String INPUT_FILE = "./resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 8;

    // Main method - the entry point of the Java program.
    public static void main(String[] args) throws IOException {
        // Read the entire content of the file into a String.
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));

        // Start the HTTP server.
        startServer(text);
    }

    // Method to start the HTTP server.
    public static void startServer(String text) throws IOException {
        // Create an HTTP server on port 8000.
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Set a context for /search path and associate it with WordCountHandler.
        server.createContext("/search", new WordCountHandler(text));

        // Create a thread pool with a fixed number of threads.
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        // Assign the executor (thread pool) to the server.
        server.setExecutor(executor);

        // Start the server.
        server.start();
    }

    // Inner class to handle the word count logic for incoming requests.
    private static class WordCountHandler implements HttpHandler {
        // Instance variable to store the text (War and Peace) for searching.
        private String text;

        // Constructor to initialize the text.
        public WordCountHandler(String text) {
            this.text = text;
        }

        // Implementation of the handle method from the HttpHandler interface.
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            // Get the query part of the request URL.
            String query = httpExchange.getRequestURI().getQuery();

            // Split the query based on '=' to get the key-value pair.
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];

            // Check if the key is not "word". If not, return an error.
            if (!action.equals("word")) {
                httpExchange.sendResponseHeaders(400, 0);  // 400 Bad Request
                return;
            }

            // Count the occurrences of the word in the text.
            long count = countWord(word);

            // Convert the count to a byte array for sending as a response.
            byte[] response = Long.toString(count).getBytes();

            // Send the headers and prepare for sending the response body.
            httpExchange.sendResponseHeaders(200, response.length);  // 200 OK

            // Get the output stream to send the response.
            OutputStream outputStream = httpExchange.getResponseBody();

            // Write the count to the output stream.
            outputStream.write(response);

            // Close the output stream.
            outputStream.close();
        }

        // Method to count the occurrences of a word in the text.
        private long countWord(String word) {
            long count = 0;
            int index = 0;
            // Keep searching for the word until it's not found.
            while (index >= 0) {
                index = text.indexOf(word, index);  // Find the word starting from 'index'.

                // If the word was found, increment the count and update 'index'.
                if (index >= 0) {
                    count++;
                    index++;
                }
            }
            // Return the count.
            return count;
        }
    }
}
