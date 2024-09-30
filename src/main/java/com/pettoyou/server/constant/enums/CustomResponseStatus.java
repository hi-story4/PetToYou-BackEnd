package com.pettoyou.server.constant.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomResponseStatus {

    /***
     * 1000: 요청 성공
     */
    SUCCESS(HttpStatus.OK.value(), "1000", "요청에 성공하였습니다."),

    /***
     * 2000: UNAUTHORIZED
     */
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED.value(), "2000", "만료된 토큰입니다."),
    BAD_JWT(HttpStatus.UNAUTHORIZED.value(), "2001", "잘못된 토큰입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "2002", "리프레시 토큰이 만료되었습니다. 재로그인을 진행해주세요."),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.CONFLICT.value(), "2003", "잘못된 리프레시 토큰입니다."),
    LOGIN_FAILED(HttpStatus.CONFLICT.value(), "2004", "아이디 또는 비밀번호가 일치하지 않습니다."),

    /***
     * 3000: ACCESS DENIED
     */
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "3000", "권한이 없습니다."),
    LOGOUT_MEMBER(HttpStatus.FORBIDDEN.value(), "3001", "로그아웃된 사용자입니다."),
    ALREADY_REGISTERED_WITH_DIFFERENT_PROVIDER(HttpStatus.CONFLICT.value(), "3002", "다른 소셜 로그인 계정으로 가입한 사용자입니다."),
    RESERVATION_ALREADY_EXIST(HttpStatus.CONFLICT.value(), "3003", "예약이 이미 존재합니다."),
    USERNAME_ALREADY_EXIST(HttpStatus.CONFLICT.value(), "3004", "사용할 수 없는 아이디 입니다."),
    SCRAP_ALREADY_EXIST(HttpStatus.CONFLICT.value(), "3005", "이미 존재하는 스크랩입니다."),

    /***
     * 4000: NOT_FOUND
     */

    NULL_JWT(HttpStatus.NO_CONTENT.value(), "4000", "토큰이 공백입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4001", "해당 유저를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4002", "리프레시 토큰을 찾을 수 없습니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4003", "해당 권한을 찾을 수 없습니다."),
    PET_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4004", "해당 반려동물을 찾을 수 없습니다."),
    BANNER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4005", "해당 배너를 찾을 수 없습니다."),
    HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4006", "해당 병원을 찾을 수 없습니다."),
    //정상응답
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4007", "해당 스크랩을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4008", "해당 매장을 찾을 수 없습니다."),
    HEALTH_NOTE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4009", "해당 건강수첩을 찾을 수 없습니다."),
    STORE_PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4010", "해당 병원의 사진을 찾을 수 없습니다."),
    VET_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4011", "해당 수의사를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4012", "해당 예약 내역을 찾을 수 없습니다"),
    //정상응답, no content
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "4013", "해당 리뷰를 찾을 수 없습니다."),

    /***
     * 5000: NOT_MATCH
     */
    MEMBER_NOT_MATCH(HttpStatus.CONFLICT.value(), "5000", "유저가 매칭되지 않습니다."),

    /***
     * 6000: Internal_Server_Error
     */

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "6000", "내부 서버 오류입니다."),
    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "6001", "S3에 파일을 업로드하지 못했습니다."),
    POINT_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "6002", "POINT Parsing FAIL"),
    STORE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "6003","STORE를 저장하는데 실패했습니다."),
    PINNED_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "6004","상단고정에 실패했습니다. "),
    RESERVATION_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "6005", "예약 방문완료 처리에 실패했습니다."),


    /***
     * 7000: InValid
     */
    INVALID_ERROR(HttpStatus.BAD_REQUEST.value(), "7000", "유효하지 않은 데이터입니다."),
    INVALID_LATITUDE_ERROR(HttpStatus.BAD_REQUEST.value(), "7001", "위도를 정확하게 입력해주세요 : 34~44"),
    INVALID_LONGITUDE_ERROR(HttpStatus.BAD_REQUEST.value(), "7002", "경도를 정확하게 입력해주세요 : 124~134"),
    INVALID_RESERVATION_DATETIME_ERROR(HttpStatus.BAD_REQUEST.value(), "7003", "유효하지 않은 예약 시간입니다.");

    private final int httpStatusCode;
    private final String code;
    private final String message;

    CustomResponseStatus(int httpStatusCode, String code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
