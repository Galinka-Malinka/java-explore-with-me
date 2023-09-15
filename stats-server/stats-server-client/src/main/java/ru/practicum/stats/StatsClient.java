package ru.practicum.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StatsClient extends BaseClient {

    private static final String API_PREFIX = "/stats";

    public StatsClient(@Value("${stats-server-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {

        log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRStatsClient 32 RRRRRRRRRR uris " + uris + " RRRRRRRRRRRRRR");

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRStatsClient 41 RRRRRRRRRR uris " + parameters.values() + " RRRRRRRRRRRRRR");


        return get("?start={start}&end={end}&uris={uris}&unique={unique}", null, parameters);
    }
}
