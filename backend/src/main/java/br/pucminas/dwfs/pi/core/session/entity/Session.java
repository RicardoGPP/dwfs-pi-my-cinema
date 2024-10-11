package br.pucminas.dwfs.pi.core.session.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import br.pucminas.dwfs.pi.core.location.entity.Location;
import br.pucminas.dwfs.pi.core.movie.entity.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "sessions",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "movie_id",
                "location_id",
                "date",
                "time"
            }
        )
    }
)
@Data
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private boolean threeD;

    @Column(nullable = false)
    private boolean subtitled;
}