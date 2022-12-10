package ru.angimehub.dev.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.angimehub.dev.dto.UserDto;
import ru.angimehub.dev.repository.UserTokenRepository;

import javax.persistence.*;

@Table(name = "user_token")
@Entity
@NoArgsConstructor
@Setter
@Getter
public class UserTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String token;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static UserTokenEntity buildFrom(String token, UserEntity user) {
        UserTokenEntity userTokenEntity = new UserTokenEntity();
        userTokenEntity.setToken(token);
        userTokenEntity.setUser(user);
        return userTokenEntity;
    }
}
