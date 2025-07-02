package com.kingdomsonline.controller;

import com.kingdomsonline.dto.ApiResponse;
import com.kingdomsonline.dto.TechnologyViewDto;
import com.kingdomsonline.service.TechnologyService;
import com.kingdomsonline.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kingdom")
public class KingdomController {

    private final TechnologyService technologyService;
    private final JwtService jwtService;

    public KingdomController(TechnologyService technologyService, JwtService jwtService) {
        this.technologyService = technologyService;
        this.jwtService = jwtService;
    }

    @GetMapping("/technology")
    public ResponseEntity<ApiResponse<List<TechnologyViewDto>>> getPlayerTechnology(HttpServletRequest request) {
        Long playerId = jwtService.extractPlayerIdFromRequest(request);
        List<TechnologyViewDto> techs = technologyService.getTechnologyView(playerId);
        return ResponseEntity.ok(ApiResponse.ok("Technology loaded", techs));
    }
}
