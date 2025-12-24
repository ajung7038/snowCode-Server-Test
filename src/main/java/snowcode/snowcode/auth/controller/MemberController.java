package snowcode.snowcode.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import snowcode.snowcode.auth.domain.Member;
import snowcode.snowcode.auth.dto.*;
import snowcode.snowcode.auth.service.MemberService;
import snowcode.snowcode.common.response.BasicResponse;
import snowcode.snowcode.common.response.ResponseUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me/{memberId}")
    @Operation(summary = "내 정보 조회 API", description = "내 정보 조회 (이름)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MyProfileResponse.class))}),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다",
                    content = {@Content(schema = @Schema(implementation = BasicResponse.class))}),
    })
    public BasicResponse<MyProfileResponse> findMember(@Schema(description = "memberId", example = "1")
                                                           @PathVariable Long memberId) {
        MyProfileResponse myProfileResponse = memberService.findMemberById(memberId);
        return ResponseUtil.success(myProfileResponse);
    }

    @GetMapping
    @Operation(summary = "회원 전체 조회", description = "회원 전체 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 전체 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MemberCountListResponse.class))}),
    })
    public BasicResponse<MemberCountListResponse> findAllMember() {
        MemberCountListResponse dto = memberService.findAllMember();
        return ResponseUtil.success(dto);
    }

    @PostMapping("/{memberId}/students")
    @Operation(summary = "학번 입력", description = "학번 입력 (사용 X)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학번 업데이트 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddProfileResponse.class))}),
    })
    public BasicResponse<AddProfileResponse> updateStudentId(@PathVariable Long memberId, @Valid @RequestBody AddProfileRequest dto) {
        Member member = memberService.findMember(memberId);
        AddProfileResponse addProfileResponse = memberService.updateStudentId(member, dto.studentId());
        return ResponseUtil.success(addProfileResponse);
    }
}
