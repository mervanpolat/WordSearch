# iWord

## Description

This Java application launches an HTTP server that processes requests to count the occurrences of a word in the text of "War and Peace". It's a multi-threaded server that can handle multiple requests simultaneously.

## Features

- HTTP server listening on port 8000.
- Multi-threaded processing with a pool of 8 threads.
- Word counting in the entire text of "War and Peace".
- Input is provided via HTTP GET requests.

## Requirements

- Java Development Kit (JDK) must be installed to compile and run the application.
- The text file "war_and_peace.txt" must be located in the `./resources/` directory relative to the application's executable.

## Installation

1. Clone the repository or download the source code.
2. Ensure "war_and_peace.txt" is placed in the `./resources/` directory.
3. Compile the code using a Java compiler. For example:
   ```
   javac ThroughputHttpServer.java
   ```

## Usage

1. Start the server by running the compiled Java class. For example:
   ```
   java ThroughputHttpServer
   ```
2. The server will start listening for connections on `http://localhost:8000`.
3. To count occurrences of a word in the text, make a GET request to `http://localhost:8000/search?word=your_word_here`. Replace `your_word_here` with the word you want to count.
4. The server will respond with the count of the word's occurrences in the text.

## Example

To count how many times the word "war" appears in the text, navigate to:
```
http://localhost:8000/search?word=war
```

The server will respond with the number representing the count of "war" in the text.

## Code Structure

- `ThroughputHttpServer.java`: The main server class that sets up the HTTP server and handlers.
- `WordCountHandler`: An inner class implementing `HttpHandler` to process word count requests.

## API Reference

The server accepts a single query parameter `word` which is the word to be counted in the text.

- **URL**: `/search`
- **Method**: `GET`
- **URL Params**: `word=[string]`

## Response Example

For a request to `http://localhost:8000/search?word=peace`, the server might respond with:
```
1024
```
indicating that the word "peace" appears 1024 times in the text.

## Troubleshooting

If the server doesn't start, ensure that port 8000 is free and not being used by another service.
