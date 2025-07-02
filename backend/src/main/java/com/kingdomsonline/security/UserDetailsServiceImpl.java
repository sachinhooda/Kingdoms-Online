package com.kingdomsonline.security;

import com.kingdomsonline.model.Player;
import com.kingdomsonline.repository.PlayerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PlayerRepository playerRepository;

    public UserDetailsServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Player player = playerRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Player not found"));
        return User.builder()
            .username(player.getEmail())
            .password(player.getPassword())
            .roles("PLAYER") // simple role for now
            .build();
    }
}
