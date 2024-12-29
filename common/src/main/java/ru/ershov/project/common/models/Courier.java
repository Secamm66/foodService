package ru.ershov.project.common.models;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.ershov.project.common.models.statuses.CourierStatus;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courier")
@Data
@Accessors(chain = true)
public class Courier {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    private CourierStatus status;

    @Column(name = "coordinates")
    private String coordinates;

    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL)
    private List<Order> orders;

}