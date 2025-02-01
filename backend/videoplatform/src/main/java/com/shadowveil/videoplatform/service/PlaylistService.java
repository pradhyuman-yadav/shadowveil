package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Playlist;
import com.shadowveil.videoplatform.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    // Retrieve all playlists.
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    // Retrieve a single playlist by its ID.
    public Optional<Playlist> getPlaylistById(Integer id) {
        return playlistRepository.findById(id);
    }

    // Create a new playlist.
    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    // Update an existing playlist.
    public Playlist updatePlaylist(Integer id, Playlist playlistDetails) {
        return playlistRepository.findById(id).map(playlist -> {
            playlist.setUser(playlistDetails.getUser());
            playlist.setName(playlistDetails.getName());
            playlist.setDescription(playlistDetails.getDescription());
            playlist.setVisibility(playlistDetails.getVisibility());
            // Typically, createdAt is not updated; only updatedAt is changed.
            playlist.setUpdatedAt(playlistDetails.getUpdatedAt());
            return playlistRepository.save(playlist);
        }).orElseThrow(() -> new RuntimeException("Playlist not found with id " + id));
    }

    // Delete a playlist by its ID.
    public void deletePlaylist(Integer id) {
        playlistRepository.deleteById(id);
    }

    // Retrieve playlists by a given user ID.
    public List<Playlist> getPlaylistsByUserId(Integer userId) {
        return playlistRepository.findByUser_Id(userId);
    }
}
