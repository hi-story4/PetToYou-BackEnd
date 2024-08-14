package com.pettoyou.server.domains.member.dto.response;

import com.pettoyou.server.domains.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoQueryDto(
        Long memberId,
        String name,
        String nickname,
        String phone,
        String email
) {
    public static MemberInfoQueryDto from(Member member) {
        return MemberInfoQueryDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .nickname(member.getNickName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .build();
    }
}
