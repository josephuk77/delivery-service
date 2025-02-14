package com.sparta.delivery.gemini.entity;

import com.sparta.delivery.aaglobal.Timestamped;
import com.sparta.delivery.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Table(name = "p_gemini")
public class Gemini extends Timestamped {

  @Id
  @UuidGenerator
  private UUID id;

  @Column
  private String question;

  @Column
  private String answer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
