package org.zuehlke.gobusiness.scrabble.calculator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.zuehlke.gobusiness.scrabble.calculator.service.DictionaryService;

@Service
@Slf4j
public class DictionaryServiceImpl implements DictionaryService {

    private final WebClient webClient;

    public DictionaryServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.dictionaryapi.dev/api/v2/entries/en").build();
    }

    @Override
    public boolean isValidWord(String word) {
        try {
            // Confirming based on 200 OK HTTP Status
            webClient.get()
                    .uri("/{word}", word)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound ex) {
            // API returns 404 Not Found for invalid word
            log.info("Dictionary API confirmed '{}' is not a valid word.", word);
            return false;
        } catch (Exception ex) {
            log.error("Error calling dictionary API for word: '{}'", word, ex);
            return false;
        }
    }
}
