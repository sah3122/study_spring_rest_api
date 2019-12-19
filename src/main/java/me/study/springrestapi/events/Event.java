package me.study.springrestapi.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder @NoArgsConstructor
@AllArgsConstructor @Getter
@Setter @EqualsAndHashCode(of = "id")// 기본적으로 모든 필드를 확인한다. 추후 연관관계가 들어가면 stack overflow가 일어날 수도 있다.
public class Event {

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
    private EventStatus eventStatus = EventStatus.DRAFT;


}
