package bstrzkk.test.spring.project.client;

import bstrzkk.test.spring.project.model.response.AllUsersResponse;
import bstrzkk.test.spring.project.model.response.SingleUserResponse;
import bstrzkk.test.spring.project.model.response.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNullElse;

@Service
public class RegresClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${external.api.client.regres}")
    private String BASE_URI;

    public User fetchUser(Integer id) {
        final ResponseEntity<SingleUserResponse> response = restTemplate.getForEntity(buildUri(id), SingleUserResponse.class);

        return requireNonNullElse(response.getBody(), new SingleUserResponse()).getData();
    }

    public List<User> fetchAllUsers() {
        final ResponseEntity<AllUsersResponse> response = restTemplate.getForEntity(buildUri(null), AllUsersResponse.class);

        return requireNonNullElse(response.getBody(), new AllUsersResponse()).getData();
    }

    private URI buildUri(Integer id) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(BASE_URI);

        if (id != null) {
            uriComponentsBuilder.pathSegment(valueOf(id));
        }

        return uriComponentsBuilder
                .build()
                .toUri();
    }

}
