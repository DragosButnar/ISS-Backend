package a2.A2;

import a2.A2.exceptions.MovieDuplicateException;
import a2.A2.exceptions.MovieLogicException;
import a2.A2.exceptions.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {
    @Autowired
    private final IMovieRepository repository;

    MovieController(IMovieRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/movies")
    public @ResponseBody List<Movie> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/movies")
    public @ResponseBody String newMovie(@RequestBody Movie newMovie) {
        if(repository.findAll().stream().anyMatch(movie-> Objects.equals(movie.getTitle(), newMovie.getTitle())))
        {
            throw new MovieDuplicateException(newMovie.getTitle());
        }
        if(newMovie.getYear() < 0){
            throw new MovieLogicException(newMovie.getTitle());
        }
        repository.save(newMovie);
        return "Saved";
    }

    // Single item

    @GetMapping("/movies/{title}")
    Optional<Movie> one(@PathVariable String title) {
        Optional<Movie> movie = repository.findByTitle(title);
        if(movie.isPresent()) {
            return repository.findById(movie.get().getId());
        }
        return null;

    }

    @PutMapping("/movies/{title}")
    Movie replaceEmployee(@RequestBody Movie newMovie, @PathVariable String title) {
        var m =  repository.findByTitle(title);
        if(m.isEmpty())
            return null;
        var id = m.get().getId();
        return repository.findById(id)
                .map(movie -> {
                    movie.setTitle(newMovie.getTitle());
                    movie.setYear(newMovie.getYear());
                    movie.setGenre(newMovie.getGenre());
                    movie.setFranchise(newMovie.getFranchise());
                    return repository.save(movie);
                })
                .orElseGet(() -> {
                    newMovie.setId(id);
                    return repository.save(newMovie);
                });
    }

    @DeleteMapping("/movies/{title}")
    void deleteMovie(@PathVariable String title) {
        var m =  repository.findByTitle(title);
        if(m.isEmpty())
            return;
        var id = m.get().getId();
        repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        repository.deleteById(id);
    }

/*    @GetMapping("/movies/peekAtID")
    @Query(nativeQuery = true, value = "select movieid from movie order by movieid desc limit 1;")
    public List<Integer> peekAtID();*/


    @GetMapping("/movies/getLastID")
    public long lastID() {
        return Movie.lastID;
    }

}
