# Release

```
./gradlew build release -Prelease.useAutomaticVersion=true
```

# DDL 생성

## 명령어
```
./gradlew generateSchema
```

## 출력 위치
```
build/generated-schema/create.sql
```

# IntelliJ Setting

* Settings
  * Build, Execution, Deployment > Build Tools > Gradle > Runner
    * Delegate IDE build/run actions to Gradle 활성화

# 테스트 데이터


 id            | status | bom | process | bom
---------------|--------|-----|---------|-----
 i-0           |        |     |         |
 i-1           |        |     |         |
