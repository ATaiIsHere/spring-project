# spring-project

## Create Table
```
CREATE TABLE `currency ` (
  currency_id VARCHAR(255),
  name VARCHAR(255)
)
```

## API
- coindesk data

  GET /currentPrice

  coindesk data transform API
  GET /currencyPrice

- 新增
  POST /currency
  
  body:
  ```
  {
      "currencyId": "TWD",
      "name": "台幣"
  }
  ```

- 讀取
  GET /currency/{id}

- 更新
  PUT /currency/{id}
  
  body:
  ```
  {
      "currencyId": "TWD",
      "name": "新台幣"
  }
  ```

- 刪除
  DELETE /currency/{id}
