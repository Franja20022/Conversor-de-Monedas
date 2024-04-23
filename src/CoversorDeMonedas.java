import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CoversorDeMonedas {
    public static void main(String[] args) {
        try {
            Scanner lectura = new Scanner(System.in);
            int opcion;

            while (true) {

                System.out.println("\nSeleccione una opción de conversión:");
                System.out.println("1. Convertir Dolar (USD) a Peso Argentina (ARS)");
                System.out.println("2. Convertir Dolar (USD) a Peso Colombia (COP)");
                System.out.println("3. Convertir Dolar (USD) a Real Brasil (BRL)");
                System.out.println("4. Convertir Cualquier Moneda a Otra");
                System.out.println("5. Salir");

                System.out.print("Ingrese el número de la opción deseada: ");
                opcion = lectura.nextInt();

                if (opcion == 5) {
                    System.out.println("¡Gracias y hasta pronto!");
                    break;
                }

                String monedaOrigen = "";
                String monedaDestino = "";
                switch (opcion) {
                    case 1:
                        monedaOrigen = "USD";
                        monedaDestino = "ARS";
                        break;
                    case 2:
                        monedaOrigen = "USD";
                        monedaDestino = "COP";
                        break;
                    case 3:
                        monedaOrigen = "USD";
                        monedaDestino = "BRL";
                        break;
                    case 4:

                        System.out.print("Ingrese la moneda de origen (por ejemplo, USD): ");
                        monedaOrigen = lectura.next().toUpperCase();
                        System.out.print("Ingrese la moneda de destino (por ejemplo, EUR): ");
                        monedaDestino = lectura.next().toUpperCase();
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        continue;
                }


                String direccion = "https://v6.exchangerate-api.com/v6/9f665abe2016603c16c2464e/latest/" + monedaDestino;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(direccion))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());


                int statusCode = response.statusCode();
                if (statusCode == 200) {

                    String responseBody = response.body();
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");

                    double tasaConversion = conversionRates.get(monedaOrigen).getAsDouble();

                    System.out.print("Ingrese la cantidad a convertir: ");
                    double cantidad = lectura.nextDouble();

                    double cantidadConvertida = cantidad / tasaConversion;

                    System.out.println(cantidad + " " + monedaOrigen + " es equivalente a " +
                            cantidadConvertida + " " + monedaDestino);

                    System.out.println("\nPresione Enter para volver al menú...");
                    lectura.nextLine();
                    lectura.nextLine();
                } else {
                    System.out.println("Error: No se pudo obtener la tasa de cambio. Código de estado: " + statusCode);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
