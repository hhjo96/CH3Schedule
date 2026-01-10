package com.example.schedule.schedule.service;


import com.example.schedule.comments.repository.CommentRepository;
import com.example.schedule.exception.ForbiddenException;
import com.example.schedule.exception.InvalidNumberException;
import com.example.schedule.exception.UserNotFoundException;
import com.example.schedule.mapper.ScheduleMapper;
import com.example.schedule.schedule.dto.*;
import com.example.schedule.schedule.dto.projection.CommentCountDto;
import com.example.schedule.schedule.dto.projection.CommentCountInterface;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.schedule.repository.ScheduleRepository;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    //create schedule
    @Transactional
    public ScheduleCreateResponse save(Long userId, ScheduleCreateRequest request) {
        //세션으로 로그인 된 유저의 아이디이므로 현재는 유저 검증이 불필요하긴 하나 유저 엔티티가 필요하므로 그냥 두었음
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        Schedule schedule = ScheduleMapper.getScheduleInstance(user, request);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ScheduleMapper.getScheduleCreateResponseInstance(savedSchedule);
    }

    //read schedule - all, paging
    public Page<ScheduleGetPageResponse> getSchedulesWithPaging(Long userId, int page, int size) {
        if(page < 0) throw new InvalidNumberException("illegal page number");
        if(size < 0) throw new InvalidNumberException("illegal page size");

        //사용자의 일정을 가져오고, 수정일 순으로 정렬하기
        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        Page<Schedule> pagedSchedule = scheduleRepository.findAllByUserIdAndDeletedFalse(userId, pageable);

        //댓글 수 가져오기 -> 일정번호 리스트와 댓글수 리스트를 map으로 연결
        //프로젝션을 사용하고 싶은데 잘 된건지 모르겠음
        List<Long> scheduleIds = pagedSchedule.getContent().stream().map(Schedule::getId).toList();
        if(scheduleIds.isEmpty()) {
            throw new ScheduleNotFoundException("you don't have any schedules");
        }
        List<CommentCountInterface> commentCountInterfaces = commentRepository.countByScheduleId(scheduleIds);
        List<CommentCountDto> commentCountDtos = commentCountInterfaces.stream()
                .map(p -> new CommentCountDto(p.getScheduleId(), p.getCommentCount())).toList();
        Map<Long, Long> commentCountMap = new HashMap<>();
        for(CommentCountDto i : commentCountDtos){
            commentCountMap.put(i.getScheduleId(), i.getCommentCount());
        }
        // 댓글수가 널인경우 0으로 변환
         return pagedSchedule.map(
                schedule -> ScheduleMapper.getScheduleGetPageResponseInstance(schedule, commentCountMap.getOrDefault(schedule.getId(), 0L)));
    }


    //read schedule - all
    @Transactional(readOnly = true)
    public List<ScheduleGetResponse> findAllByUserId(Long userId) {
        //유저가 없다면 탈퇴한 사용자라고 뜬다
        //자기가 작성한 일정만 보임
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndDeletedFalse(userId);

        return schedules.stream()
                .map(ScheduleMapper::getScheduleGetResponseInstance).toList();
    }

    //read schedule - one
    @Transactional(readOnly = true)
    public ScheduleGetResponse findOne(Long userId, Long scheduleId) {
        //스케줄이 없는 경우 체크, 유저가 없는 경우 탈퇴한 사용자라고 뜸
        Schedule schedule = findScheduleByIdAndDeletedFalseOrThrow(scheduleId);
        //자기가 작성한 일정만 보임
        if(!schedule.getUser().getId().equals(userId)) throw new ForbiddenException("not your schedule");
        return ScheduleMapper.getScheduleGetResponseInstance(schedule);
    }

    //read schedule - admin, all
    @Transactional(readOnly = true)
    public List<ScheduleGetResponse> findAdminAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleMapper::getScheduleGetResponseInstance).toList();
    }

    //update schedule
    @Transactional
    public ScheduleUpdateResponse update(Long userId, Long scheduleId, ScheduleUpdateRequest request) {
        //스케줄을 작성한 유저가 맞는지 검증
        //유저 자체의 검증은 세션에서 함
        Schedule schedule = findScheduleByIdAndDeletedFalseOrThrow(scheduleId);
        //자기가 작성한 일정만
        if(!schedule.getUser().getId().equals(userId)) throw new ForbiddenException("not your schedule");

        schedule.update(request.getTitle(), request.getContent());

        return ScheduleMapper.getScheduleUpdateResponseInstance(schedule);
    }

    //delete schedule - soft delete, 로그인한 유저의 일정만 삭제 가능
    @Transactional
    public void delete(Long scheduleId, Long userId) {
        //스케줄을 작성한 유저가 맞는지 검증 후 삭제
        Schedule schedule = findScheduleByIdAndDeletedFalseOrThrow(scheduleId);
        //자기가 작성한 일정만
        if(!schedule.getUser().getId().equals(userId)) throw new ForbiddenException("not your schedule");

        schedule.delete();
    }

    private Schedule findScheduleByIdAndDeletedFalseOrThrow(Long scheduleId){
        return scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new ScheduleNotFoundException("schedule not found"));
    }

}
