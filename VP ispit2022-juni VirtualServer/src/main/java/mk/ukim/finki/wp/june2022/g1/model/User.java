package mk.ukim.finki.wp.june2022.g1.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// dodadov entity za da se napravi tabela u databaza
@Entity
@Table(name="VM_owner") // mora da ima custom name deka imeto e User a toa ne e dozvoleno
public class User {

    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    // avtomatski generirano ID za sekoj user
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
