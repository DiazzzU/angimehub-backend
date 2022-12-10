package ru.angimehub.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.angimehub.dev.entity.MediaEntity;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<MediaEntity, Integer> {
    List<MediaEntity> findTop10ByTypeOrderByViewsDesc(Integer type);
    Optional<MediaEntity> findById(Integer id);
    List<MediaEntity> findByType(Integer type);
}
