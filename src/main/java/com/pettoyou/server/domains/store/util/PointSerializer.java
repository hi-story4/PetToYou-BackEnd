package com.pettoyou.server.domains.store.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Point;

import java.io.IOException;

public class PointSerializer extends JsonSerializer<Point> {
    /**
     * Point 객체 직렬화 시 에러 발생.
     * 인식하지 못함.
     * 커스텀 타입처럼 jsonSerializer를 이용해 직접 직렬화 해줘야한다.
     **/
    @Override
    public void serialize(Point value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject(); // JSON 오브젝트 시작
        gen.writeNumberField("latitude", value.getY()); // 위도
        gen.writeNumberField("longitude", value.getX()); //경도
        gen.writeEndObject(); // JSON 오브젝트 종료
    }
}
