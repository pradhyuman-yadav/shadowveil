package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    // Retrieve all playlists belonging to a specific user.
    List<Playlist> findByUser_Id(Integer userId);
}
