package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class UserResponse
{
    private int id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private Avatar avatar;

    public static UserResponse fromUserDetailsToUserResponse(User user)
    {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getAvatar());
    }
}
