package app.labs.linksy.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data // Getter, Setter, equals, hashCode, toString 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 자동 생성
public class FeedHashtagId implements Serializable {
    private int feedId;
    private int hashtagId;
}
