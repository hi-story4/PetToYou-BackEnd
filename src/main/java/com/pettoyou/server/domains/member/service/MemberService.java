package com.pettoyou.server.domains.member.service;

import com.pettoyou.server.domains.member.dto.request.MemberInfoModifyReqDto;
import com.pettoyou.server.domains.member.dto.response.MemberInfoQueryDto;

public interface MemberService {

    void modifyMemberInfo(
            MemberInfoModifyReqDto modifyDto,
            Long authMemberId
    );

    MemberInfoQueryDto queryMemberInfo(Long authMemberId);
}
