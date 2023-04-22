package com.auth.authservice.adapters.out.models;

import com.auth.authservice.adapters.out.types.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.Id;
import java.util.Collection;
import java.util.List;

@Document(collection = "_fastPaymentUsersv1")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
   @Id
   @Field(targetType = FieldType.OBJECT_ID)
   private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private Role role;

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
