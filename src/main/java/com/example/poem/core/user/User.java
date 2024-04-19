package com.example.poem.core.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Table(name = User.TABLE_NAME)
public class User implements UserDetails {
  static final String TABLE_NAME = "user_table";

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  @Size(max = 128)
  @NotNull
  private String username;

  @Column(name = "password")
  @Size(max = 128)
  @NotNull
  private String password;

  @Column(name = "user_role")
  @Enumerated(EnumType.STRING)
  @NotNull
  private UserRole role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
