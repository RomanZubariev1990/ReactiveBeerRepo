package com.example.reactivebeerclient.client;

import com.example.reactivebeerclient.model.Beer;
import com.example.reactivebeerclient.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CrudBeerTest {
    private static final String BEER_URL = "/api/v1/beer";
    @InjectMocks
    private CrudBeerImpl crudBeerImpl;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;
    private Beer goodBeer;
    private Beer badBeer;
    @BeforeEach
    void init(){
        goodBeer = Beer.builder()
            .beerName("BeerName")
            .beerStyle(BeerStyle.LAGER)
            .id("beerId")
            .upc("000666123")
            .price("4.20")
            .build();
        badBeer = Beer.builder().beerName("beerName").build();
    }
    @Test
    void shouldGetBeerById(){
        when(webClient.get().uri(BEER_URL).retrieve().bodyToMono(Beer.class)).thenReturn(Mono.just(goodBeer));
        StepVerifier.create(crudBeerImpl.getBeerById("beerId"))
                .expectNext(goodBeer).verifyComplete();
    }
    @Test
    void shouldGetEmptyBeerMonoById(){
        when(webClient.get().uri(BEER_URL).retrieve().bodyToMono(Beer.class)).thenReturn(Mono.error(new Throwable("404")));
        StepVerifier.create(crudBeerImpl.getBeerById("beerId"))
                .expectError(Throwable.class).verify();
    }
    @Test
    void shouldCreateBeer(){
        when(webClient.post().uri(BEER_URL).retrieve().toEntity(String.class))
                .thenReturn(Mono.just(new ResponseEntity<>( "/new/beer/url", HttpStatus.CREATED)));
        StepVerifier.create(crudBeerImpl.createNewBeer(goodBeer))
                .expectNextMatches(clientResponse->clientResponse.equals(ClientResponse.create(HttpStatusCode.valueOf(201)).build()))
                .verifyComplete();
    }

    @Test
    void shouldNotCreateBeerWhenRequestBad(){
        when(webClient.post().uri(BEER_URL).retrieve().toEntity(String.class))
                .thenReturn(Mono.just(new ResponseEntity<String>( "", HttpStatus.BAD_REQUEST)));
        StepVerifier.create(crudBeerImpl.createNewBeer(badBeer))
                .expectNext(ClientResponse.create(HttpStatusCode.valueOf(400)).build())
                .verifyComplete();
    }

    @Test
    void shouldUpdateBeer(){
        when(webClient.put().uri(BEER_URL+"/"+goodBeer.getId()).retrieve().toEntity(String.class))
                .thenReturn(Mono.just(new ResponseEntity<String>( "", HttpStatus.CREATED)));
        StepVerifier.create(crudBeerImpl.updateBeerById("id",goodBeer))
                .expectNext(ClientResponse.create(HttpStatusCode.valueOf(204)).build())
                .verifyComplete();
    }

    @Test
    void shouldNotUpdateBeerWhenBadRequest(){
        when(webClient.put().uri(BEER_URL+"/"+goodBeer.getId()).retrieve().toEntity(String.class))
                .thenReturn(Mono.just(new ResponseEntity<String>( "", HttpStatus.BAD_REQUEST)));
        StepVerifier.create(crudBeerImpl.updateBeerById("id",badBeer))
                .expectNext(ClientResponse.create(HttpStatusCode.valueOf(400)).build())
                .verifyComplete();
    }

    @Test
    void shouldNotUpdateBeerWhenNotFound(){
        when(webClient.put().uri(BEER_URL+"/"+goodBeer.getId()).retrieve().toEntity(String.class))
                .thenReturn(Mono.just(new ResponseEntity<String>( "", HttpStatus.NOT_FOUND)));
        StepVerifier.create(crudBeerImpl.updateBeerById("id",badBeer))
                .expectNext(ClientResponse.create(HttpStatusCode.valueOf(404)).build())
                .verifyComplete();
    }

    @Test
    void shouldNotUpdateBeerWhenConflict(){
        when(webClient.put().uri(BEER_URL+"/"+goodBeer.getId()).retrieve().toEntity(String.class))
                .thenReturn(Mono.just(new ResponseEntity<String>( "", HttpStatus.CONFLICT)));
        StepVerifier.create(crudBeerImpl.updateBeerById("id",badBeer))
                .expectNext(ClientResponse.create(HttpStatusCode.valueOf(409)).build())
                .verifyComplete();
    }

    @Test
    void shouldGetListOfBeer(){
        when(webClient.put().uri(BEER_URL+"?page=1&pageSize=10").retrieve().toEntity(String.class))
                .thenReturn(Mono.just(new ResponseEntity<String>( "", HttpStatus.OK)));
        StepVerifier.create(crudBeerImpl.listBeers(1,10,null,null,null))
                .expectNextMatches(list->list.size()==10)
                .verifyComplete();
    }
    @Test
    void shouldDeleteBeer(){
        when(webClient.put().uri(BEER_URL+"?page=1&pageSize=10").retrieve().toBodilessEntity())
                .thenReturn(Mono.just(new ResponseEntity<>( HttpStatus.NO_CONTENT)));
        StepVerifier.create(crudBeerImpl.deleteBeer("id"))
                .expectNext(ClientResponse.create(HttpStatusCode.valueOf(204)).build())
                .verifyComplete();
    }
    @Test
    void shouldNotDeleteWhenNotFound(){
        when(webClient.put().uri(BEER_URL+"?page=1&pageSize=10").retrieve().toBodilessEntity())
                .thenReturn(Mono.just(new ResponseEntity<>( HttpStatus.NOT_FOUND)));
        StepVerifier.create(crudBeerImpl.deleteBeer("id"))
                .expectNext(ClientResponse.create(HttpStatusCode.valueOf(404)).build())
                .verifyComplete();
    }
}
