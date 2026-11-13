# test/dto

테스트 API의 응답 데이터입니다. 모두 record로 만들었습니다.

- MessageResponse: hello API 응답. message
- SlowResponse: slow API 응답. delayMs
- ItemResponse: 상품 조회 응답. id, name
- ResultResponse: random API 응답. result

## 사용 방식

- Controller가 Service의 결과를 DTO로 감싸 ResponseEntity로 응답합니다.
- ItemResponse.from(item)은 Model인 Item을 응답용 DTO로 변환합니다.

## record

데이터를 담기만 하는 클래스를 짧게 만드는 Java 문법입니다. 필드는 변경할 수 없고, 생성자, getter, equals, hashCode, toString이 자동으로 만들어집니다. getter 이름은 getMessage()가 아니라 message()입니다.
