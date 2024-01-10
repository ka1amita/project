package com.matejkala.dtos.responsedtos;

public class PasswordResetResponseDTO extends ResponseDTO {
    String uniqueResetCode;

    public PasswordResetResponseDTO() {
    }

    public PasswordResetResponseDTO(String uniqueResetCode) {
        this.uniqueResetCode = uniqueResetCode;
    }

    public String getUniqueResetCode() {
        return uniqueResetCode;
    }

    public void setUniqueResetCode(String uniqueResetCode) {
        this.uniqueResetCode = uniqueResetCode;
    }
}
