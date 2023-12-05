# SeedOasis

## RestAPI
### 공통 응답 포맷
#### 성공 응답
```json
{
  "success" : true,
  "response" : "response data",
  "error" : null
}
```

#### 실패 응답
```json
{
  "success" : false,
  "response" : null,
  "error" : {
    "message" : "에러 메시지",
    "status" : "HTTP 상태코드"
  }
}
```