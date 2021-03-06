package me.study.springrestapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import  lombok.*;
import me.study.springrestapi.accounts.Account;
import me.study.springrestapi.accounts.AccountSerializer;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @NoArgsConstructor
@AllArgsConstructor @Getter
@Setter @EqualsAndHashCode(of = "id")// 기본적으로 모든 필드를 확인한다. 추후 연관관계가 들어가면 stack overflow가 일어날 수도 있다.
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;


    public void update() {
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }

        if (this.location == null || this.location.isBlank()) { // java 11 version에 생긴 빈 문자열 판단 함수
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
