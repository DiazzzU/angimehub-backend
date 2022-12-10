package ru.angimehub.dev.entity;

import ru.angimehub.dev.dto.SmallArtistDto;
import ru.angimehub.dev.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Table(name = "user")
@Entity
@NoArgsConstructor
@Setter
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column
    private String firstName;
    @Column
    private String secondName;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String role;
    @Column
    private String imageUrl;
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List <MediaEntity> medias;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List <UserTokenEntity> tokens;
    @ManyToMany
    @JoinTable(name = "media_like",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "media_id"))
    private List <MediaEntity> likedMedias;
    private Integer popularness;

    public static UserEntity buildFrom(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setSecondName(userDto.getSecondName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setRole("simple user");
        userEntity.setImageUrl("https://storage.yandexcloud.net/angimehub/defaultProfileImage.png");
        userEntity.setPopularness(0);
        return userEntity;
    }

    public SmallArtistDto toSmallArtistDto() {
        return SmallArtistDto.buildFrom(
                this.getId(),
                this.getFirstName(),
                this.getSecondName(),
                this.getImageUrl()
        );
    }
}