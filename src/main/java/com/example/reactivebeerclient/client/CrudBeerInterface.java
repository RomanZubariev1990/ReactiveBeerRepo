package com.example.reactivebeerclient.client;

import com.example.reactivebeerclient.model.Beer;
import com.example.reactivebeerclient.model.BeerStyle;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CrudBeerInterface {
    Mono<ClientResponse> createNewBeer(Beer beer);
    Mono<Beer> getBeerById(String id);
    Mono<List<Beer>> listBeers(Integer pageNumber, Integer pageSize, String beerName, BeerStyle beerStyle, Boolean showInventoryOnHand);
    Mono<ClientResponse> updateBeerById(String id, Beer beer);
    Mono<ClientResponse> deleteBeer (String id);
    Mono<Beer> getBeerByUpc( String upc);
}
