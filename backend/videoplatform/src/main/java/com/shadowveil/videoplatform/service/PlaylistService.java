// src/main/java/com/shadowveil/videoplatform/service/PlaylistService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.PlaylistDto;
import com.shadowveil.videoplatform.entity.Playlist;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.PlaylistRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository; // Inject UserRepository

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Playlist getPlaylistById(Integer id) { // Changed return type
        return playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with ID: " + id));
    }

    @Transactional
    public Playlist createPlaylist(PlaylistDto.Request requestDto) {
        Playlist playlist = new Playlist();

        // Handle optional user association
        if (requestDto.userId() != null) {
            User user = userRepository.findById(requestDto.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDto.userId()));
            playlist.setUser(user);
        }

        playlist.setName(requestDto.name());
        playlist.setDescription(requestDto.description());
        playlist.setVisibility(requestDto.visibility());

        return playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist updatePlaylist(Integer id, PlaylistDto.UpdateRequest updateRequestDto) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id: " + id));

        // Update fields based on the DTO
        playlist.setName(updateRequestDto.name());
        playlist.setDescription(updateRequestDto.description());
        playlist.setVisibility(updateRequestDto.visibility());
        // updatedAt is handled automatically by @UpdateTimestamp

        return playlistRepository.save(playlist);
    }

    @Transactional
    public void deletePlaylist(Integer id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id: " + id));
        playlistRepository.delete(playlist);
    }

    public List<Playlist> getPlaylistsByUserId(Integer userId) {
        return playlistRepository.findByUser_Id(userId);
    }
}