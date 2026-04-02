package co.istad.y2.quizzy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private Integer id;

    @Column(nullable = false,unique = true,length = 50)
    private String name;


    public Role(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    //Many to many because we follow spring security
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;


}
