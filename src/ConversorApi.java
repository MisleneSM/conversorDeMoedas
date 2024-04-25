import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConversorApi {
    public void buscaSiglas(String sigla) {
        URI endereco = URI.create("https://v6.exchangerate-api.com/v6/b76737415adb1f0032c802e5/latest/" + sigla);

        HttpRequest request = HttpRequest.newBuilder().uri(endereco).build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            System.out.println(json);
        } catch (Exception e) {
            System.out.println("Aconteceu algum erro");
        }
        System.out.println("Sistema concluido");
    }
}
