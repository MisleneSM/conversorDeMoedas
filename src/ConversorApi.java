import com.google.gson.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConversorApi {
    private List<String> currencyCodes = Arrays.asList("ARS", "BOB", "BRL", "CLP", "COP", "USD");

    public void buscarSiglas() {
        Scanner scanner = new Scanner(System.in);
        String opcao;

        while (true) {
            System.out.println("""
                    **********************************************
                    Seja bem-vindo(a) ao Conversor de Moedas =)
                    
                    1) Dólar ==> Peso argentino
                    2) Peso argentino =>> Dólar
                    3) Dólar =>> Real brasileiro
                    4) Real brasileiro =>> Dólar
                    5) Dólar =>> Peso colombiano
                    6) Peso colombiano =>> Dólar
                    7) Sair
                    Escolha uma opção válida:
                    *********************************************
                    """);

            opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    realizarConversao("USD", "ARS", scanner);
                    break;
                case "2":
                    realizarConversao("ARS", "USD", scanner);
                    break;
                case "3":
                    realizarConversao("USD", "BRL", scanner);
                    break;
                case "4":
                    realizarConversao("BRL", "USD", scanner);
                    break;
                case "5":
                    realizarConversao("USD", "CLP", scanner);
                    break;
                case "6":
                    realizarConversao("CLP", "USD", scanner);
                    break;
                case "7":
                    System.out.println("Programa encerrado com sucesso! Obrigada por utilizar nossos serviços.");
                    return;
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção de 1 a 7.");
            }
        }
    }

    private void realizarConversao(String moedaOrigem, String moedaDestino, Scanner scanner) {
        System.out.println("Digite o valor a ser convertido:");
        double valorOrigem = Double.parseDouble(scanner.nextLine());

        double taxaConversao = buscarTaxaConversao(moedaOrigem, moedaDestino);

        if (taxaConversao != -1) {
            double valorConvertido = valorOrigem * taxaConversao;
            System.out.printf("Valor %.1f [%s] corresponde ao valor final de =>>> %.1f [%s]\n", valorOrigem, moedaOrigem, valorConvertido, moedaDestino);
        } else {
            System.out.println("As taxas de conversão para as moedas selecionadas não estão disponíveis.");
        }
    }

    private double buscarTaxaConversao(String moedaOrigem, String moedaDestino) {
        URI endereco = URI.create("https://v6.exchangerate-api.com/v6/b76737415adb1f0032c802e5/latest/" + moedaOrigem);

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(endereco).build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonObject conversionRatesObject = jsonObject.getAsJsonObject("conversion_rates");

            if (conversionRatesObject.has(moedaDestino)) {
                return conversionRatesObject.get(moedaDestino).getAsDouble();
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Aconteceu algum erro ao buscar as taxas de conversão");
            e.printStackTrace();
            return -1;
        }
    }
}

