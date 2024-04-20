package a2.A2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface IMovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
}