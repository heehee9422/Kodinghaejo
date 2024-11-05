package com.kodinghaejo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "commonCode")
@Table(name = "jpa_common_code")
public class CommonCodeEntity {

	@Id
	@Column(name = "code", length = 20, nullable = false)
	private String code;

	@Column(name = "type", length = 20, nullable = false)
	private String type;

	@Column(name = "val", length = 50, nullable = false)
	private String val;

	@Column(name = "note", length = 200, nullable = true)
	private String note;

	@Column(name = "is_use", length = 2, nullable = false)
	private String isUse;

}