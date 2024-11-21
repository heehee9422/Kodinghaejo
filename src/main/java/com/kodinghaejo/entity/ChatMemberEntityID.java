package com.kodinghaejo.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatMemberEntityId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long chatIdx;
	private String email;

}