/*
 * (c) Copyright IBM Corp. 2021
 * (c) Copyright Instana Inc.
 */
package org.example;

import java.io.*;
import java.net.Socket;

/**
 * Simple HTTP client which doesn't generate spans.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
class HttpClient {

  private final String host;
  private final int port;

  private String header;
  private String response;

  HttpClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  HttpClient request(String... requests) throws IOException {
    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;
    try {
      socket = new Socket(host, port);

      // HttpUrlConnection generates traces by itself; therefore we HTTP like a boss
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      for (String request : requests) {
        writer.write(request + "\r\n");
      }
      writer.write("\r\n");
      writer.flush();

      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      header = reader.readLine();
      String read;
      do {
        read = reader.readLine();
      } while (!read.equals(""));
      StringBuilder sb = new StringBuilder();
      do {
        read = reader.readLine();
        if (read != null) {
          if (sb.length() > 0) {
            sb.append("\n");
          }
          sb.append(read);
        }
      } while (read != null && !read.equals(""));
      response = sb.toString();
    } finally {
      if (writer != null) {
        writer.close();
      }
      if (reader != null) {
        reader.close();
      }
      if (socket != null) {
        socket.close();
      }
    }
    return this;
  }

  HttpClient assertHeader(String expected) {
    if (header == null || !header.equals(expected)) {
      throw new AssertionError("Expected HTTP header '" + expected + "', but got " + header);
    }
    return this;
  }

  HttpClient assertHeader(String expected, String alternativeExpected) {
    try {
      if (header == null || !header.equals(expected)) {
        throw new AssertionError("Expected HTTP header '" + expected + "', but got " + header);
      }
    } catch (AssertionError e) {
      if (header == null || !header.equals(alternativeExpected)) {
        throw new AssertionError("Expected HTTP header '" + alternativeExpected + "', but got " + header);
      }
    }
    return this;
  }

  HttpClient assertResponse(String expected) {
    if (response == null || !response.equals(expected)) {
      throw new AssertionError("Expected HTTP response '" + expected + "', but got " + response);
    }
    return this;
  }

  HttpClient assertResponseContains(String expected) {
    if (response == null || !response.contains(expected)) {
      throw new AssertionError("Expected HTTP response containing '" + expected + "', but got " + response);
    }
    return this;
  }

  HttpClient assertResponseContains(String expected, String alternativeExpected) {
    try {
      if (response == null || !response.contains(expected)) {
        throw new AssertionError("Expected HTTP response containing '" + expected + "', but got " + response);
      }
    } catch (AssertionError e) {
      if (response == null || !response.contains(alternativeExpected)) {
        throw new AssertionError(
            "Expected HTTP response containing '" + alternativeExpected + "', but got " + response);
      }
    }
    return this;
  }

  String getHeader() {
    return header;
  }

  String getResponse() {
    return response;
  }
}
