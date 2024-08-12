package com.pettoyou.server.domains.member.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.member.dto.request.MemberInfoModifyReqDto;
import com.pettoyou.server.domains.member.dto.response.MemberInfoQueryDto;
import com.pettoyou.server.domains.member.entity.Member;
import com.pettoyou.server.domains.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public void modifyMemberInfo(MemberInfoModifyReqDto modifyDto, Long authMemberId) {
        Member member = findMemberById(authMemberId);

        member.modifyInfo(modifyDto.newNickname());
    }

    @Override
    public MemberInfoQueryDto queryMemberInfo(Long authMemberId) {
        Member member = findMemberById(authMemberId);

        return MemberInfoQueryDto.from(member);
    }

    private Member findMemberById(Long authMemberId) {
        return memberRepository.findById(authMemberId).orElseThrow(() ->
                new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND)
        );
    }
}
