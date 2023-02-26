package com.prodyna.movieapp.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.prodyna.movieapp.MovieAppApplication;
import com.prodyna.movieapp.domain.Actor;
import com.prodyna.movieapp.domain.Movie;
import com.prodyna.movieapp.domain.Review;
import com.prodyna.movieapp.dto.ActorDTO;
import com.prodyna.movieapp.dto.MovieDTO;
import com.prodyna.movieapp.dto.ReviewDTO;
import com.prodyna.movieapp.enumeration.Genre;
import com.prodyna.movieapp.repository.ActorRepository;
import com.prodyna.movieapp.repository.MovieRepository;
import com.prodyna.movieapp.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MovieAppApplication.class
)
@AutoConfigureMockMvc
@Testcontainers
public class MovieAppIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Container
    static final Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5.1.0-community");

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    ActorDTO jenniferDTO, tomDTO;
    Actor tom, matt, orlando;
    Review goodReview, badReview;
    Movie inception;
    ReviewDTO badReviewDTO, goodReviewDTO, notBadReviewDTO;
    MovieDTO lordOfTheRingsDTO, inceptionDTO, inceptionUpdateDTO;

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4j::getAdminPassword);
    }

    void emptyDatabase() {

        movieRepository.deleteAll();
        actorRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {

        emptyDatabase();
        init();
    }

    public void init() {

        tomDTO = ActorDTO.builder()
                .firstName("Tom")
                .lastName("Cruise")
                .biography("Thomas Cruise Mapother IV (born July 3, 1962), known professionally as Tom " +
                        "Cruise, is an American actor and producer. One of the world's highest-paid" +
                        " actors, he has received various accolades, including an Honorary Palme d'Or and " +
                        "three Golden Globe Awards, in addition to nominations for three Academy Awards.")
                .build();

        matt = actorRepository.save(
                Actor.builder()
                        .firstName("Matt")
                        .lastName("Damon")
                        .biography("Matthew Paige Damon is an American actor, film producer, and screenwriter. " +
                                "Ranked among Forbes' most bankable stars, the films in which he has" +
                                " appeared have collectively earned over$3.88 billion at the North " +
                                "American box office, making him one of the highest-grossing actors of all time.")
                        .build());

        jenniferDTO = ActorDTO.builder()
                .firstName("Jennifer")
                .lastName("Connelly")
                .biography("Jennifer Connelly was born in the Catskill Mountains, New York, to Ilene (Schuman), " +
                        "a dealer of antiques, and Gerard Connelly, a clothing manufacturer.")
                .build();

        tom = actorRepository.save(
                Actor.builder()
                        .firstName("Tom")
                        .lastName("Cruise")
                        .biography("Thomas Cruise Mapother IV (born July 3, 1962), known professionally as Tom " +
                                "Cruise, is an American actor and producer. One of the world's highest-paid" +
                                " actors, he has received various accolades, including an Honorary Palme d'Or and " +
                                "three Golden Globe Awards, in addition to nominations for three Academy Awards.")
                        .build());

        orlando = actorRepository.save(
                Actor.builder()
                        .firstName("Orlando")
                        .lastName("Bloom")
                        .build());

        goodReview = Review.builder()
                .title("Pretty good")
                .rating(5)
                .description("Pretty good movie, watch it")
                .build();

        badReview = Review.builder()
                .title("Do not watch it")
                .rating(1)
                .description("Do not watch this movie, it is not good")
                .build();

        inception = movieRepository.save(
                Movie.builder()
                        .name("Inception")
                        .genre(List.of(Genre.ACTION))
                        .releaseDate(new Date(2010))
                        .durationInMin(148.0)
                        .description("A thief who steals corporate secrets through the use of dream-sharing" +
                                " technology is given the inverse task of planting an idea into the mind of a" +
                                " C.E.O., but his tragic past may doom the project and his team to disaster.")
                        .actors(List.of(matt, tom))
                        .reviews(List.of(goodReview, badReview))
                        .build());

        notBadReviewDTO = ReviewDTO.builder()
                .title("Not bad")
                .rating(3)
                .description("Not so bad movie")
                .build();

        goodReviewDTO = ReviewDTO.builder()
                .title("Pretty good")
                .rating(5)
                .description("Pretty good movie, watch it")
                .build();

        badReviewDTO = ReviewDTO.builder()
                .title("Do not watch it")
                .rating(1)
                .description("Do not watch this movie, it is not good")
                .build();

        lordOfTheRingsDTO = MovieDTO.builder()
                .name("The Lord of the Rings")
                .description("A meek Hobbit from the Shire and eight companions set out on a journey to" +
                        " destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.")
                .actors(List.of(tomDTO, jenniferDTO))
                .reviews(List.of(goodReviewDTO, badReviewDTO))
                .genre(List.of(Genre.ADVENTURE))
                .releaseDate(new Date(2001))
                .durationInMin(178.0)
                .build();

        inceptionDTO = MovieDTO.builder()
                .name("Inception")
                .genre(List.of(Genre.ACTION))
                .releaseDate(new Date(2010))
                .durationInMin(148.0)
                .description("A thief who steals corporate secrets through the use of dream-sharing" +
                        " technology is given the inverse task of planting an idea into the mind of a" +
                        " C.E.O., but his tragic past may doom the project and his team to disaster.")
                .actors(List.of(tomDTO))
                .reviews(List.of(goodReviewDTO, badReviewDTO))
                .build();

        inceptionUpdateDTO = MovieDTO.builder()
                .name("Updated Inception")
                .genre(List.of(Genre.COMEDY))
                .releaseDate(new Date(2009))
                .durationInMin(150.0)
                .description("A thief who steals corporate secrets through the use of dream-sharing...")
                .build();
    }

    @Test
    void getAllActors_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/actors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", is("Matt")));
    }

    @Test
    void getActorById_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/actors/" + tom.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Tom")));
    }

    @Test
    void deleteActor_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/actors/" + tom.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateActor_success() throws Exception {

        String content = objectWriter.writeValueAsString(jenniferDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/actors/" + tom.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Jennifer")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(tom.getId().intValue())));
    }

    @Test
    void createActor_success() throws Exception {

        String content = objectWriter.writeValueAsString(jenniferDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getAllMovies_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is("Inception")));
    }

    @Test
    public void deleteMovie_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/" + inception.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createMovie_success() throws Exception {

        String content = objectWriter.writeValueAsString(lordOfTheRingsDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("The Lord of the Rings")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.actors", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviews", hasSize(2)));
    }

    @Test
    public void updateMovie_success() throws Exception {

        String content = objectWriter.writeValueAsString(inceptionUpdateDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/movies/"
                        + inception.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Inception")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(inception.getId().intValue())));
    }
}
