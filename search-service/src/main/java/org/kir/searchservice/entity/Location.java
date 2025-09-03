package org.kir.searchservice.entity;

import com.kir.commonservice.constant.AppConstants;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "iata", unique = true, nullable = false)
    private String iata;

    @Column(name = "icao")
    private String icao;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "time")
    private String time;
}
