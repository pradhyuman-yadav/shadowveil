//(No changes needed, well-defined)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.PlaylistVideo;
import com.shadowveil.videoplatform.entity.PlaylistVideoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, PlaylistVideoId> {
    List<PlaylistVideo> findByPlaylist_Id(Integer playlistId);
    List<PlaylistVideo> findByVideo_Id(Integer videoId);
}