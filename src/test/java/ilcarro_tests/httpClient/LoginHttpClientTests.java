package ilcarro_tests.httpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginHttpClientTests {

    private static final String LOGIN_URL = "https://ilcarro-backend.herokuapp.com/v1/user/login/usernamepassword";

    /**
     * Тест проверяет, что после логина в ответе присутствует поле accessToken.
     */
    @Test
    public void loginTokenPresenceTest() throws IOException {
        String jsonBody = """
                {
                    "username": "test_qa@gmail.com",
                    "password": "Password@1"
                }
                """;

        // Отправляем POST-запрос
        Response response = Request.Post(LOGIN_URL)
                .bodyString(jsonBody, ContentType.APPLICATION_JSON)
                .execute();

        // Получаем тело ответа в виде строки
        String responseJson = response.returnContent().asString();
        System.out.println("Response JSON: " + responseJson);

        // Парсим JSON и извлекаем accessToken
        JsonElement element = JsonParser.parseString(responseJson);
        JsonElement accessToken = element.getAsJsonObject().get("accessToken");
        System.out.println("Extracted token: " + accessToken);

        // Проверяем, что accessToken не равен null
        Assert.assertNotNull(accessToken, "accessToken не должен быть null");
    }

    /**
     * Тест проверяет, что статус-код равен 200 и поле accessToken присутствует в ответе.
     */
    @Test
    public void loginStatusCodeAndTokenTest() throws IOException {
        String jsonBody = """
                {
                    "username": "test_qa@gmail.com",
                    "password": "Password@1"
                }
                """;

        // Отправляем POST-запрос
        Response response = Request.Post(LOGIN_URL)
                .bodyString(jsonBody, ContentType.APPLICATION_JSON)
                .execute();

        // Получаем полный HTTP-ответ (HttpResponse), чтобы проверить статус-код
        HttpResponse httpResponse = response.returnResponse();
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Проверяем, что статус-код == 200
        Assert.assertEquals(statusCode, 200, "Статус-код должен быть 200");

        // Считываем тело ответа в виде строки
        String responseJson = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Response JSON: " + responseJson);

        // Парсим JSON и извлекаем accessToken
        JsonElement element = JsonParser.parseString(responseJson);
        JsonElement accessToken = element.getAsJsonObject().get("accessToken");
        System.out.println("Extracted token: " + accessToken);

        // Проверяем, что accessToken не равен null
        Assert.assertNotNull(accessToken, "accessToken не должен быть null");
    }
}
