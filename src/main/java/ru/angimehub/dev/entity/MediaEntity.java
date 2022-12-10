package ru.angimehub.dev.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.angimehub.dev.dto.FullVideoDto;
import ru.angimehub.dev.dto.SmallVideoDto;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

@Table(name = "media")
@Entity
@NoArgsConstructor
@Setter
@Getter
public class MediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Integer id;
    @Column
    private String urlVideo;
    @Column
    private String urlSplashScreen;
    @Column
    private String mediaName;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity artist;
    @Column
    private Timestamp createDateTime;
    @Column
    private Integer views;
    @Column
    private Integer duration;
    @Column
    private Integer type;
    @ManyToMany(mappedBy = "likedMedias")
    private List<UserEntity> likes;

    public static MediaEntity buildFrom(String urlVideo, String urlSplashScreen, String mediaName, UserEntity artist, Integer type) {
        MediaEntity mediaEntity = new MediaEntity();
        mediaEntity.setArtist(artist);
        mediaEntity.setMediaName(mediaName);
        mediaEntity.setUrlVideo(urlVideo);
        mediaEntity.setUrlSplashScreen(urlSplashScreen);
        mediaEntity.setCreateDateTime(Timestamp.from(ZonedDateTime.now().toInstant()));
        mediaEntity.setViews(0);
        mediaEntity.setType(type);
        return mediaEntity;
    }

    public SmallVideoDto toSmallVideoDto() {
        return SmallVideoDto.buildFrom(
                this.getUrlSplashScreen(),
                this.getUrlVideo(),
                this.getMediaName(),
                this.getId(),
                this.getArtist().getFirstName(),
                this.getArtist().getSecondName(),
                this.getType()
        );
    }

    public FullVideoDto toFullVideoDto() {
        FullVideoDto fullVideoDto = new FullVideoDto();
        fullVideoDto.setIdVideo(this.getId());
        fullVideoDto.setArtistId(this.getArtist().getId());
        fullVideoDto.setArtistFirstName(this.getArtist().getFirstName());
        fullVideoDto.setArtistSecondName(this.getArtist().getSecondName());
        fullVideoDto.setArtistImage(this.getArtist().getImageUrl());
        fullVideoDto.setType(this.getType());
        fullVideoDto.setUrlVideo(this.getUrlVideo());
        fullVideoDto.setUrlImage(this.getUrlSplashScreen());
        fullVideoDto.setMediaName(this.getMediaName());
        fullVideoDto.setViews(this.getViews());
        return fullVideoDto;
    }
}
