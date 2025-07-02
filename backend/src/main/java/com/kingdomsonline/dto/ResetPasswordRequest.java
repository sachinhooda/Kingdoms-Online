package com.kingdomsonline.dto;

public record ResetPasswordRequest(String token, String newPassword) {}
