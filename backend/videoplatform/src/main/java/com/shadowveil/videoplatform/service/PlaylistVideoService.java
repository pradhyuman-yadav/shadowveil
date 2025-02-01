package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.PlaylistVideo;
import com.shadowveil.videoplatform.entity.PlaylistVideoId;
import com.shadowveil.videoplatform.repository.PlaylistVideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistVideoService {

    private final PlaylistVideoRepository playlistVideoRepository;

    public PlaylistVideoService(PlaylistVideoRepository playlistVideoRepository) {
        this.playlistVideoRepository = playlistVideoRepository;
    }

    // Retrieve all PlaylistVideo records.
    public List<PlaylistVideo> getAllPlaylistVideos() {
        return playlistVideoRepository.findAll();
    }

    // Retrieve a single PlaylistVideo record by its composite ID.
    public Optional<PlaylistVideo> getPlaylistVideoById(PlaylistVideoId id) {
        return playlistVideoRepository.findById(id);
    }

    // Create a new PlaylistVideo record.
    public PlaylistVideo createPlaylistVideo(PlaylistVideo playlistVideo) {
        return playlistVideoRepository.save(playlistVideo);
    }

    // Update an existing PlaylistVideo record.
    public PlaylistVideo updatePlaylistVideo(PlaylistVideoId id, PlaylistVideo playlistVideoDetails) {
        return playlistVideoRepository.findById(id).map(playlistVideo -> {
            // Update fields that are not part of the composite key.
            playlistVideo.setPosition(playlistVideoDetails.getPosition());
            // Optionally update other non-key fields (e.g., createdAt if needed)
            return playlistVideoRepository.save(playlistVideo);
        }).orElseThrow(() -> new RuntimeException("PlaylistVideo not found with id: " + id));
    }

    // Delete a PlaylistVideo record by its composite ID.
    public void deletePlaylistVideo(PlaylistVideoId id) {
        playlistVideoRepository.deleteById(id);
    }

    // Retrieve all PlaylistVideo records for a specific playlist.
    public List<PlaylistVideo> getPlaylistVideosByPlaylistId(Integer playlistId) {
        return playlistVideoRepository.findByPlaylist_Id(playlistId);
    }

    // Retrieve all PlaylistVideo records for a specific video.
    public List<PlaylistVideo> getPlaylistVideosByVideoId(Integer videoId) {
        return playlistVideoRepository.findByVideo_Id(videoId);
    }
}
