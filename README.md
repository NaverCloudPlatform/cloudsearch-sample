# Cloud Search 쉬운 시작 가이드

API 를 이용하는 경우 아래 JSON 으로 요청하시기 바랍니다.

[문서샘플 - sample.json](sample.json)

### 도메인 생성 JSON

```json
{
  "name": "free-pdf-books",
  "type": "small",
  "indexerCount": 1,
  "searcherCount": 1,
  "description": "search for pdf books"
}
```

### 스키마 생성 JSON

```json
{
  "document": {
    "primarySectionName": "name",
    "sections": [
      {
        "docProperties": [
          {
            "type": "string",
            "name": "dp_type"
          }
        ],
        "name": "name"
      },
      {
        "docProperties": [
          {
            "type": "string",
            "name": "dp_type"
          }
        ],
        "name": "topic"
      },
      {
        "docProperties": [
          {
            "type": "string",
            "name": "dp_type"
          }
        ],
        "name": "url"
      }
    ],
    "indexes": [
      {
        "documentTermWeight": "sum_wgt",
        "buildInfos": [
          {
            "indexProcessors": [
              {
                "type": "hanaterm",
                "method": "sgmt",
                "option": "+korea +josacat +eomicat"
              }
            ],
            "sectionTermWeight": "1.0 * stw_2p(tf, 0.5, 0.25, 0., length / 128.0)",
            "sections": ["topic", "name"],
            "name": "index_build"
          }
        ],
        "name": "index_name"
      }
    ]
  }
}
```

### 검색

```json
{
  "search": {
    "index_name": {
      "main": {
        "query": "test"
      }
    }
  }
}
```
