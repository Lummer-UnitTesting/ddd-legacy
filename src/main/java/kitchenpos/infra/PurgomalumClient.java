package kitchenpos.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class PurgomalumClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PurgomalumClient(final RestTemplateBuilder restTemplateBuilder, final ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public boolean containsProfanity(final String text) {
        if (text == null) {
            return false;
        }

        final URI url = UriComponentsBuilder.fromUriString("https://www.purgomalum.com/service/containsprofanity")
            .queryParam("text", text)
            .build()
            .toUri();
        return Boolean.parseBoolean(restTemplate.getForObject(url, String.class));
    }
}
