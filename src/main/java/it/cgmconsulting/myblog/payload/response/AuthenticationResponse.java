package it.cgmconsulting.myblog.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.cgmconsulting.myblog.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse
{
    private int id;
    private String username;
    private String email;
    private String[] authorities;
    private String token;

}
