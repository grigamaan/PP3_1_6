package web.controllers;

import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import web.entities.User;

import java.util.List;

public class RestTemplateTest {

    private static final String URL = "http://94.198.50.185:7081/api/users";
    private static String sessionId;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<String> getUsersResponse = restTemplate.getForEntity(URL, String.class);
        sessionId = getUsersResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        User newUser = new User(3L, "James", "Brown", (byte) 30);
        ResponseEntity<String> saveUserResponse = restTemplate.postForEntity(URL, createHttpEntity(newUser), String.class);

        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 35);
        restTemplate.put(URL, createHttpEntity(updatedUser));
        ResponseEntity<String> updateUserResponse = restTemplate.postForEntity(URL, createHttpEntity(updatedUser), String.class);

        ResponseEntity<String> deleteUserResponse = restTemplate.exchange(URL + "/" + newUser.getId(), HttpMethod.DELETE, createHttpEntity(null), String.class);

        System.out.println(saveUserResponse.getBody() + updateUserResponse.getBody() + deleteUserResponse.getBody());
    }

    private static HttpEntity<User> createHttpEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, sessionId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(user, headers);
    }
}
