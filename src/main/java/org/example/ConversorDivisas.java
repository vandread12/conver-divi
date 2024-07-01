package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorDivisas {
    private static final String API_KEY = "638de3f1b09bcd4d76f5dc21";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la moneda de origen (ej. USD): ");
        String monedaOrigen = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la moneda de destino (ej. EUR): ");
        String monedaDestino = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la cantidad a convertir: ");
        double cantidad = scanner.nextDouble();

        try {
            double tasaCambio = obtenerTasaCambio(monedaOrigen, monedaDestino);
            double resultado = cantidad * tasaCambio;

            System.out.printf("%.2f %s = %.2f %s%n", cantidad, monedaOrigen, resultado, monedaDestino);
        } catch (Exception e) {
            System.out.println("Error al realizar la conversión: " + e.getMessage());
        }

        scanner.close();
    }

    private static double obtenerTasaCambio(String monedaOrigen, String monedaDestino) throws Exception {
        String urlStr = API_URL + monedaOrigen;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
        JsonObject tasas = jsonResponse.getAsJsonObject("conversion_rates");

        return tasas.get(monedaDestino).getAsDouble();
    }
}