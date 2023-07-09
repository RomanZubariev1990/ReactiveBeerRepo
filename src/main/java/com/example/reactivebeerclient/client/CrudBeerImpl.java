package com.example.reactivebeerclient.client;

import com.example.reactivebeerclient.model.Beer;
import com.example.reactivebeerclient.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrudBeerImpl implements CrudBeerInterface {
    private static final String BEER_URL = "/api/v1/beer";
    private final WebClient webClient = WebClient.builder().baseUrl("https://api.springframework.guru/").build();
    @Override
    public Mono<ClientResponse> createNewBeer(Beer beer) {
        return webClient.post().uri(BEER_URL).retrieve().toEntity(String.class)
                .map(responseEntity -> ClientResponse.create(responseEntity.getStatusCode()).build());
    }

    @Override
    public Mono<Beer> getBeerById(String id) {
        return null;
    }

    @Override
    public Mono<List<Beer>> listBeers(Integer pageNumber, Integer pageSize, String beerName, BeerStyle beerStyle,
            Boolean showInventoryOnHand) {
        return null;
    }

    @Override
    public Mono<ClientResponse> updateBeerById(String id, Beer beer) {
        return null;
    }

    @Override
    public Mono<ClientResponse> deleteBeer(String id) {
        return null;
    }

    @Override
    public Mono<Beer> getBeerByUpc(String upc) {
        return null;
    }
}
