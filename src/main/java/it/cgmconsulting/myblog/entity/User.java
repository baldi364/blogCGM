package it.cgmconsulting.myblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "user_")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends CreationUpdate implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 15, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_authorities",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName = "id")}
    )
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avatar_id", nullable = true)
    public Avatar avatar;

    /* Inherited methods from org.springframework.security.core.userdetails.UserDetails */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities.stream().map(role ->
                new SimpleGrantedAuthority(role.getAuthorityName())
        ).collect(Collectors.toList());
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}


