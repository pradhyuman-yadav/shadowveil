package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.service.ModuleService;
import com.shadowveil.videoplatform.entity.Module;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public ResponseEntity<List<ModuleDto.Response>> getAllModules() {
        List<ModuleDto.Response> modules = moduleService.getAllModules();
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleDto.Response> getModuleById(@PathVariable Integer id) {
        Optional<ModuleDto.Response> module = moduleService.getModuleById(id);
        return module.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ModuleDto.Response>> getModulesByCourseId(@PathVariable Integer courseId) {
        List<ModuleDto.Response> modules = moduleService.getModulesByCourseId(courseId);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}/ordered")
    public ResponseEntity<List<ModuleDto.Response>> getModulesByCourseIdOrdered(@PathVariable Integer courseId) {
        List<ModuleDto.Response> modules = moduleService.getModulesByCourseIdOrderedByPosition(courseId);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ModuleDto.Response> createModule(@Valid @RequestBody ModuleDto.Request moduleDto) {
        Module createdModule = moduleService.createModule(moduleDto);
        ModuleDto.Response responseDto = moduleService.convertToDto(createdModule);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleDto.Response> updateModule(@PathVariable Integer id, @Valid @RequestBody ModuleDto.Request moduleDto) {
        Module updatedModule = moduleService.updateModule(id, moduleDto);
        ModuleDto.Response responseDto = moduleService.convertToDto(updatedModule);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Integer id) {
        moduleService.deleteModule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}