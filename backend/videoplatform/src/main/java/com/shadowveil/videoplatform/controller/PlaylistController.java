package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.PlaylistDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Playlist;
import com.shadowveil.videoplatform.service.PlaylistService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<List<PlaylistDto.Response>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.getAllPlaylists();
        List<PlaylistDto.Response> responseDtos = playlists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto.Response> getPlaylistById(@PathVariable Integer id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(convertToDto(playlist));
    }

    @PostMapping
    public ResponseEntity<PlaylistDto.Response> createPlaylist(@Valid @RequestBody PlaylistDto.Request requestDto) {
        Playlist createdPlaylist = playlistService.createPlaylist(requestDto);
        return new ResponseEntity<>(convertToDto(createdPlaylist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDto.Response> updatePlaylist(
            @PathVariable Integer id,
            @Valid @RequestBody PlaylistDto.UpdateRequest updateRequestDto) { // Use UpdateRequest DTO
        Playlist updatedPlaylist = playlistService.updatePlaylist(id, updateRequestDto);
        return ResponseEntity.ok(convertToDto(updatedPlaylist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Integer id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistDto.Response>> getPlaylistsByUserId(@PathVariable Integer userId) {
        List<Playlist> playlists = playlistService.getPlaylistsByUserId(userId);
        List<PlaylistDto.Response> responseDtos = playlists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // Helper method to convert Playlist entity to PlaylistDto.Response
    private PlaylistDto.Response convertToDto(Playlist playlist) {
        UserDto.Response userDto = (playlist.getUser() != null) ?
                new UserDto.Response(
                        playlist.getUser().getId(),
                        playlist.getUser().getUsername(),
                        playlist.getUser().getEmail(),
                        playlist.getUser().getRole(),
                        playlist.getUser().getCreatedAt(),
                        playlist.getUser().getUpdatedAt()
                ) : null;

        return new PlaylistDto.Response(
                playlist.getId(),
                userDto,
                playlist.getName(),
                playlist.getDescription(),
                playlist.getVisibility(),
                playlist.getCreatedAt(),
                playlist.getUpdatedAt()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}