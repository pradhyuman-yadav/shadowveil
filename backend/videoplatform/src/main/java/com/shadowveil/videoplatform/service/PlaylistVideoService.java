package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.PlaylistVideoDto;
import com.shadowveil.videoplatform.entity.Playlist;
import com.shadowveil.videoplatform.entity.PlaylistVideo;
import com.shadowveil.videoplatform.entity.PlaylistVideoId;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.repository.PlaylistRepository;
import com.shadowveil.videoplatform.repository.PlaylistVideoRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaylistVideoService {

    private final PlaylistVideoRepository playlistVideoRepository;
    private final PlaylistRepository playlistRepository; // Inject
    private final VideoRepository videoRepository; // Inject

    @Autowired
    public PlaylistVideoService(PlaylistVideoRepository playlistVideoRepository,
                                PlaylistRepository playlistRepository, VideoRepository videoRepository) {
        this.playlistVideoRepository = playlistVideoRepository;
        this.playlistRepository = playlistRepository;
        this.videoRepository = videoRepository;
    }

    public List<PlaylistVideo> getAllPlaylistVideos() {
        return playlistVideoRepository.findAll();
    }

    public PlaylistVideo getPlaylistVideoById(PlaylistVideoId id) {
        return playlistVideoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PlaylistVideo not found with id: " + id));
    }
    @Transactional
    public PlaylistVideo createPlaylistVideo(PlaylistVideoDto.Request requestDto) {

        Playlist playlist = playlistRepository.findById(requestDto.playlistId())
                .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id: " + requestDto.playlistId()));
        Video video = videoRepository.findById(requestDto.videoId())
                .orElseThrow(()-> new EntityNotFoundException("Video not found with id: " + requestDto.videoId()));

        PlaylistVideoId id = new PlaylistVideoId(requestDto.playlistId(), requestDto.videoId());
        PlaylistVideo playlistVideo = new PlaylistVideo(id, playlist, video, requestDto.position(), null); // createdAt by DB
        return playlistVideoRepository.save(playlistVideo);
    }

    @Transactional
    public PlaylistVideo updatePlaylistVideo(PlaylistVideoId id, PlaylistVideoDto.UpdatePositionRequest requestDto) {
        PlaylistVideo playlistVideo = playlistVideoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PlaylistVideo not found with id: " + id));

        // Only update the position
        playlistVideo.setPosition(requestDto.position());
        return playlistVideoRepository.save(playlistVideo);
    }

    @Transactional
    public void deletePlaylistVideo(PlaylistVideoId id) {
        PlaylistVideo playlistVideo = playlistVideoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Playlist Video not found with id: "+ id));
        playlistVideoRepository.delete(playlistVideo);
    }

    public List<PlaylistVideo> getPlaylistVideosByPlaylistId(Integer playlistId) {
        return playlistVideoRepository.findByPlaylist_Id(playlistId);
    }

    public List<PlaylistVideo> getPlaylistVideosByVideoId(Integer videoId) {
        return playlistVideoRepository.findByVideo_Id(videoId);
    }
}