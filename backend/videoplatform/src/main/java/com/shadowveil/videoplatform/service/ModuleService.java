//Corrected ModuleService
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.CourseDto;
import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.entity.Module;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.CourseRepository;
import com.shadowveil.videoplatform.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService; // Inject for DTO conversion

    @Autowired
    public ModuleService(ModuleRepository moduleRepository, CourseRepository courseRepository, CourseService courseService) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    @Transactional(readOnly = true)
    public List<ModuleDto.Response> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ModuleDto.Response> getModuleById(Integer id) {
        return moduleRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<ModuleDto.Response> getModulesByCourseId(Integer courseId) {
        return moduleRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ModuleDto.Response> getModulesByCourseIdOrderedByPosition(Integer courseId) {
        return moduleRepository.findByCourseIdOrderByPositionAsc(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Module createModule(ModuleDto.Request moduleDto) {
        Course course = courseRepository.findById(moduleDto.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course with ID " + moduleDto.courseId() + " not found."));

        Module module = new Module();
        module.setCourse(course);
        module.setTitle(moduleDto.title());
        module.setDescription(moduleDto.description());
        module.setPosition(moduleDto.position());
        module.setCreatedAt(Instant.now());
        module.setUpdatedAt(Instant.now());
        return moduleRepository.save(module);
    }

    @Transactional
    public Module updateModule(Integer id, ModuleDto.Request moduleDto) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module with ID " + id + " not found"));

        if (moduleDto.courseId() != null && !module.getCourse().getId().equals(moduleDto.courseId())) {
            Course newCourse = courseRepository.findById(moduleDto.courseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course with ID " + moduleDto.courseId() + " not found."));
            module.setCourse(newCourse);
        }

        if (moduleDto.title() != null) module.setTitle(moduleDto.title());
        if (moduleDto.description() != null) module.setDescription(moduleDto.description());
        if (moduleDto.position() != null) module.setPosition(moduleDto.position());

        module.setUpdatedAt(Instant.now());
        return moduleRepository.save(module);
    }

    @Transactional
    public void deleteModule(Integer id) {
        if (!moduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Module with ID " + id + " not found");
        }
        moduleRepository.deleteById(id);
    }


    public ModuleDto.Response convertToDto(Module module) {
        CourseDto.Response courseResponseDto = null;
        if(module.getCourse() != null){
            courseResponseDto = courseService.convertToDto(module.getCourse());
        }
        return new ModuleDto.Response(
                module.getId(),
                courseResponseDto, // Use CourseService to get the DTO
                module.getTitle(),
                module.getDescription(),
                module.getPosition(),
                module.getCreatedAt(),
                module.getUpdatedAt()
        );
    }
}