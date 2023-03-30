package com.gfttraining.cart.api.controller.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	private LocalDateTime timestamp;
	private String msg;

}
