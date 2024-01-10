package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank @Size(min = 4, max = 15)
    private String username;
    @NotBlank @Email
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$",
            message="La password può contenere solo caratteri maiuscoli e minuscoli e numeri. La lunghezza deve essere compresa tra 6 e 10 caratteri")
    private String password;
}
